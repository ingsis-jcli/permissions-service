package com.ingsis.jcli.permissions.auth0;

import static com.ingsis.jcli.permissions.server.CorrelationIdFilter.CORRELATION_ID_HEADER;
import static com.ingsis.jcli.permissions.server.CorrelationIdFilter.CORRELATION_ID_KEY;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {
    final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes != null) {
      final HttpServletRequest httpServletRequest =
          ((ServletRequestAttributes) requestAttributes).getRequest();
      template.header(
          HttpHeaders.AUTHORIZATION, httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
    }
    String correlationId = MDC.get(CORRELATION_ID_KEY);
    if (correlationId != null) {
      template.header(CORRELATION_ID_HEADER, correlationId);
    }
  }
}
