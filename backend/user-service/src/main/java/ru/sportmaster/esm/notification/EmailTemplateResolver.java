package ru.sportmaster.esm.notification;

import ru.sportmaster.esm.mail.templates.EmailTemplates;

@FunctionalInterface
public interface EmailTemplateResolver {

    EmailTemplate resolve(EmailTemplates template);
}
