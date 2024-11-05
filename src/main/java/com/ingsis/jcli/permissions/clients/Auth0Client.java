package com.ingsis.jcli.permissions.clients;

import com.ingsis.jcli.permissions.dtos.UserDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    System.out.println("url: " + url);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("client_id", clientId);
    requestBody.add("client_secret", clientSecret);
    requestBody.add("audience", baseUrl + "api/v2/");
    requestBody.add("grant_type", "client_credentials");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

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

  public List<UserDto> getAllUsers(
      String adminAccessToken, String requestingUserId, int page, int pageSize) {
    String url = baseUrl + "users?page=" + page + "&per_page=" + pageSize;
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminAccessToken);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map[]> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

      if (response.getBody() != null) {
        List<UserDto> users = new ArrayList<>();
        for (Map user : response.getBody()) {
          String userId = (String) user.get("user_id");
          String email = (String) user.get("email");

          if (!userId.equals(requestingUserId)) {
            users.add(new UserDto(userId, email));
          }
        }
        return users;
      } else {
        throw new RuntimeException("No users found in the response");
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to get users from Auth0", e);
    }
  }
}
