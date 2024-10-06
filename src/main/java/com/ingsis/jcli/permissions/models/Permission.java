package com.ingsis.jcli.permissions.models;

import com.ingsis.jcli.permissions.common.PermissionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Permission {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_sequence")
  @SequenceGenerator(
      name = "permission_sequence",
      sequenceName = "permission_sequence",
      allocationSize = 1)
  private Long id;

  private Long userId;
  private Long snippetId;

  @Enumerated(EnumType.STRING)
  private PermissionType type;
}
