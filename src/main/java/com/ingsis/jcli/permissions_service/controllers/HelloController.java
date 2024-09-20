package com.ingsis.jcli.permissions_service.controllers;

import com.ingsis.jcli.permissions_service.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

  private final HelloService helloService;

  @Autowired
  public HelloController(HelloService helloService) {
    this.helloService = helloService;
  }

  @GetMapping("/snippets")
  public String helloSnippets() {
    return helloService.getHelloFromSnippets();
  }

  @GetMapping("/printscript")
  public String helloPrintScript() {
    return helloService.getHelloFromPrintScript();
  }

  @GetMapping
  public String hello() {
    return "Hello from permissions service!";
  }
}
