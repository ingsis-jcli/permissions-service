package com.ingsis.jcli.permissions;

import static com.ingsis.jcli.permissions.server.CorrelationIdFilter.CORRELATION_ID_HEADER;
import static com.ingsis.jcli.permissions.server.CorrelationIdFilter.CORRELATION_ID_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ingsis.jcli.permissions.config.FeignConfig;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.MDC;

public class FeignConfigTest {

  private FeignConfig feignConfig;
  private RequestTemplate requestTemplate;

  @BeforeEach
  public void setUp() {
    feignConfig = new FeignConfig();
    requestTemplate = new RequestTemplate();
  }

  @Test
  public void testCorrelationIdFeignInterceptorAddsHeaderWhenCorrelationIdExists() {
    String mockCorrelationId = "test-correlation-id";
    try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
      mdcMock.when(() -> MDC.get(CORRELATION_ID_KEY)).thenReturn(mockCorrelationId);

      RequestInterceptor interceptor = feignConfig.correlationIdFeignInterceptor();
      interceptor.apply(requestTemplate);

      assertTrue(requestTemplate.headers().containsKey(CORRELATION_ID_HEADER));
      assertEquals(
          mockCorrelationId,
          requestTemplate.headers().get(CORRELATION_ID_HEADER).iterator().next());
    }
  }

  @Test
  public void testCorrelationIdFeignInterceptorDoesNotAddHeaderWhenCorrelationIdIsNull() {
    try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
      mdcMock.when(() -> MDC.get(CORRELATION_ID_KEY)).thenReturn(null);

      RequestInterceptor interceptor = feignConfig.correlationIdFeignInterceptor();
      interceptor.apply(requestTemplate);

      assertTrue(!requestTemplate.headers().containsKey(CORRELATION_ID_HEADER));
    }
  }
}
