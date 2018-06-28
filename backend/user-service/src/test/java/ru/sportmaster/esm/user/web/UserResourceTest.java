package ru.sportmaster.esm.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import ru.sportmaster.esm.SpringTestConfig;
import ru.sportmaster.esm.confirmation.repository.EmailConfirmationRepository;
import ru.sportmaster.esm.loyalty.UpdateSubscriptionsRequest;
import ru.sportmaster.esm.user.dao.Profile;
import ru.sportmaster.esm.user.dao.ProfileRepository;
import ru.sportmaster.esm.user.dto.Subscriptions;
import ru.sportmaster.esm.user.service.RegistrationRequest;
import ru.sportmaster.esm.user.service.RegistrationResult;

public class UserResourceTest extends SpringTestConfig {

    private final String userServiceURL = "api/v2/user/";
    private final String userEmail = "test@test.net";
    private final String userPwd = "123456";
    @Autowired
    private ProfileRepository profileRepo;

    @Test
    public void testRegisterActivateAuthSubscribeUser() {

        // Try to register user

        RegistrationRequest regRequest = new RegistrationRequest("test_name", "test_city",
                userPwd, "+79192441111", userEmail, "1234", true);

        final ClientResponse regResp = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(userServiceURL + "/register").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(regRequest).exchange().block();

        Assertions.assertTrue(regResp.statusCode().is2xxSuccessful(), regResp.bodyToMono(String.class).block());

        // Try to activate user. We cant't get confirmation code so we check bad request

        final ClientResponse activateResp = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(userServiceURL + "/activate")
                        .queryParam("code", "not existent code").build())
                .accept(MediaType.ALL)
                .exchange().block();

        Assertions.assertTrue(activateResp.statusCode() == HttpStatus.BAD_REQUEST, activateResp.bodyToMono(String.class).block());

        // Try to auth user. We don't whether user has been activated or not so we check bad request

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(userEmail);
        authRequest.setPassword(userPwd + "wrong_psw");

        final ClientResponse authResp = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(userServiceURL + "/auth").build())
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(authRequest).exchange().block();

        Assertions.assertTrue(authResp.statusCode() == HttpStatus.BAD_REQUEST, authResp.bodyToMono(String.class).block());

    }

    @Test
    public void testRestorePasswordInitFinish() {

        // Try to start reset user password. We don't know if the email is really registered yet so we check bad request

        RestorePasswordRequest restoreRequest = new RestorePasswordRequest();
        restoreRequest.setEmail(userEmail + "not exists");

        final ClientResponse restInitResp = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(userServiceURL + "/restore/init").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(restoreRequest).exchange().block();

        Assertions.assertTrue(restInitResp.statusCode() == HttpStatus.BAD_REQUEST, restInitResp.bodyToMono(String.class).block());

        // Try to finish reset user password. We don't know reset code so we check bad request

        final ClientResponse finishResp = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(userServiceURL + "/restore/finish")
                        .queryParam("code", "not exists").build())
                .accept(MediaType.ALL)
                .exchange().block();

        Assertions.assertTrue(finishResp.statusCode() == HttpStatus.BAD_REQUEST, finishResp.bodyToMono(String.class).block());
    }
}
