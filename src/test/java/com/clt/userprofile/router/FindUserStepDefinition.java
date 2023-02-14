package com.clt.userprofile.router;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.clt.userprofile.UserApplicationDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FindUserStepDefinition {

    @Autowired
    private WebTestClient client;

    private Map<String, Long> idMapping = new HashMap<>();
    EntityExchangeResult invocationResult;
    private String userId;

    @Given("An existing user having id {string}")
    public void an_existing_user_having_id(String userId) {
        var driver = new UserApplicationDriver(client);
        var user = driver.createUser(Map.of());
        this.idMapping.put(userId, user.getId());
        this.userId = String.valueOf(user.getId());
    }

    @Given("A not existing user having id {string}")
    public void a_not_existing_user_having_id(String userId) {
        this.idMapping.put(userId, Long.valueOf(userId));
        //TODO: Cancellazione dell'utente se già esiste
    }

    @When("I look for the user {string}")
    public void i_look_for_the_user(String userId) {
        invocationResult = client.get().uri("/profile/"+this.idMapping.get(userId))
                .exchange()
                .expectBody(String.class)
                .returnResult();

    }

    @Then("I should receive receive {int}")
    public void i_should_receive_receive(int status) {
        Assertions.assertEquals(status, invocationResult.getStatus().value());
    }

}
