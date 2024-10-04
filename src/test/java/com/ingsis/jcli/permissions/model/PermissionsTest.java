package com.ingsis.jcli.permissions.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Permission;
import org.junit.jupiter.api.Test;

public class PermissionsTest {

  @Test
  void testEqualsAndHashCode() {
    Permission permission1 = new Permission();
    permission1.setId(1L);
    permission1.setUserId(1L);
    permission1.setSnippetId(1L);
    permission1.setType(PermissionType.READ);

    Permission permission2 = new Permission();
    permission2.setId(1L);
    permission2.setUserId(1L);
    permission2.setSnippetId(1L);
    permission2.setType(PermissionType.READ);

    assertThat(permission1).isEqualTo(permission2);
    assertThat(permission1.hashCode()).isEqualTo(permission2.hashCode());
  }

  @Test
  void testToString() {
    Permission permission = new Permission();
    permission.setId(1L);
    permission.setUserId(2L);
    permission.setSnippetId(3L);
    permission.setType(PermissionType.WRITE);

    String toStringResult = permission.toString();
    assertThat(toStringResult).contains("id=1", "userId=2", "snippetId=3", "type=WRITE");
  }

  @Test
  void testGettersAndSetters() {
    Permission permission = new Permission();
    permission.setId(10L);
    permission.setUserId(20L);
    permission.setSnippetId(30L);
    permission.setType(PermissionType.EXECUTE);

    assertThat(permission.getId()).isEqualTo(10L);
    assertThat(permission.getUserId()).isEqualTo(20L);
    assertThat(permission.getSnippetId()).isEqualTo(30L);
    assertThat(permission.getType()).isEqualTo(PermissionType.EXECUTE);
  }
}
