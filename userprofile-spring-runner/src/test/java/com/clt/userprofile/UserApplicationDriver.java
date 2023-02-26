package com.clt.userprofile;

import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class UserApplicationDriver {
  public enum UserParametersEnum {
    ID,
    USERNAME,
    NAME,
    SURNAME;
  }

  final WebTestClient client;

  public UserApplicationDriver(WebTestClient client) {
    this.client = client;
  }

  public UserProfileEntity createUser(Map<UserParametersEnum, String> parametersMap) {
    CreateUserRequest request = this.buildRandomUserRequest(parametersMap);
    return client
        .post()
        .uri("/profile")
        .body(Mono.just(request), CreateUserRequest.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(UserProfileEntity.class)
        .returnResult()
        .getResponseBody();
  }

  public void deleteUser(Map<UserParametersEnum, String> parametersMap) {
    client
        .delete()
        .uri("/profile/" + parametersMap.get(UserParametersEnum.ID))
        .exchange()
        .expectStatus()
        .isOk();
  }

  public Optional<UserProfileEntity> findUser(Map<UserParametersEnum, String> parametersMap) {
    var userId = parametersMap.get(UserParametersEnum.ID);
    var invocationResult =
        client
            .get()
            .uri("/profile/" + userId)
            .exchange()
            .expectBody(UserProfileEntity.class)
            .returnResult();
    return invocationResult.getStatus().is2xxSuccessful()
        ? Optional.of(invocationResult.getResponseBody())
        : Optional.empty();
  }

  public CreateUserRequest buildRandomUserRequest(Map<UserParametersEnum, String> parametersMap) {
    Random random = new Random();
    String username =
        parametersMap.getOrDefault(UserParametersEnum.USERNAME, UUID.randomUUID().toString());
    String name = parametersMap.getOrDefault(UserParametersEnum.NAME, "name_" + random.nextInt());
    String surname =
        parametersMap.getOrDefault(UserParametersEnum.SURNAME, "surname_" + random.nextInt());
    CreateUserRequest request = new CreateUserRequest();
    request.setUserName(username);
    request.setName(name);
    request.setSurname(surname);
    return request;
  }

  public CreateUserRequest buildRandomUserRequest() {
    return this.buildRandomUserRequest(Map.of());
  }
}
