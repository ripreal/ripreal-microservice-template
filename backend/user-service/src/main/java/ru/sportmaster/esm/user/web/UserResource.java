package ru.sportmaster.esm.user.web;

import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.loyalty.Customer;
import ru.sportmaster.esm.loyalty.UpdateSubscriptionsRequest;
import ru.sportmaster.esm.user.dao.Profile;
import ru.sportmaster.esm.user.dto.Subscription;
import ru.sportmaster.esm.user.dto.UpdateUserInfoRequest;
import ru.sportmaster.esm.user.service.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST API для работы с пользователями.
 */
@RestController
@RequestMapping("/api/v2/user")
public class UserResource {

    private final Validator validator;
    private final UserService userService;

    @Autowired
    public UserResource(Validator validator, UserService userService) {
        this.validator = Objects.requireNonNull(validator);
        this.userService = Objects.requireNonNull(userService);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> registerUser(@RequestBody RegistrationRequest request) {
        Set<ConstraintViolation<RegistrationRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.isEmpty()) {
            return userService.register(request)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .onErrorResume(ex -> {
                        RegistrationResult result = RegistrationResult.error(Collections.singletonList(ex.getMessage()));
                        return Mono.just(ResponseEntity.badRequest().body(result));
                    });
        } else {
            List<String> messages = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return Mono.just(ResponseEntity.badRequest().body(RegistrationResult.error(messages)));
        }
    }

    @GetMapping(value = "/activate")
    public Mono<ResponseEntity<String>> activateAccount(@RequestParam String code) {
        return userService.activateRegistration(code)
            .map((profile) -> ResponseEntity.ok("user was activated"));
    }

    @PostMapping("/auth")
    public Mono<ResponseEntity<AuthInfo>> auth(@RequestBody AuthRequest request) {
        return userService.auth(request.getEmail(), request.getPassword())
                .map(ResponseEntity::ok);

    }

    @PostMapping(value = "/restore/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> restorePasswordInit(@RequestBody RestorePasswordRequest request) {
        return userService.requestResetPasswordByEmail(request.getEmail())
                .map((profile) -> ResponseEntity.ok("email was sent"));
    }

    @GetMapping(value = "/restore/finish")
    public Mono<ResponseEntity<String>> restorePasswordFinish(@RequestParam String code) {
        return userService.finishResetPasswordByEmail(code)
                .map((profile) -> ResponseEntity.ok("password was reset"));
    }

    @GetMapping(value = "/account/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Profile>> getFullUserInfo(@PathVariable String customerId) {
        return userService.findUser(customerId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    //TODO: for debugging
    @GetMapping(value = "/delete/{email}", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> deleteByEmail(@PathVariable String email) {
        return ((UserServiceImpl) userService).deleteByEmail(Strings.toLowerCase(email))
                .map((profile) -> ResponseEntity.ok("user was deleted"))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body("user with this email address not " +
                        "registered")));
    }
}
