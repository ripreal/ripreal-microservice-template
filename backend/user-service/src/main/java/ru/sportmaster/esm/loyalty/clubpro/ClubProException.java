package ru.sportmaster.esm.loyalty.clubpro;

public class ClubProException extends RuntimeException {

    private final long code;

    public ClubProException(long code, String message) {
        super(message);
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
