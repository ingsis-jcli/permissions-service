package com.ingsis.jcli.permissions.controllers;

import com.ingsis.jcli.permissions.services.Auth0Service;
import com.ingsis.jcli.permissions.services.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("hello")
public class HelloController {

  private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

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
    logger.info("Endpoint /hello/snippets called");
    return helloService.getHello();
  }
}
