package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.dtos.UserDto;
import com.ingsis.jcli.permissions.services.Auth0Service;
import com.ingsis.jcli.permissions.services.JwtService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  private final JwtService jwtService;
  private final Auth0Service auth0Service;

  @Autowired
  public UserController(JwtService jwtService, Auth0Service auth0Service) {
    this.jwtService = jwtService;
    this.auth0Service = auth0Service;
  }

  @GetMapping()
  public List<UserDto> getUsers(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize,
      @RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);
    return auth0Service.getAllUsers(userId, page, pageSize);
  }

  @GetMapping("/email")
  public String getUserEmail(@RequestParam String id) {
    return auth0Service.getUserEmail(id).orElseThrow();
  }
}
