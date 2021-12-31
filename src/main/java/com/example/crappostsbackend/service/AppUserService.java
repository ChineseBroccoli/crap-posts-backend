package com.example.crappostsbackend.service;

import com.example.crappostsbackend.exception.BadFormDetailsException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.AppUserLoginFormData;
import com.example.crappostsbackend.formdata.AppUserRegisterFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.repository.AppUserRepository;
import com.example.crappostsbackend.response.AppUserLoginResponse;
import com.example.crappostsbackend.response.AppUserRegisterResponse;
import com.example.crappostsbackend.util.JsonWebTokenUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenUtility jsonWebTokenUtility;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository , @Lazy PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JsonWebTokenUtility jsonWebTokenUtility) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jsonWebTokenUtility = jsonWebTokenUtility;
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

    public AppUserRegisterResponse createAppUser(AppUserRegisterFormData appUserRegisterFormData) throws BadFormDetailsException {
        log.info("Regisering User...");

        log.info("username: " + appUserRegisterFormData.getUsername());
        log.info("password: " + appUserRegisterFormData.getPassword());
        log.info("passwordConfirm: " + appUserRegisterFormData.getPasswordConfirm());

        Map<String, String> errorMessages = new HashMap<>();

        if (appUserRegisterFormData.getUsername().isBlank())
        {
            errorMessages.put("username", "Please input your Username!");
        }
        else
        {
            AppUser alreadyExistingUser = appUserRepository.findByUsername(appUserRegisterFormData.getUsername());
            if (alreadyExistingUser != null)
            {
                errorMessages.put("username", "Username already exists!");
            }
        }

        if (appUserRegisterFormData.getPassword().isBlank())
        {
            errorMessages.put("password", "Please input your Password!");
        }

        if (appUserRegisterFormData.getPasswordConfirm().isBlank())
        {
            errorMessages.put("passwordConfirm", "Please confirm your Password!");
        }
        else
        {
            if (!appUserRegisterFormData.getPassword().equals(appUserRegisterFormData.getPasswordConfirm()))
            {
                errorMessages.put("passwordConfirm", "Passwords do not match!");
            }
        }

        if (errorMessages.size() > 0)
        {
            throw new BadFormDetailsException(errorMessages);
        }

        AppUser createdAppUser = new AppUser();
        createdAppUser.setUsername(appUserRegisterFormData.getUsername());
        String rawPassword = appUserRegisterFormData.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        createdAppUser.setPassword(encodedPassword);
        AppUser savedAppUser = this.appUserRepository.save(createdAppUser);

        return new AppUserRegisterResponse(savedAppUser, null);
    }

    public AppUserLoginResponse loginUser (AppUserLoginFormData appUserLoginFormData) throws BadFormDetailsException {
        log.info("Logging in User...");

        Map<String, String> errorMessages = new HashMap<>();

        if (appUserLoginFormData.getUsername().isBlank())
        {
            errorMessages.put("username", "Please input your Username!");
        }
        if (appUserLoginFormData.getPassword().isBlank())
        {
            errorMessages.put("password", "Please input your Password!");
        }

        UserDetails userDetails = null;

        try {
            userDetails = loadUserByUsername(appUserLoginFormData.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUserLoginFormData.getUsername(),
                            appUserLoginFormData.getPassword()
                    ));
        }
        catch (UsernameNotFoundException usernameNotFoundException)
        {
            errorMessages.put("username", "Username does not exist!");
        }
        catch (AuthenticationException authenticationException)
        {
            errorMessages.put("password", "Password is incorrect!");
        }

        if (errorMessages.size() > 0) {
            throw new BadFormDetailsException(errorMessages);
        }

        final String jsonWebToken = jsonWebTokenUtility.generateToken(userDetails);
        final AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername());
        return new AppUserLoginResponse(jsonWebToken, appUser, null);
    }

    public AppUser getAuthenticatedAppUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = appUserRepository.findByUsername(user.getUsername());
        return appUser;
    }

    public Post checkOwnedPost(AppUser appUser, Post post) throws ResourceNotFoundException{
        if (post.getCreator().getId() != appUser.getId()){
            throw ResourceNotFoundException.userNotOwnPost(post.getId());
        }
        return post;
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
