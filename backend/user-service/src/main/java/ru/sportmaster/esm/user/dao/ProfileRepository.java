package ru.sportmaster.esm.user.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {

    /**
     * Возвращает пользователя по email.
     *
     * @param email email пользователя
     * @return пользователь
     */
    Mono<Profile> findByEmail(String email);

    Mono<Profile> findByPhone(String phoneNumber);

    Mono<Profile> findByEmailAndEmailConfirmationRequired(String email, boolean confirmationRequired);

}
