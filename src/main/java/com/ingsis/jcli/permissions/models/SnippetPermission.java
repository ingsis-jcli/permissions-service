package com.ingsis.jcli.permissions.models;

import com.ingsis.jcli.permissions.common.PermissionType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
public class SnippetPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter private Long snippetId;

  @Setter
  @Getter
  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private List<PermissionType> permissions = new ArrayList<>();

  public SnippetPermission() {}

  public SnippetPermission(Long snippetId, List<PermissionType> permissions) {
    this.snippetId = snippetId;
    this.permissions = permissions;
  }
}
