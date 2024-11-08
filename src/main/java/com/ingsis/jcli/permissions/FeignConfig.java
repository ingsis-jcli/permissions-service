package com.ingsis.jcli.permissions;

import static com.ingsis.jcli.permissions.CorrelationIdFilter.CORRELATION_ID_HEADER;
import static com.ingsis.jcli.permissions.CorrelationIdFilter.CORRELATION_ID_KEY;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  public RequestInterceptor correlationIdFeignInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate requestTemplate) {
        String correlationId = MDC.get(CORRELATION_ID_KEY);
        if (correlationId != null) {
          requestTemplate.header(CORRELATION_ID_HEADER, correlationId);
        }
      }
    };
  }
}
