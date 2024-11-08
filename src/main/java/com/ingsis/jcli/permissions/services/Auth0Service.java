package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.clients.Auth0Client;
import com.ingsis.jcli.permissions.dtos.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public List<UserDto> getAllUsers(String requestingUserId, int page, int pageSize, Optional<String> name) {
    String adminAccessToken = getAdminAccessToken();
    List<UserDto> allUsers = auth0Client.getAllUsers(adminAccessToken, requestingUserId);

    List<UserDto> filteredUsers = allUsers.stream()
      .filter(user -> !user.getId().equals(requestingUserId))
      .filter(user -> name.map(n -> user.getEmail().toLowerCase().contains(n.toLowerCase())).orElse(true))
      .collect(Collectors.toList());

    if (page < 0) {
      page = 0;
    }

    int fromIndex = page * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, filteredUsers.size());

    if (fromIndex >= filteredUsers.size()) {
      return Collections.emptyList();
    }

    return filteredUsers.subList(fromIndex, toIndex);
  }

  public int getUserCount() {
    String adminAccessToken = getAdminAccessToken();
    return auth0Client.getTotalUserCount(adminAccessToken);
  }
}
