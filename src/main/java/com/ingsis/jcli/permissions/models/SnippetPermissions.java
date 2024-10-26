package com.ingsis.jcli.permissions.models;

import com.ingsis.jcli.permissions.common.PermissionType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class SnippetPermissions {

  @EmbeddedId private SnippetPermissionsId id;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "snippet_permissions_table",
      joinColumns = {@JoinColumn(name = "snippet_id"), @JoinColumn(name = "user_id")})
  @Enumerated(EnumType.STRING)
  private List<PermissionType> permissions;

  public SnippetPermissions() {}

  public SnippetPermissions(String userId, Long snippetId, List<PermissionType> permissions) {
    this.id = new SnippetPermissionsId(snippetId, userId);
    this.permissions = permissions;
  }
}
