package ru.sportmaster.esm.confirmation.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Document
public class EmailConfirmation implements Serializable {

    @Id
    private final String code;

    private final ZonedDateTime createdOn;

    private final ZonedDateTime remindDate;

    private final LocalDateTime expiredDate; //TODO: ZoneDateTime comp-ns is not supported by Springdata. Find a way out.

    @Indexed
    private final String profileId;

    private final ConfirmationType confirmationType;

    @Indexed
    private boolean needToRemind;

    @PersistenceConstructor
    public EmailConfirmation(String code, ZonedDateTime createdOn, ZonedDateTime remindDate, LocalDateTime
            expiredDate, String profileId, ConfirmationType confirmationType) {
        this.code = code;
        this.createdOn = createdOn;
        this.remindDate = remindDate;
        this.expiredDate = expiredDate;
        this.profileId = profileId;
        this.confirmationType = confirmationType;
    }

    public EmailConfirmation(ConfirmationType confirmationType, String code, String profileId, Duration remindAfter,
        Duration expireAfter) {

        this.profileId = profileId;
        this.confirmationType = confirmationType;
        this.code = code;
        this.createdOn = ZonedDateTime.now();
        if (remindAfter != Duration.ZERO) {
            this.remindDate = this.createdOn.plus(remindAfter);
            this.needToRemind = true;
        }
        else {
            this.remindDate = null;
            this.needToRemind = false;
        }
        if (expireAfter != Duration.ZERO) {
            this.expiredDate = this.createdOn.plus(expireAfter.toMillis(), ChronoUnit.MILLIS).toLocalDateTime();
        }
        else {
            this.expiredDate = null;
        }
    }

    public String getCode() {
        return code;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public String getProfileId() {
        return profileId;
    }

    public ZonedDateTime getRemindDate() {
        return remindDate;
    }

    public boolean getNeedToRemind() {
        return needToRemind;
    }

    public void setNeedToRemind(boolean needToRemind) {
        this.needToRemind = needToRemind;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailConfirmation other = (EmailConfirmation) o;
        return Objects.equals(getProfileId(), other.getProfileId()) &&
                Objects.equals(getConfirmationType(), other.getConfirmationType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getCode(), getCreatedOn());
    }

    @Override
    public String toString() {
        return "EmailConfirmation: {" +
            "code: " + getCode() +
            "profileId: " + getProfileId() +
            "}";
    }

}
