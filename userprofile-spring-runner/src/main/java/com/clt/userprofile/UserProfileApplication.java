package com.clt.userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class UserProfileApplication {

  public static void main(String[] args) {
    Hooks.onOperatorDebug();
    SpringApplication.run(UserProfileApplication.class, args);
  }
}
