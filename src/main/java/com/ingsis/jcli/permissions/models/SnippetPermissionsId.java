package com.ingsis.jcli.permissions.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@Data
@EqualsAndHashCode
public class SnippetPermissionsId implements Serializable {
  private Long snippetId;
  private String userId;

  public SnippetPermissionsId() {}

  public SnippetPermissionsId(Long snippetId, String userId) {
    this.snippetId = snippetId;
    this.userId = userId;
  }
}
