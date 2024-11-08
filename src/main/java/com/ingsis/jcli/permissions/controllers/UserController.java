package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.common.responses.PaginatedUsers;
import com.ingsis.jcli.permissions.dtos.UserDto;
import com.ingsis.jcli.permissions.services.Auth0Service;
import com.ingsis.jcli.permissions.services.JwtService;
import java.util.List;
import java.util.Optional;
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
  public PaginatedUsers getUsers(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize,
      @RequestParam(required = false) Optional<String> name,
      @RequestHeader("Authorization") String token) {

    if (name.isPresent() && (name.get().isBlank() || name.get().isEmpty())) {
      name = Optional.empty();
    }
    System.out.println("name: " + name);
    if (name.isPresent()) {
      System.out.println("name is present: " + "-" + name.get() + "-");
    }
    String userId = jwtService.extractUserId(token);
    int count = auth0Service.getUserCount() - 1;
    List<UserDto> users = auth0Service.getAllUsers(userId, page, pageSize, name);
    System.out.println("users: " + users);
    return new PaginatedUsers(page, pageSize, count, users);
  }
}
