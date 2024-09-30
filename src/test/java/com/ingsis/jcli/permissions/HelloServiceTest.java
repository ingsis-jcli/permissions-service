package com.ingsis.jcli.permissions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.clients.PrintScriptClient;
import com.ingsis.jcli.permissions.clients.SnippetsClient;
import com.ingsis.jcli.permissions.services.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class HelloServiceTest {

  @Autowired
  private HelloService helloService;

  @MockBean
  private PrintScriptClient printScriptClient;

  @MockBean
  private SnippetsClient snippetsClient;

  @Test
  void testGetHello() {
    String result = helloService.getHello();
    assertEquals("Hello from permissions service!", result);
  }

  @Test
  void testGetHelloFromPrintScript() {
    when(printScriptClient.hello()).thenReturn("Hello from printscript service!");
    String result = helloService.getHelloFromPrintScript();
    assertEquals("Hello from printscript service!", result);
    verify(printScriptClient).hello();
  }

  @Test
  void testGetHelloFromSnippets() {
    when(snippetsClient.hello()).thenReturn("Hello from snippets service!");
    String result = helloService.getHelloFromSnippets();
    assertEquals("Hello from snippets service!", result);
    verify(snippetsClient).hello();
  }
}
