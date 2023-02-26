package com.clt.userprofile.error;

public class UserProfileNotFoundException extends RuntimeException {

  public UserProfileNotFoundException(String message) {
    super(message);
  }
}
