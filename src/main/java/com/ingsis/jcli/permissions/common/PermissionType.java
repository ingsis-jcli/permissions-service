package com.ingsis.jcli.permissions.common;

public enum PermissionType {
  OWNER("owner"),
  SHARED("shared");

  public final String name;

  PermissionType(String name) {
    this.name = name;
  }
}
