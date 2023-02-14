package com.clt.userprofile;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.test.web.reactive.server.WebTestClient;

import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;

import reactor.core.publisher.Mono;

public class UserApplicationDriver {
    public enum UserParametersEnum {
        USERNAME, NAME, SURNAME;
    }

    final WebTestClient client;

    public UserApplicationDriver(WebTestClient client) {
        this.client = client;
    }

    public UserProfileEntity createUser(Map<UserParametersEnum, String> parametersMap) {
        Random random = new Random();
        String username = parametersMap.getOrDefault(UserParametersEnum.USERNAME, UUID.randomUUID().toString());
        String name = parametersMap.getOrDefault(UserParametersEnum.NAME, "name_" + random.nextInt());
        String surname = parametersMap.getOrDefault(UserParametersEnum.SURNAME, "surname_" + random.nextInt());
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(username);
        request.setName(name);
        request.setSurname(surname);
        return client.post().uri("/profile")
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserProfileEntity.class)
                .returnResult().getResponseBody();
    }
}
