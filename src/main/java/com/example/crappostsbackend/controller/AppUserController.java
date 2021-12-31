package com.example.crappostsbackend.controller;

import com.example.crappostsbackend.exception.BadFormDetailsException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.AppUserLoginFormData;
import com.example.crappostsbackend.formdata.AppUserRegisterFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.response.AppUserLoginResponse;
import com.example.crappostsbackend.response.AppUserRegisterResponse;
import com.example.crappostsbackend.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<AppUser>> getAppUsers() {
        List<AppUser> appUsers = appUserService.getAllAbledAppUsers();
        return ResponseEntity.ok().body(appUsers);
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable(name="id") long userId) throws ResourceNotFoundException {
        AppUser appUser = appUserService.getAbledAppUser(userId);
        return ResponseEntity.ok().body(appUser);
    }

    @GetMapping(path = "/users/info")
    public ResponseEntity<AppUser> getUserInfo()
    {
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        return ResponseEntity.ok().body(appUser);
    }

    @PostMapping(path = "/users/register")
    public ResponseEntity<AppUserRegisterResponse> createUser(@Valid @RequestBody AppUserRegisterFormData appUserRegisterFormData) throws BadFormDetailsException {
        AppUserRegisterResponse appUserRegisterResponse = appUserService.createAppUser(appUserRegisterFormData);
        return ResponseEntity.ok().body(appUserRegisterResponse);
    }

    @PostMapping(path = "/users/login")
    public ResponseEntity<AppUserLoginResponse> loginUser(@Valid @RequestBody AppUserLoginFormData appUserLoginFormData) throws BadFormDetailsException {
        AppUserLoginResponse appUserLoginResponse = appUserService.loginUser(appUserLoginFormData);
        return ResponseEntity.ok().body(appUserLoginResponse);
    }



}
