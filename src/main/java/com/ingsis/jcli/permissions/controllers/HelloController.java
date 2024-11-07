package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.services.Auth0Service;
import com.ingsis.jcli.permissions.services.HelloService;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("hello")
public class HelloController {

  private final HelloService helloService;
  private final Auth0Service auth0Service;

  @Autowired
  public HelloController(HelloService helloService, Auth0Service auth0Service) {
    this.helloService = helloService;
    this.auth0Service = auth0Service;
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
    log.info("HelloController.hello");
    return helloService.getHello();
  }
}
