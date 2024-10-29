package com.ingsis.jcli.permissions.controllers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.services.FriendService;
import com.ingsis.jcli.permissions.services.JwtService;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FriendControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private FriendService friendService;
  @MockBean private JwtDecoder jwtDecoder;
  @MockBean private JwtService jwtService;

  private static final String path = "/friends";
  private static final String token = "Bearer token";
  private static final String email = "user@example.com";

  private Jwt createMockJwt(String userId) {
    return Jwt.withTokenValue("mock-token")
        .header("alg", "none")
        .claim("user_id", userId)
        .claim("email", email)
        .claim("scope", "read")
        .build();
  }

  public void setupJwt(String userId) {
    Jwt mockJwt = createMockJwt(userId);
    when(jwtService.extractUserId(token)).thenReturn(userId);
    when(jwtService.extractEmail(token)).thenReturn(email);
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
  }

  @Test
  public void getFriends() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    List<String> friends = List.of("friend1", "friend2", "friend3", "friend4", "friend5");
    when(friendService.getFriends(userId)).thenReturn(friends);

    MvcResult result =
        mockMvc
            .perform(get(path).header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(friends.size()))
            .andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    List<String> friendIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

    assertThat(friendIds).containsExactlyInAnyOrderElementsOf(friends);
  }

  @Test
  public void getFriendsEmpty() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    when(friendService.getFriends(userId)).thenThrow(new NoSuchElementException());

    mockMvc.perform(get(path).header("Authorization", token)).andExpect(status().isNotFound());
  }

  @Test
  public void addFriend() throws Exception {
    String friendId = "friendId";
    String userId = "userId";
    setupJwt(userId);

    when(friendService.areFriends(userId, friendId)).thenReturn(false);

    mockMvc
        .perform(post(path).param("friendId", friendId).header("Authorization", token))
        .andExpect(status().isCreated());
  }

  @Test
  public void addFriendExisting() throws Exception {
    String friendId = "friendId";
    String userId = "userId";
    setupJwt(userId);

    when(friendService.areFriends(userId, friendId)).thenReturn(true);

    mockMvc
        .perform(post(path).param("friendId", friendId).header("Authorization", token))
        .andExpect(status().isOk());
  }
}
