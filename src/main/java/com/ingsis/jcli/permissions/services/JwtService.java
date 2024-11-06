package com.ingsis.jcli.permissions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtDecoder jwtDecoder;

  @Autowired
  public JwtService(JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  public Jwt extractJwt(String authHeader) {
    String jwt = authHeader.substring(7);
    return jwtDecoder.decode(jwt);
  }

  public String extractUserId(String authHeader) {
    Jwt jwt = extractJwt(authHeader);
    return jwt.getSubject();
  }

  public String extractEmail(String authHeader) {
    Jwt jwt = extractJwt(authHeader);
    return jwt.getClaim("https://snippets.com/email");
  }
}
