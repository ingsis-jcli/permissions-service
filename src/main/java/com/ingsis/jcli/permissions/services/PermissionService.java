package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.Snippet;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.SnippetRepository;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private final UserRepository userRepository;
  private final SnippetRepository snippetRepository;

  @Autowired
  public PermissionService(UserRepository userRepository, SnippetRepository snippetRepository) {
    this.userRepository = userRepository;
    this.snippetRepository = snippetRepository;
  }

  public boolean hasPermission(Long userId, Long snippetId, PermissionType type) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return false;
    }
    User user = userOpt.get();

    Optional<Snippet> snippetOpt = snippetRepository.findById(snippetId);
    if (snippetOpt.isEmpty()) {
      return false;
    }
    Snippet snippet = snippetOpt.get();

    if (user.getSnippetPermissions().containsKey(snippet)) {
      return user.getSnippetPermissions().get(snippet).equals(type);
    }

    if (snippet.getUserPermissions().containsKey(user)) {
      return snippet.getUserPermissions().get(user).equals(type);
    }

    return false;
  }
}
