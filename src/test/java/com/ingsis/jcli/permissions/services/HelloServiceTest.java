package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.clients.PrintScriptClient;
import com.ingsis.jcli.permissions.clients.SnippetsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class HelloServiceTest {

  @Mock private PrintScriptClient printScriptClient;

  @Mock private SnippetsClient snippetsClient;

  @InjectMocks private HelloService helloService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

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
