package ru.sportmaster.esm.user.web;

import javax.validation.constraints.NotEmpty;

public class AuthRequest {

    @NotEmpty(message = "error.email_invalid")
    private String email;

    @NotEmpty(message = "error.password_invalid")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
