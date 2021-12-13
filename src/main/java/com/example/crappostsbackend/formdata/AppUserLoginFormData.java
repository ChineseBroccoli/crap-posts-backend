package com.example.crappostsbackend.formdata;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class AppUserLoginFormData implements Serializable {

    @NotEmpty(message = "Username cannot be empty!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
