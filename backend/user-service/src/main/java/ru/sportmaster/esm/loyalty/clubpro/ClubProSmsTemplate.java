package ru.sportmaster.esm.loyalty.clubpro;

import ru.sportmaster.esm.notification.SmsTemplate;

public enum ClubProSmsTemplate implements SmsTemplate {

    CONFIRM_TEXT(4),
    RESTORE_PASSWORD(5),
    DELIVERY_ORDER(6),
    PICKUP_ORDER(7),
    CONFIRM_CODE(8),
    ANYTHING(9),
    CHANGE_PHONE(10);

    private final int code;

    ClubProSmsTemplate(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
