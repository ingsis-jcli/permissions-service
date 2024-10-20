package com.ingsis.jcli.permissions.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.services.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

  private static final String path = "/permissions";

  @Test
  void hasPermissionOk() throws Exception {
    Long userId = 1L;
    Long snippetId = 1L;
    String type = PermissionType.READ.name();

    when(permissionService.hasPermission(userId, snippetId, PermissionType.READ)).thenReturn(true);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.claim("scope", "read:snippets"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  void hasPermissionDenied() throws Exception {
    Long userId = 1L;
    Long snippetId = 1L;
    String type = PermissionType.READ.name();

    when(permissionService.hasPermission(userId, snippetId, PermissionType.READ)).thenReturn(false);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.claim("scope", "read:snippets"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(false));
  }
}
