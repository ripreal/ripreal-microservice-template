package ru.sportmaster.esm.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;


public class Subscription {
    /**
     * Признак того, что подписка на рассылку активна
     */
    @NotNull(message = "error.requiredField.isSubscribed")
    private Boolean isSubscribed;

    /**
     * Способ рассылки (например: Email).
     */
    @NotEmpty(message = "error.requiredField.pointOfContact")
    private String pointOfContact;

    /**
     * Тип рассылки (например Promotions)
     */
    @NotEmpty(message = "error.requiredField.topic")
    private String topic;

    public boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean subscribed) {
        this.isSubscribed = subscribed;
    }

    public String getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(String pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Subscription)) {
            return false;
        }

        Subscription other = (Subscription) o;
        return Objects.equals(getTopic(), other.getTopic()) &&
            Objects.equals(getPointOfContact(), other.getPointOfContact());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopic(), getPointOfContact());
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscribed=" + getIsSubscribed() +
                "pointOfContact=" + getPointOfContact() +
                "topic=" + getTopic() +
                "}";
    }
}
