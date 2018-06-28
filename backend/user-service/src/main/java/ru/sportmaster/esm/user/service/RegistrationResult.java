package ru.sportmaster.esm.user.service;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Результат регистрации пользователя.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationResult {

    public static RegistrationResult success(String customerId) {
        RegistrationResult result = new RegistrationResult();
        result.setCustomerId(customerId);

        return result;
    }

    public static RegistrationResult error(List<String> errors) {
        RegistrationResult result = new RegistrationResult();
        result.setErrors(errors);

        return result;
    }

    private String customerId;
    private Boolean emailConfirmationRequired;
    private Boolean newSiteCustomer;
    private Set<String> errors;

    public RegistrationResult() {
        errors = new HashSet<>();
    }

    /**
     * Идентификатор зарегистрированного пользователя.
     *
     * @return идентификатор пользователя
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Устанавливает идентификатор зарегистрированного пользователя.
     *
     * @param customerId идентификатор зарегистрированного пользователя
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Возвращает список ошибок при регистрации.
     *
     * @return список ошибок
     */
    public Collection<String> getErrors() {
        return errors;
    }

    /**
     * Устанавливает список ошибок при регистрации.
     *
     * @param errors список ошибок
     */
    public void setErrors(Collection<String> errors) {
        this.errors = new HashSet<>(errors);
    }

    /**
     * Требуется ли подтверждение email пользователя.
     *
     * @return требуется ли подтверждение email пользователя
     */
    public Boolean isEmailConfirmationRequired() {
        return emailConfirmationRequired;
    }

    public void setEmailConfirmationRequired(boolean emailConfirmationRequired) {
        this.emailConfirmationRequired = emailConfirmationRequired;
    }

    /**
     * Впервые ли этот пользователь регистрируется на сайте.
     *
     * @return впервые ли этот пользователь регистрируется на сайте
     */
    public Boolean isNewSiteCustomer() {
        return newSiteCustomer;
    }

    public void setNewSiteCustomer(boolean newSiteCustomer) {
        this.newSiteCustomer = newSiteCustomer;
    }
}
