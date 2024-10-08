package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.clients.PrintScriptClient;
import com.ingsis.jcli.permissions.clients.SnippetsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  private final SnippetsClient snippetsClient;
  private final PrintScriptClient printScriptClient;

  @Autowired
  public HelloService(SnippetsClient snippetsClient, PrintScriptClient printScriptClient) {
    this.snippetsClient = snippetsClient;
    this.printScriptClient = printScriptClient;
  }

  public String getHelloFromSnippets() {
    return snippetsClient.hello();
  }

  public String getHelloFromPrintScript() {
    return printScriptClient.hello();
  }

  public String getHello() {
    return "Hello from permissions service!";
  }
}
