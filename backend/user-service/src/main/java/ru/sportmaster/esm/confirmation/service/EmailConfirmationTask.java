package ru.sportmaster.esm.confirmation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.sportmaster.esm.confirmation.configuration.EmailConfirmationProperties;
import ru.sportmaster.esm.confirmation.domain.EmailConfirmation;
import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Task to sending mail of confirmation user registration
 *
 */
@Service
public class EmailConfirmationTask {

    private final Logger log = LoggerFactory.getLogger(EmailConfirmationTask.class);

    private final TaskScheduler taskScheduler;
    private final EmailConfirmationService confirmationService;
    private final UserService userService;
    private ScheduledFuture<?> scheduledFuture;

    public EmailConfirmationTask(TaskScheduler taskScheduler, EmailConfirmationService confirmationService,
                                 UserService userService) {
        this.taskScheduler = Objects.requireNonNull(taskScheduler);
        this.confirmationService = Objects.requireNonNull(confirmationService);
        this.userService = Objects.requireNonNull(userService);
    }

    @PostConstruct
    public void init() {
        log.debug("Init mail task scheduler");
        PeriodicTrigger trigger = new PeriodicTrigger(1, TimeUnit.HOURS);
      //  trigger.setInitialDelay(1); // allow to complete application starting
        this.scheduledFuture = taskScheduler.schedule(this::sendConfirmationMail, trigger);
        this.scheduledFuture = taskScheduler.schedule(this::removeExpiredConfirmations, trigger);
    }

    private void removeExpiredConfirmations() {
        log.debug("Start to remove expired confirmations by scheduler");
        Flux<EmailConfirmation> confirmations = confirmationService.listOfExpired();
        confirmations.doOnNext((confirmation) -> {
            confirmationService.invalidate(confirmation.getCode());
        })
        .doOnComplete(() -> log.debug("Finish to remove expired confirmations by scheduler"))
        .subscribe();
    }

    public void sendConfirmationMail() {
        log.debug("Start to send confirmation mails by scheduler");
        confirmationService.listOfNeededToBeReminded()
            .collectMap(EmailConfirmation::getProfileId)
            .map((confirmations) ->
                userService.findAllProfilesById(confirmations.keySet())
                    .doOnNext((profile) -> {
                        confirmations.forEach((profileId, confirmation) -> {
                            if (profile.getId().equals(profileId)) {
                                confirmationService.sendNotificationMail(profile, EmailTemplates.COMFIRM_EMAIL)
                                .then(confirmationService.deactivateNotification(confirmation))
                                .subscribe();
                            }});})
                    .subscribe())
            .doOnError((thrw) -> log.error("Error sending confirmation mails by scheduler:", thrw))
            .doOnSuccess((v) -> log.debug("Finish to send confirmation mails by scheduler"))
            .subscribe();
    }

    @PreDestroy
    public void destroy() {
        scheduledFuture.cancel(true);
    }

}
