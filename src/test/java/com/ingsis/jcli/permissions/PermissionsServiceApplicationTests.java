package com.ingsis.jcli.permissions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PermissionsServiceApplicationTests {

  @BeforeAll
  static void setup() {
    PermissionsServiceApplication.loadEnv();
  }

  @Test
  void contextLoads() {
  }
}
