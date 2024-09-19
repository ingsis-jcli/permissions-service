package com.ingsis.jcli.permissions_service.services;

import com.ingsis.jcli.permissions_service.clients.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  private final HelloClient helloClient;

  @Autowired
  public HelloService(HelloClient helloClient) {
    this.helloClient = helloClient;
  }

  public String getHello() {
    return helloClient.hello();
  }
}
