package ru.sportmaster.esm.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.sportmaster.esm.mail.EmailInfo;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Service for sending emails.
 */
@Service
public class MailServiceImpl implements MailService {

    private final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendEmail(String to, String from, String subject, String content, boolean isMultipart,
                             boolean isHtml) {

        Objects.requireNonNull(to);
        Objects.requireNonNull(from);

        boolean sent = false;

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content=\n{}",
                    isMultipart, isHtml, to, subject, content);
            sent = true;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
        return sent;
    }

    @Override
    public boolean sendEmail(EmailInfo emailInfo) {
        return sendEmail(emailInfo.getTo(), emailInfo.getFrom(), emailInfo.getSubject(), emailInfo.getContent(),
                false, true);
    }
}
