package ru.sportmaster.esm.user.dao;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.sportmaster.esm.user.dto.Subscription;
import ru.sportmaster.esm.user.dto.Subscriptions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document(collection = "profile")
public class Profile {

    @Id
    private String id;

    private String name;
    private long bonuses;
    private String password;
    private boolean needChangePassword;
    private boolean emailConfirmationRequired;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phone;
    @CreatedDate
    private long createdOn;
    private boolean subscribed;

    private Set<Subscription> subscriptions;

    public Profile() {
        createdOn = System.currentTimeMillis();
        subscriptions = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBonuses() {
        return bonuses;
    }

    public void setBonuses(long bonuses) {
        this.bonuses = bonuses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNeedChangePassword() {
        return needChangePassword;
    }

    public void setNeedChangePassword(boolean needChangePassword) {
        this.needChangePassword = needChangePassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscribed = (subscriptions.size() > 0);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
        subscribed = (subscriptions.size() == 0);
    }

    public boolean hasSubscription(Subscription subscription) {
        return subscriptions.contains(subscription);
    }

    public boolean hasActiveSubscription(Subscription subscription) {
        return subscriptions.stream().anyMatch(item -> item.equals(subscription)
                && item.getIsSubscribed() == subscription.getIsSubscribed());
    }

    public boolean getEmailConfirmationRequired() {
        return emailConfirmationRequired;
    }

    public void setEmailConfirmationRequired(boolean emailConfirmationRequired) {
        this.emailConfirmationRequired = emailConfirmationRequired;
    }

    public boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Profile other = (Profile) o;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
