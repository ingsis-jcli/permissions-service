package com.ingsis.jcli.permissions.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.repository.SnippetPermissionsRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class PermissionServiceTest {

  @Mock private SnippetPermissionsRepository snippetPermissionsRepository;

  @InjectMocks private PermissionService permissionService;

  @MockBean private JwtDecoder jwtDecoder;

  private String userId;
  private Long snippetId;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    snippetId = 1L;
    userId = "1";
  }

  @Test
  void testHasPermission() {
    SnippetPermissions snippetPermissions =
        new SnippetPermissions(userId, snippetId, List.of(PermissionType.SHARED));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.of(snippetPermissions));
    boolean hasPermission = permissionService.hasPermission(userId, snippetId, PermissionType.SHARED);
    assertThat(hasPermission).isTrue();
  }

  @Test
  void testNoPermission() {
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.empty());
    boolean hasPermission = permissionService.hasPermission(userId, snippetId, PermissionType.SHARED);
    assertThat(hasPermission).isFalse();
  }

  @Test
  void testNoProperPermission() {
    SnippetPermissions snippetPermissions =
        new SnippetPermissions(userId, snippetId, List.of(PermissionType.SHARED));
    when(snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId))
        .thenReturn(Optional.of(snippetPermissions));
    boolean hasPermission =
        permissionService.hasPermission(userId, snippetId, PermissionType.OWNER);
    assertThat(hasPermission).isFalse();
  }
}
