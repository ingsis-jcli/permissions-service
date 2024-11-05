package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.services.FriendService;
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
@RequestMapping("friends")
public class FriendController {

  private final FriendService friendService;
  private final JwtService jwtService;
  private final UserService userService;

  @Autowired
  public FriendController(
      FriendService friendService, JwtService jwtService, UserService userService) {
    this.friendService = friendService;
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<String>> getFriends(@RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);
    List<String> friendIds = friendService.getFriends(userId);
    return ResponseEntity.ok(friendIds);
  }

  @PostMapping
  public ResponseEntity<Void> addFriend(
      @RequestParam("friendId") String friendId, @RequestHeader("Authorization") String token) {
    String userId = jwtService.extractUserId(token);

    if (friendService.areFriends(userId, friendId)) {
      return ResponseEntity.ok().build();
    }

    User user = userService.getUser(userId);
    User friend = userService.getUser(friendId);

    friendService.addFriend(user, friend);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
