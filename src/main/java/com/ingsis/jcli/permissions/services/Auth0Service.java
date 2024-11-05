package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.clients.Auth0Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Auth0Service {

  @Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.resourceserver.jwt.client-secret}")
  private String clientSecret;

  @Value("${auth0.audience}")
  private String audience;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String authServerUri;

  private final Auth0Client auth0Client;

  public Auth0Service() {
    this.auth0Client =
        new Auth0Client(new RestTemplate(), authServerUri, clientId, clientSecret, audience);
  }

  public String getAdminAccessToken() {
    return auth0Client.getAccessToken();
  }
}
