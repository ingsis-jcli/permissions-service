package com.ingsis.jcli.permissions.repository;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.models.SnippetPermissionsId;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetPermissionsRepository
    extends JpaRepository<SnippetPermissions, SnippetPermissionsId> {
  Optional<SnippetPermissions> findByIdSnippetIdAndIdUserId(Long snippetId, String userId);
  
  @Query("SELECT sp.id.snippetId " +
      "FROM SnippetPermissions sp " +
      "JOIN sp.permissions p " +
      "WHERE sp.id.userId = :userId AND p = :permissionType")
  List<Long> findSnippetIdsByUserIdAndPermissionType(@Param("userId") String userId,
                                                     @Param("permissionType") PermissionType permissionType);
}
