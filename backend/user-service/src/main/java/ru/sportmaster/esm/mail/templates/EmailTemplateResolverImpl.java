package ru.sportmaster.esm.mail.templates;

import org.springframework.stereotype.Component;
import ru.sportmaster.esm.notification.EmailTemplate;
import ru.sportmaster.esm.notification.EmailTemplateResolver;

@Component
public class EmailTemplateResolverImpl implements EmailTemplateResolver {

    private EmailTemplate template;

    public EmailTemplateResolverImpl(EmailTemplate template) {
        this.template = template;
    }

    @Override
    public EmailTemplate resolve(EmailTemplates templateId) {
        template.setTemplate(templateId);
        return template;
    }

}
