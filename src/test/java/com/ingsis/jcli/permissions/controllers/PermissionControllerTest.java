package com.ingsis.jcli.permissions.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.clients.SnippetsClient;
import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.common.exceptions.DeniedAction;
import com.ingsis.jcli.permissions.common.exceptions.PermissionDeniedException;
import com.ingsis.jcli.permissions.common.responses.ProcessStatus;
import com.ingsis.jcli.permissions.common.responses.SnippetResponse;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PermissionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PermissionService permissionService;
  @MockBean private SnippetsClient snippetsClient;

  @MockBean private JwtDecoder jwtDecoder;

  @MockBean private JwtService jwtService;

  @Autowired private ObjectMapper objectMapper;

  private static final String path = "/permissions";
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
  public void hasPermissionOk() throws Exception {
    String userId = "1";
    Long snippetId = 1L;
    String type = PermissionType.SHARED.name();

    setupJwt(userId);

    when(permissionService.hasPermission(userId, snippetId, PermissionType.SHARED))
        .thenReturn(true);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  public void hasPermissionDenied() throws Exception {
    String userId = "1";
    Long snippetId = 1L;
    String type = PermissionType.SHARED.name();

    setupJwt(userId);

    when(permissionService.hasPermission(userId, snippetId, PermissionType.SHARED))
        .thenReturn(false);

    mockMvc
        .perform(
            get(path)
                .param("type", type)
                .param("snippetId", snippetId.toString())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(false));
  }

  @Test
  public void shareWithUserOk() throws Exception {
    String userId = "1";
    String friendId = "friend123";
    Long snippetId = 123L;
    String token = "Bearer test-token";

    setupJwt(userId);

    SnippetResponse snippetResponse =
        new SnippetResponse(
            snippetId,
            "a",
            "let a: string;",
            "printscript",
            "1.0",
            "ps",
            ProcessStatus.COMPLIANT,
            userId);

    when(jwtService.extractUserId(token)).thenReturn(userId);
    when(snippetsClient.getSnippet(snippetId)).thenReturn(ResponseEntity.ok(snippetResponse));

    mockMvc
        .perform(
            post(path + "/share")
                .param("snippetId", snippetId.toString())
                .param("friendId", friendId)
                .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(snippetResponse.getId()))
        .andExpect(jsonPath("$.name").value(snippetResponse.getName()))
        .andExpect(jsonPath("$.content").value(snippetResponse.getContent()))
        .andExpect(jsonPath("$.language").value(snippetResponse.getLanguage()))
        .andExpect(jsonPath("$.version").value(snippetResponse.getVersion()))
        .andExpect(jsonPath("$.compliance").value(snippetResponse.getCompliance().toString()))
        .andExpect(jsonPath("$.author").value(snippetResponse.getAuthor()));
  }

  @Test
  public void shareWithUserForbidden() throws Exception {
    String userId = "1";
    String friendId = "friend123";
    Long snippetId = 123L;

    setupJwt(userId);

    doThrow(new PermissionDeniedException(userId, DeniedAction.SHARE_SNIPPET))
        .when(permissionService)
        .shareWithUser(userId, friendId, snippetId);

    SnippetResponse snippetResponse =
        new SnippetResponse(
            snippetId,
            "a",
            "let a: string;",
            "printscript",
            "1.0",
            "ps",
            ProcessStatus.COMPLIANT,
            userId);

    when(snippetsClient.getSnippet(snippetId)).thenReturn(ResponseEntity.ok(snippetResponse));

    mockMvc
        .perform(
            post(path + "/share")
                .param("snippetId", snippetId.toString())
                .param("friendId", friendId)
                .header("Authorization", token))
        .andExpect(status().isForbidden());
  }

  @Test
  public void grantOwnerPermissionSuccess() throws Exception {
    Long snippetId = 1L;
    String userId = "123";

    setupJwt(userId);

    when(jwtService.extractUserId(token)).thenReturn(userId);

    mockMvc
        .perform(
            post(path + "/own")
                .param("snippetId", snippetId.toString())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getSnippetsSharedWithUser() throws Exception {
    String userId = "1";
    List<Long> snippetIds = List.of(1L, 2L, 3L);

    setupJwt(userId);

    when(permissionService.getSnippetsSharedWithUser(userId)).thenReturn(snippetIds);

    mockMvc
        .perform(
            get(path + "/shared")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(snippetIds.size()))
        .andExpect(jsonPath("$[0]").value(1L))
        .andExpect(jsonPath("$[1]").value(2L))
        .andExpect(jsonPath("$[2]").value(3L));
  }
}
