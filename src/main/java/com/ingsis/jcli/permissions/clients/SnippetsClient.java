package com.ingsis.jcli.permissions.clients;

import com.ingsis.jcli.permissions.common.responses.SnippetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "snippets", url = "http://infra-snippets-api:8080")
public interface SnippetsClient {

  @RequestMapping(method = RequestMethod.GET, value = "/hello")
  String hello();

  @RequestMapping(method = RequestMethod.GET, value = "/snippet")
  ResponseEntity<SnippetResponse> getSnippet(@RequestParam("snippetId") Long snippetId);
}
