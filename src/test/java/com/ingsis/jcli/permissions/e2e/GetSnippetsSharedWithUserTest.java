package com.ingsis.jcli.permissions.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.jcli.permissions.common.PermissionType;
import com.ingsis.jcli.permissions.models.SnippetPermissions;
import com.ingsis.jcli.permissions.repository.SnippetPermissionsRepository;
import com.ingsis.jcli.permissions.services.JwtService;
import com.ingsis.jcli.permissions.services.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetSnippetsSharedWithUserTest {
  
  @Autowired private MockMvc mockMvc;
  @Autowired private PermissionService permissionService;
  @Autowired private SnippetPermissionsRepository permissionsRepository;
  @Autowired private ObjectMapper objectMapper;
  
  @MockBean private JwtDecoder jwtDecoder;
  @MockBean private JwtService jwtService;
  
  private static final String path = "/permissions/user";
  
  private static final String token = "Bearer token";
  
  private Jwt createMockJwt(String userId) {
    return Jwt.withTokenValue("mock-token")
        .header("alg", "none")
        .claim("user_id", userId)
        .claim("scope", "write")
        .build();
  }
  
  public void setupJwt(String userId) {
    Jwt mockJwt = createMockJwt(userId);
    when(jwtService.extractUserId(token)).thenReturn(userId);
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
  }
  
  @BeforeEach
  public void setup() {
    List<SnippetPermissions> mocks = List.of(
        // Snippet 1
        new SnippetPermissions("user1", 1L, List.of(PermissionType.OWNER)),
        
        // Snippet 2
        new SnippetPermissions("user1", 2L, List.of(PermissionType.OWNER)),
        new SnippetPermissions("user2", 2L, List.of(PermissionType.SHARED)),
        new SnippetPermissions("user4", 2L, List.of(PermissionType.SHARED)),
        
        // Snippet 3
        new SnippetPermissions("user2", 3L, List.of(PermissionType.OWNER)),
        new SnippetPermissions("user1", 3L, List.of(PermissionType.SHARED)),
        new SnippetPermissions("user3", 3L, List.of(PermissionType.SHARED)),
        new SnippetPermissions("user4", 3L, List.of(PermissionType.SHARED)),
        
        // Snippet 4
        new SnippetPermissions("user3", 4L, List.of(PermissionType.OWNER)),
        new SnippetPermissions("user1", 4L, List.of(PermissionType.SHARED)),
        new SnippetPermissions("user2", 4L, List.of(PermissionType.SHARED)),
        new SnippetPermissions("user4", 4L, List.of(PermissionType.SHARED)),
        
        // Snippet 5
        new SnippetPermissions("user1", 5L, List.of(PermissionType.OWNER)),
        new SnippetPermissions("user4", 5L, List.of(PermissionType.SHARED)),
        
        // Snippet 6
        new SnippetPermissions("user5", 6L, List.of(PermissionType.OWNER))
    );
    
    permissionsRepository.deleteAll();
    permissionsRepository.saveAll(mocks);
  }
  
  @Test
  public void sharedWithUser1() throws Exception {
    String userId = "user1";
    setupJwt(userId);
    
    MvcResult result = mockMvc.perform(get(path)
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andReturn();
    
    String jsonResponse = result.getResponse().getContentAsString();
    List<Long> snippetIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

    assertThat(snippetIds).containsExactlyInAnyOrder(3L, 4L);
  }
  
  @Test
  public void sharedWithUser2() throws Exception {
    String userId = "user2";
    setupJwt(userId);
    
    MvcResult result = mockMvc.perform(get(path)
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andReturn();
    
    String jsonResponse = result.getResponse().getContentAsString();
    List<Long> snippetIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    
    assertThat(snippetIds).containsExactlyInAnyOrder(2L, 4L);
  }
  
  @Test
  public void sharedWithUser3() throws Exception {
    String userId = "user3";
    setupJwt(userId);
    
    MvcResult result = mockMvc.perform(get(path)
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andReturn();
    
    String jsonResponse = result.getResponse().getContentAsString();
    List<Long> snippetIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    
    assertThat(snippetIds).containsExactlyInAnyOrder(3L);
  }
  
  @Test
  public void sharedWithUser4() throws Exception {
    String userId = "user4";
    setupJwt(userId);
    
    MvcResult result = mockMvc.perform(get(path)
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andReturn();
    
    String jsonResponse = result.getResponse().getContentAsString();
    List<Long> snippetIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    
    assertThat(snippetIds).containsExactlyInAnyOrder(2L, 3L, 4L, 5L);
  }
  
  @Test
  public void sharedWithUser5() throws Exception {
    String userId = "user5";
    setupJwt(userId);
    
    MvcResult result = mockMvc.perform(get(path)
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andReturn();
    
    String jsonResponse = result.getResponse().getContentAsString();
    List<Long> snippetIds = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    
    assertThat(snippetIds.size()).isEqualTo(0);
  }
}
