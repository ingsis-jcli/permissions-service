package com.ingsis.jcli.permissions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "printscript-api", url = "http://printscript-api:8082")
public interface PrintScriptClient {

  @RequestMapping(method = RequestMethod.GET, value = "/hello")
  String hello();
}
