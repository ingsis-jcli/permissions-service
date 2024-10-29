package com.ingsis.jcli.permissions.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.models.SnippetPermissionsId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class SnippetPermissionsRepositoryTest {

  @Autowired private SnippetPermissionsRepository snippetPermissionsRepository;

  private SnippetPermissionsId snippetPermissionsId;
  private SnippetPermissions snippetPermissions;

  @BeforeEach
  void setUp() {
    snippetPermissionsId = new SnippetPermissionsId(1L, "user123");
    snippetPermissions =
        new SnippetPermissions("user123", 1L, List.of(PermissionType.SHARED));
  }

  @Test
  void testSaveAndFindSnippetPermissions() {
    // Save the SnippetPermissions object
    snippetPermissionsRepository.save(snippetPermissions);

    // Find by ID
    Optional<SnippetPermissions> foundSnippetPermissions =
        snippetPermissionsRepository.findById(snippetPermissionsId);

    // Assertions
    assertTrue(foundSnippetPermissions.isPresent(), "SnippetPermissions should be found");
    assertEquals(
        snippetPermissionsId,
        foundSnippetPermissions.get().getId(),
        "The found ID should match the saved ID");
    assertEquals(
        snippetPermissions.getPermissions(),
        foundSnippetPermissions.get().getPermissions(),
        "The permissions should match");
  }

  @Test
  void testFindBySnippetIdAndUserId() {
    // Save the SnippetPermissions object
    snippetPermissionsRepository.save(snippetPermissions);

    // Find by snippet ID and user ID
    Optional<SnippetPermissions> foundBySnippetAndUser =
        snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(1L, "user123");

    // Assertions
    assertTrue(
        foundBySnippetAndUser.isPresent(),
        "SnippetPermissions should be found by snippet ID and user ID");
    assertEquals(
        snippetPermissionsId,
        foundBySnippetAndUser.get().getId(),
        "The found ID should match the saved ID");
    assertEquals(
        snippetPermissions.getPermissions(),
        foundBySnippetAndUser.get().getPermissions(),
        "The permissions should match");
  }
}
