package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final PermissionRepository permissionRepository;

  @Autowired
  public PermissionService(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  public boolean hasPermission(Long userId, Long snippetId, PermissionType type) {
    return permissionRepository
        .findByUserIdAndSnippetIdAndType(userId, snippetId, type)
        .isPresent();
  }
}
