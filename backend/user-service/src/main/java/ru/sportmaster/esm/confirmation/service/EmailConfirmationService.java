package ru.sportmaster.esm.confirmation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.confirmation.configuration.EmailConfirmationProperties;
import ru.sportmaster.esm.confirmation.domain.ConfirmationType;
import ru.sportmaster.esm.confirmation.domain.EmailConfirmation;
import ru.sportmaster.esm.confirmation.repository.EmailConfirmationRepository;
import ru.sportmaster.esm.confirmation.service.utils.CodeUtils;
import ru.sportmaster.esm.mail.service.MailServiceImpl;
import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.notification.EmailTemplateResolver;
import ru.sportmaster.esm.user.dao.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class EmailConfirmationService implements ConfirmationService {

    private final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final EmailConfirmationRepository repository;
    private final EmailConfirmationProperties properties;
    private final EmailTemplateResolver templateResolver;

    public EmailConfirmationService(EmailConfirmationRepository repository, EmailConfirmationProperties properties,
                                    EmailTemplateResolver templateResolver) {
        this.repository = Objects.requireNonNull(repository);
        this.properties = properties;
        this.templateResolver = templateResolver;
    }

    @Override
    public Mono<Void> deactivateNotification(EmailConfirmation confirmation) {
        log.debug("start deactivating email confrimation");
        confirmation.setNeedToRemind(false);
        return repository.save(confirmation)
            .doOnSuccess(conf -> log.debug("email confirmation {} deactivated", conf))
            .then();
    }

    @Override
    public void invalidate(String code) {
        repository.deleteById(code).subscribe();
    }

    @Override
    public Mono<String> validate(String code) {
        return repository.findById(code)
            .map((confirmation) -> {
                invalidate(confirmation.getCode());
                return confirmation.getProfileId();
            });
    }

    @Override
    public Mono<Boolean> sendNotificationMail(Profile profile, EmailTemplates template) {
        switch (template) {
            case RECIEVE_PRMOTIONS:
                return Mono.just(templateResolver.resolve(template).send(profile, null));
            case COMFIRM_EMAIL:
                return generateEmailVerifyCode(profile.getId())
                        .map((code) -> templateResolver.resolve(EmailTemplates.COMFIRM_EMAIL).send(profile, code));
            case RESET_PASSWORD:
                return generateResetPasswordCode(profile.getId())
                        .map((code) -> templateResolver.resolve(EmailTemplates.RESET_PASSWORD).send(profile, code));
            default:
                throw new IllegalArgumentException("email template not specified");
        }
    }

    @Override
    public Flux<EmailConfirmation> listOfNeededToBeReminded() {
        return repository.findAllByNeedToRemind(true)
                // Use filter on the server side since spring data does not support ZoneDateTime comparisons
                .filter(this::requiresReminding);
    }

    @Override
    public Flux<EmailConfirmation> listOfExpired() {
        return repository.findAllByExpiredDateBefore(LocalDateTime.now());
    }

    private Mono<String> generateEmailVerifyCode(String profileId) {
        EmailConfirmation confirmation = new EmailConfirmation(
                ConfirmationType.EMAIL_VERIFY,
                CodeUtils.generateCodeString(properties.getCodeLength(), properties.getAlphabetType()),
                profileId,
                Duration.of(properties.getEmailVerificationRemindInterval(), ChronoUnit.MINUTES),
                Duration.of(properties.getEmailVerificationTimeToLive(), ChronoUnit.MINUTES));
        return repository.save(confirmation).map(EmailConfirmation::getCode)
            .doOnSuccess((conf) -> log.info("generated and saved email confirmation code {} for profile {}",
                    conf, profileId));
    }

    private Mono<String> generateResetPasswordCode(String profileId) {
        EmailConfirmation confirmation = new EmailConfirmation(
                ConfirmationType.PASSWORD_RESTORE,
                CodeUtils.generateCodeString(properties.getCodeLength(), properties.getAlphabetType()),
                profileId,
                Duration.ZERO,
                Duration.of(properties.getPasswordRestoreTimeToLive(), ChronoUnit.MINUTES));
        return repository.save(confirmation).map(EmailConfirmation::getCode)
            .doOnSuccess((conf) -> log.info("generated and saved password reset code {} for profile {}",
                    conf, profileId));
    }

    private boolean requiresReminding(EmailConfirmation confirmation) {
        return ZonedDateTime.now().compareTo(confirmation.getRemindDate()) >= 0;
    }

}
