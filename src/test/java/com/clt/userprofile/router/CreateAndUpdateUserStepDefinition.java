package com.clt.userprofile.router;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    UserProfileEntity user;
    EntityExchangeResult result;

    @Given("A new user that wants to register")
    public void a_new_user_having_username() {
        user = UserProfileEntity.builder()
                .userName(UUID.randomUUID().toString())
                .build();
    }

    @Given("The user has not a unique username")
    public void the_user_has_not_a_unique_username() {
        // Doing so we ensure that will already exist a user with the given username
        var driver = new UserApplicationDriver(client);
        driver.createUser(Map.of(UserApplicationDriver.UserParametersEnum.USERNAME, user.getUserName()));
    }

    @Given("The user has a unique username")
    public void the_user_has_a_unique_username() {
        // No operation is required as the username is generated randomly
    }

    @When("The user tries to register")
    public void the_new_tries_to_register() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUserName());
        var result = client.post().uri("/profile")
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectBody(UserProfileEntity.class)
                .returnResult();
        this.result = result;
    }

    @Then("the outcome of the registration is {string}")
    public void the_outcome_of_the_registration_of_is(String outcome) {
        if ("OK".equals(outcome))
            Assertions.assertTrue(this.result.getStatus().is2xxSuccessful());
        else
            Assertions.assertTrue(this.result.getStatus().isError());
    }

}
