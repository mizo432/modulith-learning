package undecided.erp.common.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.List;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.web.exception.HandlerExceptionResolverLoggingInterceptor;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

@EnableAspectJAutoProxy(proxyTargetClass = true)
// (7)
//@ComponentScan("com.example.project.api") // (6)
@EnableWebMvc
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {

  @Bean("jsonMessageConverter")
  public MappingJackson2HttpMessageConverter jsonMessageConverter(
      ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
    bean.setObjectMapper(objectMapper);
    return bean;
  }

  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
    bean.setDateFormat(stdDateFormat()); // (2)
    bean.afterPropertiesSet();
    return bean.getObject();
  }

  @Bean
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
  }

  @Override
  public void configureMessageConverters(
      List<HttpMessageConverter<?>> converters) {
    converters.add(jsonMessageConverter(objectMapper()));
  }

  // (4)
  @Override
  public void addArgumentResolvers(
      List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(pageableHandlerMethodArgumentResolver());
  }

  @Bean
  public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  // (5)
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor()).addPathPatterns("/**");
  }

  @Bean
  public TraceLoggingInterceptor traceLoggingInterceptor() {
    return new TraceLoggingInterceptor();
  }


  // (7)
  @Bean
  public Advisor handlerExceptionResolverLoggingInterceptorAdvisor(
      HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(
        "execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))");
    return new DefaultPointcutAdvisor(pointcut, handlerExceptionResolverLoggingInterceptor);
  }
}
