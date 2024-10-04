package com.ingsis.jcli.permissions.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Permission;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PermissionsRepositoryTest {

  @Autowired private PermissionRepository permissionRepository;

  @Test
  void testSavePermission() {
    Permission permission = new Permission();
    permission.setUserId(1L);
    permission.setSnippetId(2L);
    permission.setType(PermissionType.READ);

    Permission savedPermission = permissionRepository.save(permission);

    assertThat(savedPermission.getId()).isNotNull();
    assertThat(savedPermission.getUserId()).isEqualTo(1L);
    assertThat(savedPermission.getSnippetId()).isEqualTo(2L);
    assertThat(savedPermission.getType()).isEqualTo(PermissionType.READ);
  }

  @Test
  void testFindByUserIdAndSnippetIdAndType() {
    // Create and save a Permission entity
    Permission permission = new Permission();
    permission.setUserId(1L);
    permission.setSnippetId(2L);
    permission.setType(PermissionType.WRITE);

    permissionRepository.save(permission);

    Optional<Permission> foundPermission =
        permissionRepository.findByUserIdAndSnippetIdAndType(1L, 2L, PermissionType.WRITE);

    assertThat(foundPermission).isPresent();
    assertThat(foundPermission.get().getUserId()).isEqualTo(1L);
    assertThat(foundPermission.get().getSnippetId()).isEqualTo(2L);
    assertThat(foundPermission.get().getType()).isEqualTo(PermissionType.WRITE);
  }

  @Test
  void testFindByUserIdAndSnippetIdAndType_NotFound() {
    Optional<Permission> foundPermission =
        permissionRepository.findByUserIdAndSnippetIdAndType(1L, 2L, PermissionType.WRITE);

    assertThat(foundPermission).isNotPresent();
  }
}
