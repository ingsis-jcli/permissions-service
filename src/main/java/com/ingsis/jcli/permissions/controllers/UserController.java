package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.dtos.UserDto;
import com.ingsis.jcli.permissions.services.Auth0Service;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;
  private final Auth0Service auth0Service;

  @Autowired
  public UserController(UserService userService, JwtService jwtService, Auth0Service auth0Service) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.auth0Service = auth0Service;
  }

  @PostMapping
  public ResponseEntity<Void> saveUser(@RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);
    String email = jwtService.extractEmail(token);
    if (userService.userExists(userId)) {
      return ResponseEntity.ok().build();
    }
    userService.saveUser(userId, email);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("emails")
  public ResponseEntity<List<String>> getEmails(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize,
      @RequestHeader("Authorization") String token) {

    String userId = jwtService.extractUserId(token);
    if (!userService.userExists(userId)) {
      return ResponseEntity.notFound().build();
    }

    List<String> emails = userService.getEmails(userId, page, pageSize);
    return ResponseEntity.ok(emails);
  }

  @GetMapping("/users")
  public List<UserDto> getUsers(@RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);
    return auth0Service.getAllUsers(userId);
  }
}
