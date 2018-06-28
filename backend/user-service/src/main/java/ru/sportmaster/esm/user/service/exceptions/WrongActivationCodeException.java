package ru.sportmaster.esm.user.service.exceptions;

public class WrongActivationCodeException extends RuntimeException {
    public WrongActivationCodeException(String msg) {
        super(msg);
    }
}
