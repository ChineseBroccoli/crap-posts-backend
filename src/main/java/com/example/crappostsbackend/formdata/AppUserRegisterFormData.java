package com.example.crappostsbackend.formdata;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class AppUserRegisterFormData implements Serializable {

    private String username;

    private String password;

    private String passwordConfirm;

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
