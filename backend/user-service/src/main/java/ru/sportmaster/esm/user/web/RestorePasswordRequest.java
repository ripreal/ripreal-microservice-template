package ru.sportmaster.esm.user.web;

import javax.validation.constraints.Email;

public class RestorePasswordRequest {

    @Email(message = "error.email_invalid")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
