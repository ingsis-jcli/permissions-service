package com.ingsis.jcli.permissions.config;

import com.ingsis.jcli.permissions.Generated;
import com.ingsis.jcli.permissions.server.CorrelationIdInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Generated
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new CorrelationIdInterceptor());
    restTemplate.setInterceptors(interceptors);

    return restTemplate;
  }
}
