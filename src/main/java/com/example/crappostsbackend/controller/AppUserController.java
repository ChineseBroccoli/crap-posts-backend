package com.example.crappostsbackend.controller;

import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.AppUserLoginFormData;
import com.example.crappostsbackend.formdata.AppUserRegisterFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.response.AppUserLoginResponse;
import com.example.crappostsbackend.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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

    @PostMapping(path = "/users/register")
    public ResponseEntity<AppUser> createUser(@Valid @RequestBody AppUserRegisterFormData appUserRegisterFormData) {
        AppUser createdUser = appUserService.createAppUser(appUserRegisterFormData);
        return ResponseEntity.ok().body(createdUser);
    }

    @PostMapping(path = "/users/login")
    public ResponseEntity<AppUserLoginResponse> loginUser(@Valid @RequestBody AppUserLoginFormData appUserLoginFormData) {
        String jwt = appUserService.loginUser(appUserLoginFormData);
        AppUserLoginResponse appUserLoginResponse = new AppUserLoginResponse(jwt);
        return ResponseEntity.ok().body(appUserLoginResponse);
    }



}
