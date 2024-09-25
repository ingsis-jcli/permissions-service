package com.ingsis.jcli.permissions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ingsis.jcli.permissions.controllers.HelloController;
import com.ingsis.jcli.permissions.services.HelloService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

  @BeforeAll
  static void setup() {
    PermissionsServiceApplication.loadEnv();
  }

  @Autowired private MockMvc mockMvc;

  @MockBean private HelloService helloService;

  @Test
  void testGetHello() throws Exception {
    when(helloService.getHello()).thenReturn("Hello from permissions service!");
    mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from permissions service!"));
  }

  @Test
  void testGetHelloFromPrintScript() throws Exception {
    when(helloService.getHelloFromPrintScript()).thenReturn("Hello from printscript service!");
    mockMvc
        .perform(get("/hello/printscript"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from printscript service!"));
  }

  @Test
  void testGetHelloFromSnippets() throws Exception {
    when(helloService.getHelloFromSnippets()).thenReturn("Hello from snippets service!");
    mockMvc
        .perform(get("/hello/snippets"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from snippets service!"));
  }
}
