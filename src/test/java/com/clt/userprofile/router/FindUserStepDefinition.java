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

    @Given("A user having id {string}")
    public void a_user_having_id(String string) {
        var driver = new UserApplicationDriver(client);
        var user = driver.createUser(Map.of());
        this.idMapping.put(string, user.getId());
    }

    @When("I lookfor the user")
    public void i_lookfor_the_user() {
        invocationResult = client.get().uri("/profile")
                .exchange()
                .expectBody(String.class)
                .returnResult();

    }

    @Then("I should receive receive {string}")
    public void i_should_receive_receive(String string) {
        HttpStatus status = HttpStatus.valueOf(string);
        Assertions.assertEquals(status, invocationResult.getStatus());
    }

}
