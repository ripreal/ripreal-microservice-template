package ru.sportmaster.esm.confirmation.configuration;

import ru.sportmaster.esm.confirmation.domain.AlphabetType;

public class EmailConfirmationProperties {

    private int codeLength;
    private long emailVerificationRemindInterval;
    private long emailVerificationTimeToLive;
    private long passwordRestoreTimeToLive;
    private AlphabetType alphabetType;

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public AlphabetType getAlphabetType() {
        return alphabetType;
    }

    public void setAlphabetType(AlphabetType alphabetType) {
        this.alphabetType = alphabetType;
    }

    public long getEmailVerificationRemindInterval() {
        return emailVerificationRemindInterval;
    }

    public void setEmailVerificationRemindInterval(long emailVerificationRemindInterval) {
        this.emailVerificationRemindInterval = emailVerificationRemindInterval;
    }

    public long getPasswordRestoreTimeToLive() {
        return passwordRestoreTimeToLive;
    }

    public void setPasswordRestoreTimeToLive(long passwordRestoreTimeToLive) {
        this.passwordRestoreTimeToLive = passwordRestoreTimeToLive;
    }

    public long getEmailVerificationTimeToLive() {
        return emailVerificationTimeToLive;
    }

    public void setEmailVerificationTimeToLive(long emailVerificationTimeToLive) {
        this.emailVerificationTimeToLive = emailVerificationTimeToLive;
    }
}
