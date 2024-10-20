package com.ingsis.jcli.permissions;

import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.jwt.Jwt.withTokenValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ingsis.jcli.permissions.services.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HelloControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private HelloService helloService;

  @MockBean private JwtDecoder jwtDecoder;

  @Test
  void testGetHello() throws Exception {
    when(helloService.getHello()).thenReturn("Hello from permissions service!");
    mockMvc
        .perform(get("/hello").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from permissions service!"));
  }

  @Test
  void testGetHelloFromPrintScript() throws Exception {
    when(helloService.getHelloFromPrintScript()).thenReturn("Hello from printscript service!");
    Jwt mockJwt =
        withTokenValue("mock-jwt-token")
            .header("alg", "none")
            .claim("scope", "read:snippets")
            .build();
    mockMvc
        .perform(
            get("/hello/printscript").with(SecurityMockMvcRequestPostProcessors.jwt().jwt(mockJwt)))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from printscript service!"));
  }

  @Test
  void testGetHelloFromSnippets() throws Exception {
    when(helloService.getHelloFromSnippets()).thenReturn("Hello from snippets service!");
    Jwt mockJwt =
        withTokenValue("mock-jwt-token")
            .header("alg", "none")
            .claim("scope", "read:snippets")
            .build();
    mockMvc
        .perform(
            get("/hello/snippets").with(SecurityMockMvcRequestPostProcessors.jwt().jwt(mockJwt)))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from snippets service!"));
  }
}
