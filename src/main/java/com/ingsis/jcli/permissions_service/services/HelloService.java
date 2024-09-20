package com.ingsis.jcli.permissions_service.services;

import com.ingsis.jcli.permissions_service.clients.PrintScriptClient;
import com.ingsis.jcli.permissions_service.clients.SnippetsClient;
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
}
