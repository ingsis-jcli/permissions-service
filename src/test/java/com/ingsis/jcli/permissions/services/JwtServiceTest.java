package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

class JwtServiceTest {

  private JwtService jwtService;
  private JwtDecoder jwtDecoder;

  @BeforeEach
  void setUp() {
    jwtDecoder = Mockito.mock(JwtDecoder.class);
    jwtService = new JwtService(jwtDecoder);
  }

  @Test
  void testExtractJwt() {
    String authHeader = "Bearer mock-token";
    Jwt expectedJwt = createMockJwt("123456-user-id");
    when(jwtDecoder.decode(anyString())).thenReturn(expectedJwt);
    Jwt actualJwt = jwtService.extractJwt(authHeader);
    assertEquals(expectedJwt, actualJwt, "Extracted JWT should match the mock JWT");
  }

  @Test
  void testExtractUserId() {
    String authHeader = "Bearer mock-token";
    Jwt mockJwt = createMockJwt("123456user-id");
    when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
    String actualUserId = jwtService.extractUserId(authHeader);
    assertEquals("user-id", actualUserId, "Extracted user ID should match expected value");
  }

  private Jwt createMockJwt(String subject) {
    return Jwt.withTokenValue("mock-token")
        .header("alg", "none")
        .subject(subject)
        .claim("scope", "read")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600))
        .build();
  }
}
