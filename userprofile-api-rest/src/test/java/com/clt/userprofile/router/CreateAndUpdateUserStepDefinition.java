package com.clt.userprofile.router;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.clt.userprofile.UserApplicationDriver;
import com.clt.userprofile.UserApplicationDriver.UserParametersEnum;
import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import reactor.core.publisher.Mono;

public class CreateAndUpdateUserStepDefinition {

    @Autowired
    private WebTestClient client;
    UserProfileEntity user;
    EntityExchangeResult<UserProfileEntity> result;

    @Given("An already existing user")
    public void an_already_existing_user() {
        var driver = new UserApplicationDriver(client);
        user = driver.createUser(Map.of());
    }

    @Given("A new user that wants to register")
    public void a_new_user_that_wants_to_register() {
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
    public void the_user_tries_to_register() {
        var driver = new UserApplicationDriver(client);
        CreateUserRequest request = driver
                .buildRandomUserRequest(Map.of(UserApplicationDriver.UserParametersEnum.USERNAME, user.getUserName()));
        var result = client.post().uri("/profile")
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectBody(UserProfileEntity.class)
                .returnResult();
        this.result = result;
    }

    @When("The user tries to change his data")
    public void the_user_tries_to_change_his_data() {
        var driver = new UserApplicationDriver(client);
        CreateUserRequest request = driver.buildRandomUserRequest();
        user = UserProfileEntity.builder()
                .id(user.getId())
                .userName(request.getUserName())
                .name(request.getName())
                .surname(request.getSurname())
                .description(request.getDescription())
                .build();
        result = client.patch().uri("/profile/" + user.getId())
                .body(Mono.just(request), CreateUserRequest.class)
                .exchange()
                .expectBody(UserProfileEntity.class)
                .returnResult();
    }

    
    @When("the user tries to cancel himself")
    public void the_user_tries_to_cancel_himself() {
        result = client.delete().uri("/profile/" + user.getId())
                .exchange()
                .expectBody(UserProfileEntity.class)
                .returnResult();
    }

    @Then("the outcome of the operation is {string}")
    public void the_outcome_of_the_operation_of_is(String outcome) {
        if ("OK".equals(outcome))
            Assertions.assertTrue(this.result.getStatus().is2xxSuccessful());
        else
            Assertions.assertTrue(this.result.getStatus().isError());
    }

    @Then("The modified data have been stored")
    public void the_modified_data_have_been_stored() {
        Assertions.assertEquals(user, result.getResponseBody());
    }

    @Then("the user has been canceled")
    public void the_user_has_been_canceled() {
        var driver = new UserApplicationDriver(client);
        var optionalUser = driver.findUser(Map.of(UserParametersEnum.ID, String.valueOf(user.getId())));
        Assertions.assertTrue(optionalUser.isEmpty(), "The user is still present");
    }   

}
