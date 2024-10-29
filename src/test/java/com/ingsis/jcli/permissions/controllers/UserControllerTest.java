package com.ingsis.jcli.permissions.controllers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.UserService;
import java.util.List;
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
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;
  @MockBean private JwtDecoder jwtDecoder;
  @MockBean private JwtService jwtService;

  private static final String path = "/users";
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
  public void saveNewUser() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    when(userService.userExists(userId)).thenReturn(false);

    mockMvc.perform(post(path).header("Authorization", token)).andExpect(status().isCreated());
  }

  @Test
  public void saveExistingUser() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    when(userService.userExists(userId)).thenReturn(true);

    mockMvc.perform(post(path).header("Authorization", token)).andExpect(status().isOk());
  }

  @Test
  public void getEmails() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    List<String> emails = List.of("user1@example.com", "user2@example.com", "user3@example.com");

    when(userService.userExists(userId)).thenReturn(true);
    when(userService.getEmails(userId, 0, 10)).thenReturn(emails);

    MvcResult result =
        mockMvc
            .perform(get(path + "/emails").header("Authorization", token))
            .andExpect(status().isOk())
            .andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    List<String> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

    assertThat(response).containsExactlyInAnyOrderElementsOf(emails);
  }

  @Test
  public void getEmailsUserNotFound() throws Exception {
    String userId = "userId";
    setupJwt(userId);

    when(userService.userExists(userId)).thenReturn(false);

    mockMvc
        .perform(get(path + "/emails").header("Authorization", token))
        .andExpect(status().isNotFound());
  }
}
