package ru.sportmaster.esm.mail.templates;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.sportmaster.esm.mail.configuration.EmailServerProperties;
import ru.sportmaster.esm.mail.EmailInfo;
import ru.sportmaster.esm.mail.service.MailService;
import ru.sportmaster.esm.notification.EmailTemplate;
import ru.sportmaster.esm.user.dao.Profile;

import java.util.Locale;
import java.util.Objects;

@Component
public class ThymeleafTemplate implements EmailTemplate {

    private static final String PROFILE = "profile";
    private static final String BASE_URL = "baseUrl";
    private static final String CODE = "code";
    private final MailService mailService;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final EmailServerProperties serverProperties;

    private EmailTemplates template;

    public ThymeleafTemplate(MailService mailService, MessageSource messageSource, SpringTemplateEngine
            templateEngine, EmailServerProperties serverProperties) {
        this.mailService = Objects.requireNonNull(mailService);
        this.messageSource = Objects.requireNonNull(messageSource);
        this.templateEngine = Objects.requireNonNull(templateEngine);
        this.serverProperties = serverProperties;
    }

    public boolean send(Profile profile, String code) {
        return mailService.sendEmail(prepareEmailInfo(profile, code));
    }

    private EmailInfo prepareEmailInfo(Profile profile, String code) {

        if (profile == null)
            throw new IllegalArgumentException("profile not specified!");
        if (template == null)
            throw new IllegalArgumentException("template not specified!");

        Locale lang = Locale.forLanguageTag(serverProperties.getLocale());
        Context context = new Context(lang);
        context.setVariable(PROFILE, profile);
        context.setVariable(BASE_URL, serverProperties.getAppHost());
        context.setVariable(CODE, code);
        String content = templateEngine.process(template.getName(), context);
        String subject = messageSource.getMessage("email." + template.getName() +".title", null, lang);
        return new EmailInfo(profile.getEmail(), serverProperties.getSender(), subject, content);
    }

    public void setTemplate(EmailTemplates template) {
        this.template = template;
    }
}
