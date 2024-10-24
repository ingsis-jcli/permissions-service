package com.ingsis.jcli.permissions.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Snippet;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.SnippetRepository;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class PermissionServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private SnippetRepository snippetRepository;

  @InjectMocks private PermissionService permissionService;

  private User user;
  private Snippet snippet;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1L);
    user.setName("Test User");

    snippet = new Snippet();
    snippet.setId(1L);
    snippet.setContent("Sample snippet");

    user.setSnippetPermissions(new HashMap<>());
    snippet.setUserPermissions(new HashMap<>());
  }

  @Test
  void testHasPermission_UserHasPermissionInSnippet() {
    user.getSnippetPermissions().put(snippet, PermissionType.WRITE);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.WRITE);

    assertThat(hasPermission).isTrue();
  }

  @Test
  void testHasPermission_SnippetHasPermissionForUser() {
    snippet.getUserPermissions().put(user, PermissionType.READ);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.READ);

    assertThat(hasPermission).isTrue();
  }

  @Test
  void testHasPermission_NoPermissionInBoth() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.EXECUTE);

    assertThat(hasPermission).isFalse();
  }

  @MockBean private JwtDecoder jwtDecoder;

  @Test
  void testHasPermission_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.READ);

    assertThat(hasPermission).isFalse();
  }

  @Test
  void testHasPermission_SnippetNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(snippetRepository.findById(1L)).thenReturn(Optional.empty());

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.READ);

    assertThat(hasPermission).isFalse();
  }

  @Test
  void testHasPermission_UserAndSnippetBothHaveNoPermissions() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

    boolean hasPermission = permissionService.hasPermission(1L, 1L, PermissionType.READ);

    assertThat(hasPermission).isFalse();
  }
}
