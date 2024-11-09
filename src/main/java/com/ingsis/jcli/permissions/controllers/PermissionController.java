package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.clients.SnippetsClient;
import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.responses.SnippetResponse;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final SnippetsClient snippetsClient;

  @Autowired
  public PermissionController(
      PermissionService permissionService, JwtService jwtService, SnippetsClient snippetsClient) {
    this.permissionService = permissionService;
    this.jwtService = jwtService;
    this.snippetsClient = snippetsClient;
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

    SnippetResponse snippetResponse = snippetsClient.getSnippet(snippetId).getBody();

    permissionService.shareWithUser(userId, friendId, snippetId);
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
  public void newRelicAlert(
    @RequestHeader("Authorization") String token) {
    throw new RuntimeException("This is an exception");
  }

}
