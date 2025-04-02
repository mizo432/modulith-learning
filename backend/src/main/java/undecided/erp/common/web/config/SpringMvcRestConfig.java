package undecided.erp.common.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.List;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.exception.ExceptionLogger;
import undecided.erp.common.web.exception.HandlerExceptionResolverLoggingInterceptor;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/**
 * Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。
 */
//@EnableAspectJAutoProxy(proxyTargetClass = true)
// (7)
//@ComponentScan("com.example.project.api") // (6)
//@EnableWebMvc
//@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {

  /**
   * Creates a JSON message converter for HTTP responses.
   *
   * @param objectMapper The ObjectMapper to use for JSON conversion
   * @return A configured MappingJackson2HttpMessageConverter
   */
  @Bean("jsonMessageConverter")
  public MappingJackson2HttpMessageConverter jsonMessageConverter(
      ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
    bean.setObjectMapper(objectMapper);
    return bean;
  }

  /**
   * Creates and configures an ObjectMapper for JSON serialization/deserialization. The ObjectMapper
   * is configured with a standard date format.
   *
   * @return A configured ObjectMapper
   */
  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
    bean.setDateFormat(stdDateFormat()); // (2)
    bean.afterPropertiesSet();
    return bean.getObject();
  }

  /**
   * Creates a standard date format for JSON date serialization/deserialization.
   *
   * @return A StdDateFormat instance
   */
  @Bean
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
  }

  /**
   * Configures the HTTP message converters for the application. Adds a JSON message converter with
   * the configured ObjectMapper.
   *
   * @param converters The list of converters to be configured
   */
  @Override
  public void configureMessageConverters(
      List<HttpMessageConverter<?>> converters) {
    converters.add(jsonMessageConverter(objectMapper()));
  }

  /**
   * Configures the argument resolvers for controller methods. Adds a pageable handler method
   * argument resolver for pagination support.
   *
   * @param argumentResolvers The list of argument resolvers to be configured
   */
  @Override
  public void addArgumentResolvers(
      List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(pageableHandlerMethodArgumentResolver());
  }

  /**
   * Creates a PageableHandlerMethodArgumentResolver for handling pageable parameters in controller
   * methods. This resolver enables automatic conversion of pagination parameters from request
   * parameters.
   *
   * @return A PageableHandlerMethodArgumentResolver instance
   */
  @Bean
  public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  /**
   * Configures the interceptors for HTTP requests. Adds a trace logging interceptor for all request
   * paths.
   *
   * @param registry The interceptor registry to be configured
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor()).addPathPatterns("/**");
  }

  /**
   * Creates a TraceLoggingInterceptor for logging HTTP request and response information. This
   * interceptor logs details about incoming requests and outgoing responses for tracing purposes.
   *
   * @return A TraceLoggingInterceptor instance
   */
  @Bean
  public TraceLoggingInterceptor traceLoggingInterceptor() {
    return new TraceLoggingInterceptor();
  }

  @Bean("handlerExceptionResolverLoggingInterceptor")
  public HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor(
      ExceptionLogger exceptionLogger) {
    HandlerExceptionResolverLoggingInterceptor bean = new HandlerExceptionResolverLoggingInterceptor();
    bean.setExceptionLogger(exceptionLogger);
    return bean;
  }

  /**
   * Creates an advisor for the HandlerExceptionResolverLoggingInterceptor. This advisor intercepts
   * exception resolution in Spring MVC and applies logging. It targets the resolveException method
   * of HandlerExceptionResolver.
   *
   * @param handlerExceptionResolverLoggingInterceptor The interceptor to be applied
   * @return An advisor that applies the interceptor to the specified pointcut
   */
  @Bean
  public Advisor handlerExceptionResolverLoggingInterceptorAdvisor(
      HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(
        "execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))");
    return new DefaultPointcutAdvisor(pointcut, handlerExceptionResolverLoggingInterceptor);
  }
}
