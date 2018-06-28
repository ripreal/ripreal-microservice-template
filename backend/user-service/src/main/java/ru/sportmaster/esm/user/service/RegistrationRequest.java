package ru.sportmaster.esm.user.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sportmaster.esm.constraint.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {

    @NotEmpty(message = "error.name_empty")
    @Size(min = 2, max = 64)
    private final String name;

    @NotEmpty(message = "error.city_name_empty")
    private final String cityName;

    @NotEmpty(message = "error.password_empty")
    @Size(min = 6, max = 32)
    private final String password;

    @NotEmpty(message = "error.phone_empty")
    @Phone(message = "error.phone_invalid")
    private final String phone;

    @NotEmpty(message = "error.email_empty")
    @Email(message = "error.email_invalid")
    private final String email;

    @NotEmpty(message = "error.code_empty")
    private final String code;

    @NotNull(message = "error.subscription_empty")
    private final Boolean subscription;

    @JsonCreator
    public RegistrationRequest(@JsonProperty("name") String name, @JsonProperty("cityName") String cityName,
                               @JsonProperty("password") String password, @JsonProperty("phone") String phone,
                               @JsonProperty("email") String email, @JsonProperty("code") String code,
                               @JsonProperty("subscription") Boolean subscription) {
        this.name = name;
        this.cityName = cityName;
        this.password = password;
        this.phone = (phone == null ? null : phone.replaceAll(" ", ""));
        this.email = (email == null ? null : email.toLowerCase());
        this.code = code;
        this.subscription = subscription;
    }

    public String getName() {
        return name;
    }

    public String getCityName() {
        return cityName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public Boolean getSubscription() {
        return subscription;
    }

}
