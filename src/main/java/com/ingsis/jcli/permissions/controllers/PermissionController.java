package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

  private final PermissionService permissionService;
  private final JwtService jwtService;

  @Autowired
  public PermissionController(PermissionService permissionService, JwtService jwtService) {
    this.permissionService = permissionService;
    this.jwtService = jwtService;
  }

  @GetMapping()
  public ResponseEntity<Boolean> hasPermission(
      @RequestParam("type") String type,
      @RequestParam("snippetId") Long snippetId,
      @RequestHeader("Authorization") String token) {

    String userId = jwtService.extractUserId(token);

    PermissionType permissionType = PermissionType.valueOf(type.toUpperCase());

    boolean hasPermission = permissionService.hasPermission(userId, snippetId, permissionType);

    return ResponseEntity.ok(hasPermission);
  }
  
  @GetMapping("/user")
  public ResponseEntity<List<Long>> getSnippetsSharedWithUser(
      @RequestHeader("Authorization") String token) {
    
    String userId = jwtService.extractUserId(token);
    List<Long> snippetIds = permissionService.getSnippetsSharedWithUser(userId);
    return ResponseEntity.ok(snippetIds);
  }
}
