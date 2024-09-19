package com.ingsis.jcli.permissions_service.controllers;

import com.ingsis.jcli.permissions_service.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("snippets/")
public class SnippetsController {

  private final HelloService helloService;

  @Autowired
  public SnippetsController(HelloService helloService) {
    this.helloService = helloService;
  }

  @GetMapping("hello")
  public String hello() {
    return helloService.getHello();
  }
}
