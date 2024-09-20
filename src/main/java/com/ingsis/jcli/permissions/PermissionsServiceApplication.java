package com.ingsis.jcli.permissions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PermissionsServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(PermissionsServiceApplication.class, args);
  }
}
