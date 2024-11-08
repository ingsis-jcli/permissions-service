package com.ingsis.jcli.permissions.common.exceptions;

public class UserDoesNotExist extends RuntimeException {
  public UserDoesNotExist(String userId) {
    super("User with userId " + userId + " does not exist");
  }
}
