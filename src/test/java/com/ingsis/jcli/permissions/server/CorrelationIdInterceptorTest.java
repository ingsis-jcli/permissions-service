package com.ingsis.jcli.permissions.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.CorrelationIdInterceptor;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

class CorrelationIdInterceptorTest {

  @Mock private HttpRequest httpRequest;
  @Mock private ClientHttpRequestExecution execution;
  @Mock private ClientHttpResponse response;
  private final byte[] body = new byte[0];

  @InjectMocks private CorrelationIdInterceptor interceptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void intercept_addsCorrelationIdHeader_whenCorrelationIdIsPresent() throws IOException {
    String correlationId = "test-correlation-id";
    MDC.put("correlation-id", correlationId);

    HttpHeaders headers = new HttpHeaders();
    when(httpRequest.getHeaders()).thenReturn(headers);
    when(execution.execute(httpRequest, body)).thenReturn(response);

    interceptor.intercept(httpRequest, body, execution);

    assertEquals(correlationId, headers.getFirst("X-Correlation-Id"));

    MDC.clear();
  }
}
