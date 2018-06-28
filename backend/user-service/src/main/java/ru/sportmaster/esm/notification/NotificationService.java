package ru.sportmaster.esm.notification;

import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.Profile;

public interface NotificationService {

    Mono<Void> sendSms(String phoneNumber, SmsTemplate template, String... args);

}
