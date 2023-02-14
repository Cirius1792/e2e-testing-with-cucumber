package com.clt.userprofile.router;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.clt.userprofile.UserApplicationDriver;
import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class CreateAndUpdateUserStepDefinition {

    @Autowired
    private WebTestClient client;
    private Map<String, UserProfileEntity> users = new HashMap<>();
    private Map<String, EntityExchangeResult> results = new HashMap<>();

    @Given("a new user having username {string}")
    public void a_new_user_having_username(String username) {
        var user = UserProfileEntity.builder()
                .userName(username)
                .build();
        this.users.put(username, user);
    }

    @Given("exists a user having username {string}")
    public void exists_a_user_having_username(String username) {
        var driver = new UserApplicationDriver(client);
        driver.createUser(Map.of(UserApplicationDriver.UserParametersEnum.USERNAME, username));
    }

    @When("the user having username {string} tries to register")
    public void the_new_tries_to_register(String username) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(username);
        var result = client.post().uri("/profile")
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectBody(UserProfileEntity.class)
                .returnResult();
        this.results.put(username, result);
    }

    @Then("the outcome of the registration of {string} is {string}")
    public void the_outcome_of_the_registration_of_is(String username, String outcome) {
        if ("OK".equals(outcome))
            Assertions.assertTrue(this.results.get(username).getStatus().is2xxSuccessful());
        else
            Assertions.assertTrue(this.results.get(username).getStatus().isError());
    }

}
