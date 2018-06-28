package ru.sportmaster.esm.confirmation.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.confirmation.domain.EmailConfirmation;
import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.user.dao.Profile;

public interface ConfirmationService {

    Mono<Void> deactivateNotification(EmailConfirmation confirmation);

    void invalidate(String code);

    Mono<String> validate(String code);

    Mono<Boolean> sendNotificationMail(Profile profile, EmailTemplates template);

    Flux<EmailConfirmation> listOfNeededToBeReminded();

    Flux<EmailConfirmation> listOfExpired();
}
