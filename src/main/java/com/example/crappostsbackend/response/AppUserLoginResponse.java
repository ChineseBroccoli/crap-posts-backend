package com.example.crappostsbackend.response;

public class AppUserLoginResponse {
    private String jwt;

    public AppUserLoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
