package com.ingsis.jcli.permissions;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PermissionsServiceApplication {
  public static void main(String[] args) {
    loadEnv();
    SpringApplication.run(PermissionsServiceApplication.class, args);
  }

  public static void loadEnv() {
    Dotenv dotenv = Dotenv.load();
    System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
    System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
    System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
    System.setProperty("POSTGRES_PORT", dotenv.get("POSTGRES_PORT"));
    System.setProperty("PORT", dotenv.get("PORT"));
  }
}
