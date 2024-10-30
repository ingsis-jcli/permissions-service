package com.ingsis.jcli.permissions.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.exceptions.DeniedAction;
import com.ingsis.jcli.permissions.common.exceptions.PermissionDeniedException;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.SnippetPermissionsRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PermissionServiceTest {

  @Autowired private PermissionService permissionService;

  @MockBean private SnippetPermissionsRepository snippetPermissionsRepository;
  @MockBean private UserService userService;
  @MockBean private JwtDecoder jwtDecoder;

  private static final String userId = "userId";
  private static final String email = "user@email.com";
  private static final Long snippetId = 1L;

  @Test
  public void testHasPermission() {
    SnippetPermissions snippetPermissions =
        new SnippetPermissions(userId, snippetId, List.of(PermissionType.SHARED));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.of(snippetPermissions));
    boolean hasPermission =
        permissionService.hasPermission(userId, snippetId, PermissionType.SHARED);
    assertThat(hasPermission).isTrue();
  }

  @Test
  public void testNoPermission() {
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.empty());
    boolean hasPermission =
        permissionService.hasPermission(userId, snippetId, PermissionType.SHARED);
    assertThat(hasPermission).isFalse();
  }

  @Test
  public void testNoProperPermission() {
    SnippetPermissions snippetPermissions =
        new SnippetPermissions(userId, snippetId, List.of(PermissionType.SHARED));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.of(snippetPermissions));
    boolean hasPermission =
        permissionService.hasPermission(userId, snippetId, PermissionType.OWNER);
    assertThat(hasPermission).isFalse();
  }

  @Test
  public void shareWithUser() {
    String friendId = "friendId";
    String friendEmail = "friend@example.com";

    when(userService.getUserById(userId)).thenReturn(Optional.of(new User(userId, email)));
    when(userService.getUserByEmail(friendEmail))
        .thenReturn(Optional.of(new User(friendId, friendEmail)));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(
            Optional.of(new SnippetPermissions(userId, snippetId, List.of(PermissionType.OWNER))));

    permissionService.shareWithUser(userId, friendEmail, snippetId);

    verify(snippetPermissionsRepository, times(1))
        .save(new SnippetPermissions(friendId, snippetId, List.of(PermissionType.SHARED)));
  }

  @Test
  public void shareWithUserDenied() {
    String friendId = "friendId";
    String friendEmail = "friend@example.com";

    when(userService.getUserById(userId)).thenReturn(Optional.of(new User(userId, email)));
    when(userService.getUserByEmail(friendEmail))
        .thenReturn(Optional.of(new User(friendId, friendEmail)));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.empty());

    PermissionDeniedException exception =
        assertThrows(
            PermissionDeniedException.class,
            () -> permissionService.shareWithUser(userId, friendEmail, snippetId));

    assertEquals(userId, exception.getUserId());
    assertEquals(DeniedAction.SHARE_SNIPPET, exception.getAction());
  }

  @Test
  public void shareWithUserNotFound() {
    String friendEmail = "friend@example.com";

    doThrow(new NoSuchElementException()).when(userService).getUserById(userId);

    assertThrows(
        NoSuchElementException.class,
        () -> permissionService.shareWithUser(userId, friendEmail, snippetId));
  }

  @Test
  public void shareWithUserFriendNotFound() {
    String friendEmail = "friend@example.com";

    when(userService.getUserById(userId)).thenReturn(Optional.of(new User(userId, email)));
    doThrow(new NoSuchElementException()).when(userService).getUserByEmail(friendEmail);

    assertThrows(
        NoSuchElementException.class,
        () -> permissionService.shareWithUser(userId, friendEmail, snippetId));
  }
}
