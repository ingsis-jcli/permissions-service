package com.ingsis.jcli.permissions.clients;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Auth0Client {
  private final RestTemplate restTemplate;
  private final String baseUrl;
  private final String clientId;
  private final String clientSecret;
  private final String audience;

  public Auth0Client(
      RestTemplate restTemplate,
      String baseUrl,
      String clientId,
      String clientSecret,
      String audience) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.audience = audience;
  }

  public String getAccessToken() {
    String url = baseUrl + "oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("client_id", clientId);
    requestBody.put("client_secret", clientSecret);
    requestBody.put("audience", audience);
    requestBody.put("grant_type", "client_credentials");
    HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

      if (response.getBody() != null && response.getBody().containsKey("access_token")) {
        return response.getBody().get("access_token").toString();
      } else {
        throw new RuntimeException("Access token not found in the response");
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to get Auth0 access token", e);
    }
  }
}
