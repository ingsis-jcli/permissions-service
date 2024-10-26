package com.ingsis.jcli.permissions.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.ingsis.jcli.permissions.common.PermissionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class SnippetPermissionsTest {

  @Test
  void testSnippetPermissionsIdConstructorAndEquals() {
    Long snippetId1 = 1L;
    String userId1 = "user1";

    SnippetPermissionsId id1 = new SnippetPermissionsId(snippetId1, userId1);
    SnippetPermissionsId id2 = new SnippetPermissionsId(snippetId1, userId1);

    assertEquals(id1, id2, "SnippetPermissionsId objects with same values should be equal");

    assertEquals(id1.hashCode(), id2.hashCode(), "Hash codes should be equal for equal objects");

    assertEquals(snippetId1, id1.getSnippetId(), "Snippet ID should match");
    assertEquals(userId1, id1.getUserId(), "User ID should match");

    SnippetPermissionsId id3 = new SnippetPermissionsId(2L, "user2");
    assertNotEquals(
        id1, id3, "SnippetPermissionsId objects with different values should not be equal");
  }

  @Test
  void testSnippetPermissionsConstructorAndGetters() {
    Long snippetId = 1L;
    String userId = "user1";
    List<PermissionType> permissions = List.of(PermissionType.READ, PermissionType.WRITE);

    SnippetPermissionsId id = new SnippetPermissionsId(snippetId, userId);
    SnippetPermissions snippetPermissions = new SnippetPermissions(userId, snippetId, permissions);

    assertEquals(id, snippetPermissions.getId(), "Embedded ID should be correctly set");

    assertEquals(
        permissions, snippetPermissions.getPermissions(), "Permissions should be correctly set");
  }

  @Test
  void testSnippetPermissionsEqualsAndHashCode() {
    Long snippetId1 = 1L;
    String userId1 = "user1";
    List<PermissionType> permissions1 = List.of(PermissionType.READ, PermissionType.WRITE);

    Long snippetId2 = 2L;
    String userId2 = "user2";
    List<PermissionType> permissions2 = List.of(PermissionType.READ);

    SnippetPermissions snippetPermissions1 =
        new SnippetPermissions(userId1, snippetId1, permissions1);
    SnippetPermissions snippetPermissions2 =
        new SnippetPermissions(userId1, snippetId1, permissions1);
    SnippetPermissions snippetPermissions3 =
        new SnippetPermissions(userId2, snippetId2, permissions2);

    assertEquals(
        snippetPermissions1,
        snippetPermissions2,
        "SnippetPermissions objects with same values should be equal");

    assertEquals(
        snippetPermissions1.hashCode(),
        snippetPermissions2.hashCode(),
        "Hash codes should be equal for equal objects");

    assertNotEquals(
        snippetPermissions1,
        snippetPermissions3,
        "SnippetPermissions objects with different values should not be equal");
  }
}
