package ru.sportmaster.esm.constraint;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private final PhoneNumberUtil phoneNumberUtil;

    public PhoneValidator() {
        phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(value, null));
        } catch (NumberParseException e) {
            return false;
        }
    }
}
