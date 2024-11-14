package com.ingsis.jcli.permissions.repository;

import com.ingsis.jcli.permissions.models.SnippetPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<SnippetPermission, Long> {
  void deleteBySnippetId(Long snippetId);
}
