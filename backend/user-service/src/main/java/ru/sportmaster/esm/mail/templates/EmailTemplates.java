package ru.sportmaster.esm.mail.templates;

public enum EmailTemplates {

    RESET_PASSWORD("resetPassword"),
    RECIEVE_PRMOTIONS("promotions"),
    COMFIRM_EMAIL("activation");

    private final String name;

    EmailTemplates(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
