package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  @Autowired
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @GetMapping()
  public ResponseEntity<Boolean> hasPermission(
      @RequestParam("type") String type,
      @RequestParam("snippetId") Long snippetId,
      @RequestParam("userId") Long userId) {

    PermissionType permissionType = PermissionType.valueOf(type.toUpperCase());

    boolean hasPermission = permissionService.hasPermission(userId, snippetId, permissionType);

    return ResponseEntity.ok(hasPermission);
  }
}
