package com.ingsis.jcli.permissions.repository;

import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.models.SnippetPermissionsId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetPermissionsRepository
    extends JpaRepository<SnippetPermissions, SnippetPermissionsId> {
  Optional<SnippetPermissions> findByIdSnippetIdAndIdUserId(Long snippetId, String userId);
}
