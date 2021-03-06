package com.example.crappostsbackend.response;

import com.example.crappostsbackend.model.AppUser;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AppUserLoginResponse {
    @JsonProperty("jsonWebToken")
    private String jsonWebToken;
    @JsonProperty("user")
    private AppUser appUser;
    @JsonProperty("errorMessages")
    private Map<String, String> errorMessages;

    public AppUserLoginResponse(String jsonWebToken, AppUser appUser, Map<String, String> errorMessages) {
        this.jsonWebToken = jsonWebToken;
        this.appUser = appUser;
        this.errorMessages = errorMessages;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
