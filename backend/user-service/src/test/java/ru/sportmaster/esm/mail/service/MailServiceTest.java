package ru.sportmaster.esm.mail.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.sportmaster.esm.SpringTestConfig;
import ru.sportmaster.esm.confirmation.configuration.EmailConfirmationProperties;
import ru.sportmaster.esm.mail.configuration.EmailServerProperties;
import ru.sportmaster.esm.confirmation.service.EmailConfirmationService;
import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.mail.templates.ThymeleafTemplate;
import ru.sportmaster.esm.user.dao.Profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class MailServiceTest extends SpringTestConfig  {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private EmailConfirmationProperties confirmationProps;
    @Autowired
    private EmailServerProperties serverProperties;
    @Autowired
    private EmailConfirmationService codeService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    ThymeleafTemplate confirmationMail;

    @BeforeEach
    public void init() {
        this.javaMailSender = mock(javaMailSender.getClass());
        when(this.javaMailSender.createMimeMessage()).thenCallRealMethod();
        confirmationMail = new ThymeleafTemplate(new MailServiceImpl(this.javaMailSender),
                messageSource, templateEngine, serverProperties);
        confirmationMail.setTemplate(EmailTemplates.COMFIRM_EMAIL);
    }

    @Test
    public void testSendActivationEmail() {
        Profile profile = new Profile();
        profile.setPassword("12345");
        profile.setName("test username");
        profile.setId("12345");
        profile.setPhone("+79191111111");
        profile.setEmail("test@user.notexist");
        boolean sent = confirmationMail.send(profile, "test");
        assertTrue("error when sending email", sent);
    }
}
