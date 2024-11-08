package com.ingsis.jcli.permissions.common.exceptions;

import lombok.Getter;

@Getter
public class PermissionDeniedException extends RuntimeException {

  private final String userId;
  private final DeniedAction action;

  public PermissionDeniedException(String userId, DeniedAction action) {
    super("User is not allowed to perform this action: " + action);
    this.userId = userId;
    this.action = action;
  }
}
