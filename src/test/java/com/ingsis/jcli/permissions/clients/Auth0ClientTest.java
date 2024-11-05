package com.ingsis.jcli.permissions.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
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
            eq(baseUrl + "oauth/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(responseEntity);

    assertThrows(RuntimeException.class, () -> auth0Client.getAccessToken());
  }

  @Test
  void testGetAccessToken_Failure_Exception() {
    when(restTemplate.exchange(
            eq(baseUrl + "oauth/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
        .thenThrow(new RuntimeException("API error"));

    assertThrows(RuntimeException.class, () -> auth0Client.getAccessToken());
  }
}
