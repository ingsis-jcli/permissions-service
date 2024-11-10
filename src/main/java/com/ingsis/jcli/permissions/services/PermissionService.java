package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.exceptions.DeniedAction;
import com.ingsis.jcli.permissions.common.exceptions.PermissionDeniedException;
import com.ingsis.jcli.permissions.models.SnippetPermission;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.PermissionRepository;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService {

  private final UserService userService;
  private final UserRepository userRepository;
  private PermissionRepository snippetPermissionRepository;

  @Autowired
  public PermissionService(
      UserService userService,
      UserRepository userRepository,
      PermissionRepository snippetPermissionRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.snippetPermissionRepository = snippetPermissionRepository;
  }

  public Optional<SnippetPermission> getUserPermissionOnSnippet(Long snippetId, User user) {
    Set<SnippetPermission> snippetPermissionSet = user.getSnippetPermissions();
    return snippetPermissionSet.stream()
        .filter(sp -> sp.getSnippetId().equals(snippetId))
        .findFirst();
  }

  public String grantOwnerPermission(Long snippetId, String userId) {
    User user = userService.getUser(userId);
    Optional<SnippetPermission> snippetPermissionsOpt = getUserPermissionOnSnippet(snippetId, user);
    if (snippetPermissionsOpt.isEmpty()) {
      addPermission(PermissionType.OWNER, user, snippetId);
      return "Permission granted";
    }
    SnippetPermission snippetPermissions = snippetPermissionsOpt.get();
    List<PermissionType> permissionTypeList = snippetPermissions.getPermissions();
    if (permissionTypeList.contains(PermissionType.OWNER)) {
      return "Permission already granted";
    }
    addPermission(PermissionType.OWNER, user, snippetId);
    return "Permission granted";
  }

  public void addPermission(PermissionType permission, User user, Long snippetId) {
    user.addSnippetPermission(snippetId, permission);
    userRepository.save(user);
  }

  public boolean hasPermission(String userId, Long snippetId, PermissionType type) {
    User user = userService.getUser(userId);
    Optional<SnippetPermission> snippetPermissionOpt = getUserPermissionOnSnippet(snippetId, user);
    if (snippetPermissionOpt.isEmpty()) {
      return false;
    }
    List<PermissionType> permissionTypeList = snippetPermissionOpt.get().getPermissions();
    return permissionTypeList.contains(type);
  }

  public String shareWithUser(String userId, String friendId, Long snippetId) {
    User friend = userService.getUser(friendId);
    if (!canShareSnippet(userId, snippetId)) {
      throw new PermissionDeniedException(userId, DeniedAction.SHARE_SNIPPET);
    }
    Optional<SnippetPermission> snippetPermissionOptional =
        getUserPermissionOnSnippet(snippetId, friend);
    if (snippetPermissionOptional.isPresent()
        && snippetPermissionOptional.get().getPermissions().contains(PermissionType.SHARED)) {
      return "Snippet already shared with user";
    }
    addPermission(PermissionType.SHARED, friend, snippetId);
    return "Snippet shared with user";
  }

  public boolean canShareSnippet(String userId, Long snippetId) {
    return hasPermission(userId, snippetId, PermissionType.OWNER);
  }

  public List<Long> getSnippetsSharedWithUser(String userId) {
    User user = userService.getUser(userId);
    Set<SnippetPermission> snippetPermissionSet = user.getSnippetPermissions();

    return snippetPermissionSet.stream()
        .filter(sp -> sp.getPermissions().contains(PermissionType.SHARED))
        .map(SnippetPermission::getSnippetId)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deletePermissionsBySnippetId(Long snippetId) {
    snippetPermissionRepository.deleteBySnippetId(snippetId);
  }
}
