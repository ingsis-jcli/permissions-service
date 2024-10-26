package com.ingsis.jcli.permissions.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PermissionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PermissionService permissionService;

  @MockBean private JwtDecoder jwtDecoder;

  @MockBean private JwtService jwtService;

  @Autowired private ObjectMapper objectMapper;

  private static final String path = "/permissions";

  private Jwt createMockJwt(String userId) {
    return Jwt.withTokenValue("mock-token")
        .header("alg", "none")
        .claim("user_id", userId)
        .claim("scope", "read")
        .build();
  }

  @Test
  void hasPermissionOk() throws Exception {
    String userId = "1";
    Long snippetId = 1L;
    String type = PermissionType.READ.name();

    Jwt mockJwt = createMockJwt(userId);

    when(jwtService.extractUserId(anyString())).thenReturn(userId);
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
    when(permissionService.hasPermission(userId, snippetId, PermissionType.READ)).thenReturn(true);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .header("Authorization", "Bearer mock-token")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(mockJwt)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  void hasPermissionDenied() throws Exception {
    String userId = "1";
    Long snippetId = 1L;
    String type = PermissionType.READ.name();

    Jwt mockJwt = createMockJwt(userId);
    when(jwtService.extractUserId(anyString())).thenReturn(userId);
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
    when(permissionService.hasPermission(userId, snippetId, PermissionType.READ)).thenReturn(false);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .header("Authorization", "Bearer mock-token")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(mockJwt)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(false));
  }
}
