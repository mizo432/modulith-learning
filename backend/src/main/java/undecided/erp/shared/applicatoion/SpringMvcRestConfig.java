package undecided.erp.shared.applicatoion;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/** Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {
  /**
   * Registers MVC interceptors on the given registry by adding the TraceLoggingInterceptor.
   *
   * @param registry the InterceptorRegistry to configure with application interceptors
   */
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor());
  }

  /**
   * Create a TraceLoggingInterceptor for use as a Spring MVC HandlerInterceptor.
   *
   * The interceptor traces incoming requests and records processing and error-related logs to aid request tracking.
   *
   * @return a new TraceLoggingInterceptor instance suitable as a HandlerInterceptor
   */
  @Bean
  public HandlerInterceptor handlerExceptionnterceptor() {
    return new TraceLoggingInterceptor();
  }

  /**
   * Provides a standard date format for consistent date serialization and deserialization.
   *
   * @return a StdDateFormat instance for standardized date parsing and formatting
   */
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
  }

  /**
   * Provides a pageable argument resolver for controller methods to enable pagination support.
   *
   * @return a PageableHandlerMethodArgumentResolver configured with default pagination settings
   */
  @Bean
  public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  /**
   * Creates and configures a TraceLoggingInterceptor for use as a Spring MVC interceptor.
   *
   * Configures the interceptor to warn when request handling exceeds 3,000,000,000 nanoseconds.
   *
   * @return a configured TraceLoggingInterceptor instance
   */
  @Bean
  public TraceLoggingInterceptor traceLoggingInterceptor() {
    TraceLoggingInterceptor traceLoggingInterceptor = new TraceLoggingInterceptor();
    traceLoggingInterceptor.setWarnHandlingNanos(3000000000L);
    return traceLoggingInterceptor;
  }
}