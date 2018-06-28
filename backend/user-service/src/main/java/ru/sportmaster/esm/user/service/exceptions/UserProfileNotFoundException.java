package ru.sportmaster.esm.user.service.exceptions;

public class UserProfileNotFoundException extends Exception {
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
