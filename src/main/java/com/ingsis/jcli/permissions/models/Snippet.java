package com.ingsis.jcli.permissions.models;

import com.ingsis.jcli.permissions.common.PermissionType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.SequenceGenerator;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Entity
@Data
public class Snippet {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippet_sequence")
  @SequenceGenerator(
      name = "snippet_sequence",
      sequenceName = "snippet_sequence",
      allocationSize = 1)
  private Long id;

  private String content;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "snippet_user_permissions",
      joinColumns = @JoinColumn(name = "snippet_id"))
  @MapKeyJoinColumn(name = "user_id")
  @Column(name = "permission_type")
  @Enumerated(EnumType.STRING)
  private Map<User, PermissionType> userPermissions = new HashMap<>();
}
