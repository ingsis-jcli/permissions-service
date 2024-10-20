package com.ingsis.jcli.permissions.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Snippet;
import com.ingsis.jcli.permissions.models.User;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SnippetTest {

  @Test
  void testSnippetEqualsAndHashCode() {
    Snippet snippet1 = new Snippet();
    snippet1.setId(1L);
    snippet1.setContent("This is a test snippet");

    Snippet snippet2 = new Snippet();
    snippet2.setId(1L);
    snippet2.setContent("This is a test snippet");

    assertThat(snippet1).isEqualTo(snippet2);
    assertThat(snippet1.hashCode()).isEqualTo(snippet2.hashCode());
  }

  @Test
  void testSnippetToString() {
    Snippet snippet = new Snippet();
    snippet.setId(1L);
    snippet.setContent("This is a test snippet");

    String toStringResult = snippet.toString();
    assertThat(toStringResult).contains("id=1", "content=This is a test snippet");
  }

  @Test
  void testSnippetGettersAndSetters() {
    Snippet snippet = new Snippet();
    snippet.setId(10L);
    snippet.setContent("Test content");

    assertThat(snippet.getId()).isEqualTo(10L);
    assertThat(snippet.getContent()).isEqualTo("Test content");
  }

  @Test
  void testUserPermissionsMap() {
    Snippet snippet = new Snippet();
    User user = new User();
    user.setId(1L);
    user.setName("Test User");

    Map<User, PermissionType> userPermissions = new HashMap<>();
    userPermissions.put(user, PermissionType.READ);

    snippet.setUserPermissions(userPermissions);

    assertThat(snippet.getUserPermissions().get(user)).isEqualTo(PermissionType.READ);
    assertThat(snippet.getUserPermissions()).containsKey(user);
  }

  @Test
  void testAddPermissionToUser() {
    Snippet snippet = new Snippet();
    User user = new User();
    user.setId(2L);
    user.setName("User 2");

    snippet.getUserPermissions().put(user, PermissionType.WRITE);

    assertThat(snippet.getUserPermissions().get(user)).isEqualTo(PermissionType.WRITE);
    assertThat(snippet.getUserPermissions().size()).isEqualTo(1);
  }

  @Test
  void testRemovePermissionFromUser() {
    Snippet snippet = new Snippet();
    User user = new User();
    user.setId(3L);
    user.setName("User 3");

    snippet.getUserPermissions().put(user, PermissionType.EXECUTE);
    assertThat(snippet.getUserPermissions().get(user)).isEqualTo(PermissionType.EXECUTE);

    snippet.getUserPermissions().remove(user);
    assertThat(snippet.getUserPermissions()).doesNotContainKey(user);
  }
}
