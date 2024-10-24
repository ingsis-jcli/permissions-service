package com.ingsis.jcli.permissions.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Snippet;
import com.ingsis.jcli.permissions.models.User;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  void testUserEqualsAndHashCode() {
    User user1 = new User();
    user1.setId(1L);
    user1.setName("Test User");

    User user2 = new User();
    user2.setId(1L);
    user2.setName("Test User");

    assertThat(user1).isEqualTo(user2);
    assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
  }

  @Test
  void testUserToString() {
    User user = new User();
    user.setId(1L);
    user.setName("Test User");

    String toStringResult = user.toString();
    assertThat(toStringResult).contains("id=1", "name=Test User");
  }

  @Test
  void testUserGettersAndSetters() {
    User user = new User();
    user.setId(10L);
    user.setName("User 10");

    assertThat(user.getId()).isEqualTo(10L);
    assertThat(user.getName()).isEqualTo("User 10");
  }

  @Test
  void testSnippetPermissionsMap() {
    User user = new User();
    Snippet snippet = new Snippet();
    snippet.setId(1L);
    snippet.setContent("Test Snippet");

    Map<Snippet, PermissionType> snippetPermissions = new HashMap<>();
    snippetPermissions.put(snippet, PermissionType.READ);

    user.setSnippetPermissions(snippetPermissions);

    assertThat(user.getSnippetPermissions().get(snippet)).isEqualTo(PermissionType.READ);
    assertThat(user.getSnippetPermissions()).containsKey(snippet);
  }

  @Test
  void testAddPermissionToSnippet() {
    User user = new User();
    Snippet snippet = new Snippet();
    snippet.setId(2L);
    snippet.setContent("Snippet 2");

    user.getSnippetPermissions().put(snippet, PermissionType.WRITE);

    assertThat(user.getSnippetPermissions().get(snippet)).isEqualTo(PermissionType.WRITE);
    assertThat(user.getSnippetPermissions().size()).isEqualTo(1);
  }

  @Test
  void testRemovePermissionFromSnippet() {
    User user = new User();
    Snippet snippet = new Snippet();
    snippet.setId(3L);
    snippet.setContent("Snippet 3");

    user.getSnippetPermissions().put(snippet, PermissionType.EXECUTE);
    assertThat(user.getSnippetPermissions().get(snippet)).isEqualTo(PermissionType.EXECUTE);

    user.getSnippetPermissions().remove(snippet);
    assertThat(user.getSnippetPermissions()).doesNotContainKey(snippet);
  }
}
