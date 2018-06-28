package ru.sportmaster.esm.mail.service;

import ru.sportmaster.esm.mail.EmailInfo;

public interface MailService {
    boolean sendEmail(String to, String from, String subject, String content, boolean isMultipart,
                      boolean isHtml);

    boolean sendEmail(EmailInfo emailInfo);
}
