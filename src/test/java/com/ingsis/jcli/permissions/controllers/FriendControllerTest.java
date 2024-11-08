package com.ingsis.jcli.permissions.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.services.FriendService;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FriendControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FriendService friendService;

  @MockBean private JwtDecoder jwtDecoder;

  @MockBean private JwtService jwtService;

  @MockBean private UserService userService;

  @Autowired private ObjectMapper objectMapper;

  private static final String path = "/friends";
  private static final String token = "Bearer token";
  private static final String email = "user@example.com";
  private static final String userId = "user123";
  private static final String friendId = "user1234";

  private Jwt createMockJwt() {
    return Jwt.withTokenValue("mock-token")
        .header("alg", "none")
        .claim("user_id", userId)
        .claim("email", email)
        .claim("scope", "read")
        .build();
  }

  public void setupJwt() {
    Jwt mockJwt = createMockJwt();
    when(jwtService.extractUserId(token)).thenReturn(userId);
    when(jwtService.extractEmail(token)).thenReturn(email);
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
  }

  @Test
  void testGetFriends() throws Exception {
    List<String> friendIds = List.of("friend1", "friend2");

    setupJwt();

    when(friendService.getFriends(userId)).thenReturn(friendIds);

    mockMvc
        .perform(get(path).header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(friendIds.size()))
        .andExpect(jsonPath("$[0]").value("friend1"))
        .andExpect(jsonPath("$[1]").value("friend2"));
  }

  @Test
  void testAddFriendWhenNotAlreadyFriends() throws Exception {
    User user = new User(userId);
    User friend = new User(friendId);

    setupJwt();

    when(friendService.areFriends(userId, friendId)).thenReturn(false);
    when(userService.getUser(userId)).thenReturn(user);
    when(userService.getUser(friendId)).thenReturn(friend);

    mockMvc
        .perform(
            post(path)
                .header("Authorization", token)
                .param("friendId", friendId)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    verify(friendService).areFriends(userId, friendId);
    verify(userService).getUser(userId);
    verify(userService).getUser(friendId);
    verify(friendService).addFriend(user, friend);
  }

  @Test
  void testAddFriendWhenAlreadyFriends() throws Exception {
    setupJwt();
    when(friendService.areFriends(userId, friendId)).thenReturn(true);

    mockMvc
        .perform(
            post("/friends")
                .header("Authorization", token)
                .param("friendId", friendId)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(friendService).areFriends(userId, friendId);
    verify(userService, never()).getUser(anyString());
    verify(friendService, never()).addFriend(any(), any());
  }
}
