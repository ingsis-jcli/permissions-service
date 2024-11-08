package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.exceptions.PermissionDeniedException;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

  @Mock private UserService userService;

  @Mock private UserRepository userRepository;

  @InjectMocks private PermissionService permissionService;

  private User user;
  private User friend;
  private Long snippetId;

  @BeforeEach
  void setUp() {
    user = new User("user1");
    friend = new User("friend1");
    snippetId = 1L;
  }

  @Test
  void testGrantOwnerPermissionWhenNoPermissionExists() {
    when(userService.getUser("user1")).thenReturn(user);
    String result = permissionService.grantOwnerPermission(snippetId, "user1");

    assertEquals("Permission granted", result);
    assertTrue(
        user.getSnippetPermissions().stream()
            .anyMatch(
                sp ->
                    sp.getSnippetId().equals(snippetId)
                        && sp.getPermissions().contains(PermissionType.OWNER)));
    verify(userRepository).save(user);
  }

  @Test
  void testGrantOwnerPermissionWhenPermissionAlreadyExists() {
    user.addSnippetPermission(snippetId, PermissionType.OWNER);
    when(userService.getUser("user1")).thenReturn(user);

    String result = permissionService.grantOwnerPermission(snippetId, "user1");

    assertEquals("Permission already granted", result);
    verify(userRepository, never()).save(user);
  }

  @Test
  void testHasPermissionWhenPermissionExists() {
    user.addSnippetPermission(snippetId, PermissionType.SHARED);
    when(userService.getUser("user1")).thenReturn(user);

    assertTrue(permissionService.hasPermission("user1", snippetId, PermissionType.SHARED));
  }

  @Test
  void testHasPermissionWhenPermissionDoesNotExist() {
    when(userService.getUser("user1")).thenReturn(user);

    assertFalse(permissionService.hasPermission("user1", snippetId, PermissionType.OWNER));
  }

  @Test
  void testShareWithUserWhenPermissionIsGranted() {
    user.addSnippetPermission(snippetId, PermissionType.OWNER);
    when(userService.getUser("user1")).thenReturn(user);
    when(userService.getUser("friend1")).thenReturn(friend);

    String result = permissionService.shareWithUser("user1", "friend1", snippetId);

    assertEquals("Snippet shared with user", result);
    assertTrue(
        friend.getSnippetPermissions().stream()
            .anyMatch(
                sp ->
                    sp.getSnippetId().equals(snippetId)
                        && sp.getPermissions().contains(PermissionType.SHARED)));
    verify(userRepository).save(friend);
  }

  @Test
  void testShareWithUserWhenAlreadyShared() {
    user.addSnippetPermission(snippetId, PermissionType.OWNER);
    friend.addSnippetPermission(snippetId, PermissionType.SHARED);
    when(userService.getUser("user1")).thenReturn(user);
    when(userService.getUser("friend1")).thenReturn(friend);

    String result = permissionService.shareWithUser("user1", "friend1", snippetId);

    assertEquals("Snippet already shared with user", result);
    verify(userRepository, never()).save(friend);
  }

  @Test
  void testShareWithUserWhenPermissionDenied() {
    when(userService.getUser("user1")).thenReturn(user);
    when(userService.getUser("friend1")).thenReturn(friend);

    assertThrows(
        PermissionDeniedException.class,
        () -> permissionService.shareWithUser("user1", "friend1", snippetId));
  }

  @Test
  void testGetSnippetsSharedWithUser() {
    user.addSnippetPermission(snippetId, PermissionType.SHARED);
    Long snippetId2 = 2L;
    user.addSnippetPermission(snippetId2, PermissionType.SHARED);

    when(userService.getUser("user1")).thenReturn(user);

    List<Long> sharedSnippets = permissionService.getSnippetsSharedWithUser("user1");

    assertEquals(2, sharedSnippets.size());
    assertTrue(sharedSnippets.contains(snippetId));
    assertTrue(sharedSnippets.contains(snippetId2));
  }
}
