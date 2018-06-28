package ru.sportmaster.esm.user.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sportmaster.esm.loyalty.clubpro.ClubProException;
import ru.sportmaster.esm.user.service.RegistrationException;
import ru.sportmaster.esm.user.service.exceptions.AuthException;
import ru.sportmaster.esm.user.service.exceptions.UserProfileNotFoundException;
import ru.sportmaster.esm.user.service.exceptions.WrongActivationCodeException;
import ru.sportmaster.esm.user.service.exceptions.PasswordResetException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(WrongActivationCodeException.class)
    protected ResponseEntity<ErrorInfo> handle(WrongActivationCodeException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getMessage()
                , HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordResetException.class)
    protected ResponseEntity<ErrorInfo> handle(PasswordResetException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getMessage(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorInfo> handle(AuthException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getMessage(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    protected ResponseEntity<ErrorInfo> handle(UserProfileNotFoundException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getMessage(),
                HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClubProException.class)
    protected ResponseEntity<ErrorInfo> handle(ClubProException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getMessage(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    public class ErrorInfo {

        private String message;
        private int status;

        ErrorInfo(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }
}


