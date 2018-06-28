package ru.sportmaster.esm.notification;

import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.user.dao.Profile;

public interface EmailTemplate {
    boolean send(Profile profile, String code);
    void setTemplate(EmailTemplates template);
}
