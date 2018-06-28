package ru.sportmaster.esm.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.confirmation.service.EmailConfirmationService;
import ru.sportmaster.esm.loyalty.Customer;
import ru.sportmaster.esm.mail.templates.EmailTemplates;
import ru.sportmaster.esm.mail.templates.ThymeleafTemplate;
import ru.sportmaster.esm.user.dao.Profile;
import ru.sportmaster.esm.user.dao.ProfileRepository;
import ru.sportmaster.esm.user.dto.Subscription;
import ru.sportmaster.esm.user.dto.Subscriptions;
import ru.sportmaster.esm.user.dto.UpdateUserInfoRequest;
import ru.sportmaster.esm.user.service.exceptions.AuthException;
import ru.sportmaster.esm.user.service.exceptions.PasswordResetException;
import ru.sportmaster.esm.user.service.exceptions.UserProfileNotFoundException;
import ru.sportmaster.esm.user.service.exceptions.WrongActivationCodeException;
import ru.sportmaster.esm.user.service.utils.CollectionsUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ProfileRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;

    @Autowired
    public UserServiceImpl(ProfileRepository repository,
                           EmailConfirmationService emailConfirmationService) {
        this.repository = Objects.requireNonNull(repository);
        this.emailConfirmationService = emailConfirmationService;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    // REGISTER USER

    @Override
    public Mono<RegistrationResult> register(RegistrationRequest request) throws RegistrationException {
        // ищем пользователя в программе лояльности
        return repository.findByPhone(request.getPhone())
                .flatMap(profile -> repeatedRegistration(profile.getId(), request))
                .switchIfEmpty(newRegistration(request));
    }

    /**
     * Подверждает регистрацию пользователя по переданному токену. В случае неудачи возвращается пустое {@link Mono}.
     * @param code токен, сгенерированный при регистрации пользователей и переданный в письме подтверждения
     * @return {@link Mono} c {@link Profile} пользователя или пустой {@link Mono}, если активация неверная
     */
    @Override
    public Mono<Profile> activateRegistration(String code) {
        return emailConfirmationService.validate(code)
                .flatMap((profileId) -> {
                    log.debug("Result of email confirmation for user {} is true", profileId);
                    return repository.findById(profileId)
                            .flatMap((profile) -> {
                                profile.setEmailConfirmationRequired(false);
                                return repository.findByPhone(profile.getPhone())
                                        .then(repository.save(profile));
                            });
                })
                .switchIfEmpty(Mono.error(new WrongActivationCodeException("error.wrong_activation_code")));
    }

    @Override
    public Mono<AuthInfo> auth(String email, String password) throws AuthException {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(new AuthException("error.user_not_found")))
                .filter(p -> passwordEncoder.matches(password, p.getPassword()))
                .switchIfEmpty(Mono.error(new AuthException("error.password_invalid")))
                .map(profile -> new AuthInfo(profile.getId(), profile.getName(), profile.getBonuses()));
    }

    @Override
    public Flux<Profile> findAllProfilesById(Iterable<String> ids) {
        return repository.findAllById(ids);
    }

    private Mono<RegistrationResult> newRegistration(RegistrationRequest request) {

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setBonuses(300);
        profile.setPhone(request.getPhone());
        profile.setEmailConfirmationRequired(true);
        profile.setEmail(request.getEmail());
        profile.setPassword(passwordEncoder.encode(request.getPassword()));
        profile.setSubscribed(request.getSubscription()); // subscriptions themselves will be added after

        return repository.save(profile)
            .doOnError(ex -> {
                Mono.error(new RegistrationException(ex.getMessage()));
            })
            .flatMap(savedProfile -> {
                RegistrationResult result = new RegistrationResult();
                result.setCustomerId(savedProfile.getId());
                result.setEmailConfirmationRequired(savedProfile.getEmailConfirmationRequired());
                result.setNewSiteCustomer(true);
                return emailConfirmationService.sendNotificationMail(savedProfile, EmailTemplates.COMFIRM_EMAIL)
                        .thenReturn(result);
            });
    }

    private Mono<RegistrationResult> repeatedRegistration(String loyaltyId, RegistrationRequest request) {
        // обновляем данные в программе лояльности
        return repository.findById(loyaltyId)
            .switchIfEmpty(Mono.just(new Profile()))
            .flatMap(profile -> {
                Profile updatedProfile = new Profile();
                updatedProfile.setId(loyaltyId);
                updatedProfile.setName(request.getName());
                updatedProfile.setBonuses(profile.getBonuses());
                updatedProfile.setEmail(request.getEmail());
                updatedProfile.setPhone(request.getPhone());
                updatedProfile.setCreatedOn(profile.getCreatedOn());
                updatedProfile.setPassword(passwordEncoder.encode(request.getPassword()));
                updatedProfile.setSubscriptions(profile.getSubscriptions());
                updatedProfile.setSubscribed(request.getSubscription());
                if (updatedProfile.getSubscribed())
                    updatedProfile.setSubscriptions(CollectionsUtils.mergeSets(profile.getSubscriptions(),
                            Subscriptions.registrationSubscriptions(true)));
                else
                    updatedProfile.setSubscriptions(Subscriptions.registrationSubscriptions(false));
                if (!Objects.equals(profile.getEmail(), request.getEmail())) {
                    updatedProfile.setEmailConfirmationRequired(true);
                    return repository.save(updatedProfile).map((savedProfile) -> {
                        emailConfirmationService.sendNotificationMail(savedProfile, EmailTemplates.COMFIRM_EMAIL)
                                .subscribe();
                        return savedProfile;
                    });
                } else {
                    return repository.save(updatedProfile);
                }

            })
            .onErrorResume(ex -> {
                String errorCode = ex.getMessage();
                if (ex.getMessage().contains("email"))
                    errorCode = "error.email_already_exists";
                else if((ex.getMessage().contains("phone")))
                    errorCode = "error.phone_already_exists";
                return Mono.error(new RegistrationException(errorCode));
            })
            .map(updatedProfile -> {
                RegistrationResult result = new RegistrationResult();
                result.setCustomerId(updatedProfile.getId());
                result.setEmailConfirmationRequired(updatedProfile.getEmailConfirmationRequired());
                result.setNewSiteCustomer(false);
                return result;
            });
    }


    // RESET PASSWORD

    @Override
    public Mono<Boolean> requestResetPasswordByEmail(String email) {
        return repository.findByEmail(email)
            .flatMap((profile) -> emailConfirmationService.sendNotificationMail(profile, EmailTemplates.RESET_PASSWORD))
            .switchIfEmpty(Mono.error(new PasswordResetException("error.email_address_not_registered")));

    }

    @Override
    public Mono<Profile> finishResetPasswordByEmail(String code) {
        return emailConfirmationService
                .validate(code)
                .flatMap(profileId -> repository.findById(profileId)
                        .flatMap(profile -> {
                            profile.setNeedChangePassword(true);
                            return repository.save(profile);
                        })
                )
                .switchIfEmpty(Mono.error(new PasswordResetException("error.reset_code_is_not_valid")));
    }

    //TODO: for debugging
    public Mono<Profile> deleteByEmail(String email) {
        return repository.findByEmail(email)
                .map((profile) -> {
                    repository.delete(profile).subscribe();
                    return profile;
                });
    }

    @Override
    public Mono<Profile> findUser(String profileId) {
        return repository.findById(profileId);
    }
}
