package com.ingsis.jcli.permissions.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.dtos.UserDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Auth0ClientTest {

  private RestTemplate restTemplate;
  private Auth0Client auth0Client;
  private String baseUrl = "https://your-tenant-name.auth0.com/";
  private String clientId = "your-client-id";
  private String clientSecret = "your-client-secret";
  private String audience = "your-audience";

  @BeforeEach
  void setUp() {
    restTemplate = mock(RestTemplate.class);
    auth0Client = new Auth0Client(restTemplate, baseUrl, clientId, clientSecret, audience);
  }

  @Test
  void testGetAccessToken_Success() {
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("access_token", "mock-access-token");
    ResponseEntity<Map> responseEntity = ResponseEntity.ok(responseMap);

    when(restTemplate.exchange(
            eq(baseUrl + "oauth/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(responseEntity);

    String accessToken = auth0Client.getAccessToken();

    assertEquals("mock-access-token", accessToken);
  }

  @Test
  void testGetAccessToken_Failure_NoAccessToken() {
    Map<String, String> responseMap = new HashMap<>();
    ResponseEntity<Map> responseEntity = ResponseEntity.ok(responseMap);

    when(restTemplate.exchange(
            eq(baseUrl + "api/v2/oauth/token"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)))
        .thenReturn(responseEntity);

    assertThrows(RuntimeException.class, () -> auth0Client.getAccessToken());
  }

  @Test
  void testGetAccessToken_Failure_Exception() {
    when(restTemplate.exchange(
            eq(baseUrl + "api/v2/oauth/token"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(RuntimeException.class, () -> auth0Client.getAccessToken());
  }

  @Test
  void testGetAllUsers_Success() {
    String adminAccessToken = "mock-admin-access-token";
    String requestingUserId = "requesting-user-id";

    Map<String, String> user1 = new HashMap<>();
    user1.put("user_id", "user-id-1");
    user1.put("email", "user1@example.com");

    Map<String, String> user2 = new HashMap<>();
    user2.put("user_id", requestingUserId);
    user2.put("email", "user2@example.com");

    Map<String, String> user3 = new HashMap<>();
    user3.put("user_id", "user-id-3");
    user3.put("email", "user3@example.com");

    Map[] usersArray = new Map[] {user1, user2, user3};
    ResponseEntity<Map[]> responseEntity = ResponseEntity.ok(usersArray);

    when(restTemplate.exchange(
            eq("https://your-tenant-name.auth0.com/users"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map[].class)))
        .thenReturn(responseEntity);

    List<UserDto> users = auth0Client.getAllUsers(adminAccessToken, requestingUserId);

    assertEquals(2, users.size());
    assertEquals("user-id-1", users.get(0).getId());
    assertEquals("user1@example.com", users.get(0).getEmail());
    assertEquals("user-id-3", users.get(1).getId());
    assertEquals("user3@example.com", users.get(1).getEmail());
  }

  @Test
  void testGetAllUsers_Failure_Exception() {
    String adminAccessToken = "mock-admin-access-token";

    when(restTemplate.exchange(
            eq(baseUrl + "users"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map[].class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(
        RuntimeException.class, () -> auth0Client.getAllUsers(adminAccessToken, "some-user-id"));
  }
}
