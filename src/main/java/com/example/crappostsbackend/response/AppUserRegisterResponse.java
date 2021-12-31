package com.example.crappostsbackend.response;

import com.example.crappostsbackend.model.AppUser;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AppUserRegisterResponse {
    @JsonProperty("user")
    private AppUser appUser;
    @JsonProperty("errorMessages")
    private Map<String, String> errorMessages;

    public AppUserRegisterResponse(AppUser appUser, Map<String, String> errorMessages) {
        this.appUser = appUser;
        this.errorMessages = errorMessages;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
