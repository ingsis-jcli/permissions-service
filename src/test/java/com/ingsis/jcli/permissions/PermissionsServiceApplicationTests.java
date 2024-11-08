package com.ingsis.jcli.permissions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PermissionsServiceApplicationTests {
  @MockBean private JwtDecoder jwtDecoder;

  @Test
  void contextLoads() {}
}
