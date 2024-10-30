package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.exceptions.DeniedAction;
import com.ingsis.jcli.permissions.common.exceptions.PermissionDeniedException;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.SnippetPermissionsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final SnippetPermissionsRepository snippetPermissionsRepository;
  private final UserService userService;

  @Autowired
  public PermissionService(
      SnippetPermissionsRepository snippetRepository, UserService userService) {
    this.snippetPermissionsRepository = snippetRepository;
    this.userService = userService;
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

  public void shareWithUser(String userId, String friendEmail, Long snippetId) {
    userService.getUserById(userId).orElseThrow();
    User friend = userService.getUserByEmail(friendEmail).orElseThrow();

    if (!canShareSnippet(userId, snippetId)) {
      throw new PermissionDeniedException(userId, DeniedAction.SHARE_SNIPPET);
    }

    List<PermissionType> permission = List.of(PermissionType.SHARED);

    snippetPermissionsRepository.save(
        new SnippetPermissions(friend.getUserId(), snippetId, permission));
  }

  public boolean canShareSnippet(String userId, Long snippetId) {
    return hasPermission(userId, snippetId, PermissionType.OWNER);
  }

  public List<Long> getSnippetsSharedWithUser(String userId) {
    return snippetPermissionsRepository.findSnippetIdsByUserIdAndPermissionType(
        userId, PermissionType.SHARED);
  }
}
