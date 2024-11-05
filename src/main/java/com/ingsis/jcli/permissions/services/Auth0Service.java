package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.clients.Auth0Client;
import com.ingsis.jcli.permissions.dtos.UserDto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Auth0Service {

  private final Auth0Client auth0Client;

  public Auth0Service(
      @Value("${auth0.management.client-id}") String clientId,
      @Value("${auth0.management.client-secret}") String clientSecret,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String authServerUri) {
    this.auth0Client = new Auth0Client(new RestTemplate(), authServerUri, clientId, clientSecret);
  }

  public String getAdminAccessToken() {
    return auth0Client.getAccessToken();
  }

  public List<UserDto> getAllUsers(String requestingUserId, int page, int pageSize) {
    String adminAccessToken = getAdminAccessToken();
    return auth0Client.getAllUsers(adminAccessToken, requestingUserId, page, pageSize);
  }

  public Optional<String> getUserEmail(String userId) {
    String adminAccessToken = getAdminAccessToken();
    return auth0Client.getUserEmail(adminAccessToken, userId);
  }

  public boolean userExists(String userId) {
    String adminAccessToken = getAdminAccessToken();
    return auth0Client.getUserEmail(adminAccessToken, userId).isPresent();
  }
}
