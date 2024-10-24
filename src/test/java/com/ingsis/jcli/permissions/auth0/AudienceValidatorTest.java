package com.ingsis.jcli.permissions.auth0;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AudienceValidatorTest {

  private static final String VALID_AUDIENCE = "valid-audience";
  private AudienceValidator audienceValidator;

  @BeforeEach
  void setUp() {
    audienceValidator = new AudienceValidator(VALID_AUDIENCE);
  }

  @Test
  void testValidateSuccess() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getAudience()).thenReturn(List.of(VALID_AUDIENCE));

    OAuth2TokenValidatorResult result = audienceValidator.validate(jwt);

    assertEquals(OAuth2TokenValidatorResult.success(), result);
  }

  @Test
  void testValidateFailureWhenAudienceIsMissing() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getAudience()).thenReturn(List.of("other-audience"));

    OAuth2TokenValidatorResult result = audienceValidator.validate(jwt);

    OAuth2Error expectedError =
        new OAuth2Error("invalid_token", "The required audience is missing", null);
    assertEquals(
        expectedError.getDescription(), result.getErrors().iterator().next().getDescription());
    assertEquals(expectedError.getErrorCode(), result.getErrors().iterator().next().getErrorCode());
  }

  @Test
  void testValidateFailureWhenAudienceIsNull() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getAudience()).thenReturn(null);

    OAuth2TokenValidatorResult result = audienceValidator.validate(jwt);

    OAuth2Error expectedError =
        new OAuth2Error("invalid_token", "The required audience is missing", null);
    assertEquals(
        expectedError.getDescription(), result.getErrors().iterator().next().getDescription());
    assertEquals(expectedError.getErrorCode(), result.getErrors().iterator().next().getErrorCode());
  }
}
