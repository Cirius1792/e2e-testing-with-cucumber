package com.clt.userprofile.router;

import com.clt.userprofile.UserApplicationDriver;
import com.clt.userprofile.UserApplicationDriver.UserParametersEnum;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class FindUserStepDefinition {

  @Autowired private WebTestClient client;

  EntityExchangeResult<String> invocationResult;
  String userId;

  @Given("An existing user")
  public void an_existing_user() {
    var driver = new UserApplicationDriver(client);
    var user = driver.createUser(Map.of());
    userId = String.valueOf(user.getId());
  }

  @Given("A not existing user")
  public void a_not_existing_user() {
    userId = "999";
    new UserApplicationDriver(client).deleteUser(Map.of(UserParametersEnum.ID, userId));
  }

  @When("I look for the user by id")
  public void i_look_for_the_user_by_id() {
    invocationResult =
        client.get().uri("/profile/" + userId).exchange().expectBody(String.class).returnResult();
  }

  @Then("I should receive {int}")
  public void i_should_receive_receive(int status) {
    Assertions.assertEquals(status, invocationResult.getStatus().value());
  }
}
