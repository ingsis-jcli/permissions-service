package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Permission;
import com.ingsis.jcli.permissions.repository.PermissionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PermissionServiceTest {

  @Autowired private PermissionService permissionService;

  @MockBean private PermissionRepository permissionRepository;

  @MockBean private JwtDecoder jwtDecoder;

  @Test
  void hasPermission() {
    Long userId = 1L;
    Long snippetId = 1L;
    PermissionType type = PermissionType.READ;

    when(permissionRepository.findByUserIdAndSnippetIdAndType(userId, snippetId, type))
        .thenReturn(Optional.of(new Permission()));

    assertTrue(permissionService.hasPermission(userId, snippetId, type));
  }

  @Test
  void hasPermissionNotFound() {
    Long userId = 1L;
    Long snippetId = 1L;
    PermissionType type = PermissionType.READ;

    when(permissionRepository.findByUserIdAndSnippetIdAndType(userId, snippetId, type))
        .thenReturn(Optional.empty());

    assertFalse(permissionService.hasPermission(userId, snippetId, type));
  }
}
