package com.ingsis.jcli.permissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestLogFilter implements WebFilter {

  private static final Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String uri = exchange.getRequest().getURI().toString();
    String method = exchange.getRequest().getMethod().toString();
    String requestId = MDC.get("request-id");

    logger.info("Request - ID: {}, Method: {}, URI: {}", requestId, method, uri);

    return chain.filter(exchange).doOnTerminate(() -> {
      Integer statusCode = exchange.getResponse().getStatusCode() != null ?
          exchange.getResponse().getStatusCode().value() : null;
      logger.info("Response - ID: {}, Status: {}", requestId, statusCode);
    });
  }
}
