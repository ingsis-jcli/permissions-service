package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.repository.SnippetPermissionsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final SnippetPermissionsRepository snippetPermissionsRepository;

  @Autowired
  public PermissionService(SnippetPermissionsRepository snippetRepository) {
    this.snippetPermissionsRepository = snippetRepository;
  }

  public boolean hasPermission(String userId, Long snippetId, PermissionType type) {
    Optional<SnippetPermissions> snippetPermissionsOpt =
        snippetPermissionsRepository.findByIdSnippetIdAndIdUserId(snippetId, userId);
    if (snippetPermissionsOpt.isEmpty()) {
      return false;
    }
    SnippetPermissions snippetPermissions = snippetPermissionsOpt.get();

    List<PermissionType> permissionTypeList = snippetPermissions.getPermissions();
    return permissionTypeList.contains(type);
  }
}
