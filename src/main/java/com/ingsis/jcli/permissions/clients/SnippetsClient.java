package com.ingsis.jcli.permissions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "snippets", url = "http://snippets-service:8080/")
public interface SnippetsClient {

  @RequestMapping(method = RequestMethod.GET, value = "/hello")
  String hello();
}
