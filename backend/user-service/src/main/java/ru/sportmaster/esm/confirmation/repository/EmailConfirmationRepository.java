package ru.sportmaster.esm.confirmation.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.confirmation.domain.EmailConfirmation;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Repository
public interface EmailConfirmationRepository extends ReactiveMongoRepository<EmailConfirmation, String> {

    Flux<EmailConfirmation> findAllByExpiredDateBefore(LocalDateTime expiredDate);

    Flux<EmailConfirmation> findAllByNeedToRemind(boolean needToRemind);

    Mono<EmailConfirmation> findByProfileId(String profileId);

}
