package ru.sportmaster.esm.user.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.loyalty.Customer;
import ru.sportmaster.esm.user.dao.Profile;
import ru.sportmaster.esm.user.dto.Subscription;
import ru.sportmaster.esm.user.dto.UpdateUserInfoRequest;
import ru.sportmaster.esm.user.service.exceptions.AuthException;

import java.util.Set;

public interface UserService {

    Mono<RegistrationResult> register(RegistrationRequest request) throws RegistrationException;

    Mono<AuthInfo> auth(String email, String password) throws AuthException;

    Mono<Boolean> requestResetPasswordByEmail(String email);

    Mono<Profile> finishResetPasswordByEmail(String code);

    Mono<Profile> activateRegistration(String code);

    Flux<Profile> findAllProfilesById(Iterable<String> ids);

    Mono<Profile> findUser(String profileId);

}
