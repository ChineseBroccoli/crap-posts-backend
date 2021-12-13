package com.example.crappostsbackend.service;

import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.AppUserLoginFormData;
import com.example.crappostsbackend.formdata.AppUserRegisterFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.repository.AppUserRepository;
import com.example.crappostsbackend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository , @Lazy PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public List<AppUser> getAllAbledAppUsers() {
        log.info("Getting all Users...");
        return this.appUserRepository
                .findAll()
                .stream()
                .filter(user -> !user.getIsDisabled())
                .collect(Collectors.toList());
    }

    public AppUser getAbledAppUser(Long userId) throws ResourceNotFoundException {
        log.info("Getting User with id :: " + userId);
        AppUser appUser = this.appUserRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.userNotFoundById(userId));
        if (appUser.getIsDisabled()) {
            throw ResourceNotFoundException.userNotFoundById(userId);
        }
        return appUser;

    }

    public AppUser createAppUser(AppUserRegisterFormData appUserRegisterFormData) {
        AppUser createdAppUser = new AppUser();
        createdAppUser.setUsername(appUserRegisterFormData.getUsername());
        String rawPassword = appUserRegisterFormData.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        createdAppUser.setPassword(encodedPassword);
        log.info("Registering User...");
        log.info(createdAppUser.toString());
        return this.appUserRepository.save(createdAppUser);
    }

    public String loginUser (AppUserLoginFormData appUserLoginFormData) {
        log.info("Logging in User...");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUserLoginFormData.getUsername(), appUserLoginFormData.getPassword())
        );
        final UserDetails userDetails = loadUserByUsername(appUserLoginFormData.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return jwt;
    }

    public AppUser getAuthenticatedAppUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = appUserRepository.findByUsername(user.getUsername());
        return appUser;
    }

    public Post getAuthenticatedUserPosts(AppUser appUser, Long postId) throws ResourceNotFoundException{
        Post ownedPost = appUser.getPosts().stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .orElseThrow(() -> ResourceNotFoundException.userNotOwnPost(postId));
        return ownedPost;
    }

    public Post addPost(AppUser appUser, Post post){
        appUser.getPosts().add(post);
        appUserRepository.save(appUser);
        return post;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null)
        {
            throw new UsernameNotFoundException("User not found in the database!");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
