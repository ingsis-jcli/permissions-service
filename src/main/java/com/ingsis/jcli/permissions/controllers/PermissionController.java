package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.responses.SnippetResponse;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @PostMapping("/share")
  public ResponseEntity<SnippetResponse> shareWithUser(
      @RequestParam Long snippetId,
      @RequestParam String friendId,
      @RequestHeader("Authorization") String token) {

    System.out.println("Sharing snippet with user: " + friendId);

    String userId = jwtService.extractUserId(token);

    permissionService.shareWithUser(userId, friendId, snippetId);

    SnippetResponse snippetResponse = permissionService.getSnippetById(snippetId);

    return ResponseEntity.ok(snippetResponse);
  }

  @PostMapping("/own")
  public ResponseEntity<Void> grantOwnerPermission(
      @RequestParam Long snippetId, @RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);
    permissionService.grantOwnerPermission(snippetId, userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/shared")
  public ResponseEntity<List<Long>> getSnippetsSharedWithUser(
      @RequestHeader("Authorization") String token) {

    String userId = jwtService.extractUserId(token);
    List<Long> snippetIds = permissionService.getSnippetsSharedWithUser(userId);
    return ResponseEntity.ok(snippetIds);
  }

  @GetMapping("/alert")
  public void newRelicAlert(@RequestHeader("Authorization") String token) {
    throw new RuntimeException("This is an exception");
  }

  @DeleteMapping("/snippet/{snippetId}")
  public ResponseEntity<Void> deletePermissionsBySnippetId(
      @PathVariable Long snippetId, @RequestHeader("Authorization") String token) {

    permissionService.deletePermissionsBySnippetId(snippetId);

    return ResponseEntity.noContent().build();
  }
}
