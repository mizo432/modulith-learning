package undecided.erp.common.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/**
 * Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
// (7)
//@ComponentScan("com.example.project.api") // (6)
//@EnableWebMvc
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {

  /**
   * JSONメッセージコンバータを作成します。このメソッドは、指定されたObjectMapperを使用して
   * MappingJackson2HttpMessageConverterを設定します。これにより、HTTPリクエスト/レスポンスの
   * JSONのシリアライズ/デシリアライズ処理がカスタマイズされます。
   *
   * @param objectMapper JSONのシリアライズ/デシリアライズ処理をカスタマイズするためのObjectMapperのインスタンス
   * @return 設定済みのMappingJackson2HttpMessageConverterインスタンス
   */
  @Bean("jsonMessageConverter")
  public MappingJackson2HttpMessageConverter jsonMessageConverter(
      ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
    bean.setObjectMapper(objectMapper);
    return bean;
  }

  /**
   * ObjectMapperを生成するためのメソッドです。
   * Jackson2ObjectMapperFactoryBeanを使用して、JSONのシリアライズ/デシリアライズ処理をカスタマイズします。 標準の日付フォーマットが適用されます。
   *
   * @return 設定済みのObjectMapperインスタンス
   */
  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
    bean.setDateFormat(stdDateFormat()); // (2)
    bean.afterPropertiesSet();
    return bean.getObject();
  }

  /**
   * 標準の日付フォーマットを提供するStdDateFormatインスタンスを作成します。 このメソッドは、日付のシリアライズ/デシリアライズ処理を統一された形式で設定するために使用されます。
   *
   * @return 標準の日付フォーマットを表すStdDateFormatインスタンス
   */
  @Bean
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
  }

  /**
   * メッセージコンバータを設定するメソッドです。
   * <p>
   * このメソッドでは、HTTPリクエストやレスポンスのJSONのシリアライズ/デシリアライズ処理を 行うためのカスタムメッセージコンバータを追加します。
   *
   * @param converters HTTPメッセージコンバータを保持するリスト
   */
  @Override
  public void configureMessageConverters(
      List<HttpMessageConverter<?>> converters) {
    converters.add(jsonMessageConverter(objectMapper()));
  }

  /**
   * コントローラメソッドで使用される引数リゾルバを追加します。 このメソッドを使用することで、Spring
   * MVCにカスタムのHandlerMethodArgumentResolverを登録できます。
   *
   * @param argumentResolvers HandlerMethodArgumentResolverを保持するリスト
   */
  @Override
  public void addArgumentResolvers(
      List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(pageableHandlerMethodArgumentResolver());
  }

  /**
   * PageableHandlerMethodArgumentResolverのインスタンスを作成し、Spring MVCで使用するための
   * {@link HandlerMethodArgumentResolver}として設定します。このメソッドは、 ページネーションをサポートするデフォルトの設定を提供します。
   *
   * @return PageableHandlerMethodArgumentResolverのインスタンス
   */
  @Bean
  public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  /**
   * InterceptorをSpring MVC設定に追加するメソッドです。 指定されたInterceptorRegistryにTraceLoggingInterceptorを登録し、
   * 全てのパスパターンに適用します。
   *
   * @param registry Interceptorを登録するためのInterceptorRegistryインスタンス
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor()).addPathPatterns("/**");

  }

  /**
   * TraceLoggingInterceptorのインスタンスを生成し、Spring MVCのInterceptorとして利用可能にします。
   * このInterceptorはリクエストの処理時間をトレースし、必要に応じてログ出力を行います。
   *
   * @return TraceLoggingInterceptorの新しいインスタンス
   */
  @Bean
  public TraceLoggingInterceptor traceLoggingInterceptor() {
    TraceLoggingInterceptor traceLoggingInterceptor = new TraceLoggingInterceptor();
    traceLoggingInterceptor.setWarnHandlingNanos(3000000000L);
    return traceLoggingInterceptor;

  }

  /**
   * HandlerExceptionResolverLoggingInterceptorを生成し、ExceptionLoggerを設定します。
   * このInterceptorは例外解決プロセスにログ機能を追加するために使用されます。
   *
   * @param exceptionLogger 例外発生時に記録を行うExceptionLoggerのインスタンス
   * @return 設定済みのHandlerExceptionResolverLoggingInterceptorインスタンス
   */
  // @Bean("handlerExceptionResolverLoggingInterceptor")
  /* public HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor(
      ExceptionLogger exceptionLogger) {
    HandlerExceptionResolverLoggingInterceptor bean = new HandlerExceptionResolverLoggingInterceptor();
    bean.setExceptionLogger(exceptionLogger);
    return bean;
  }*/

  /**
   * HandlerExceptionResolverLoggingInterceptorを使用して、例外発生時のログ処理を実装するための
   * Advisorを設定します。このAdvisorは、HandlerExceptionResolverのresolveExceptionメソッドの
   * 実行時にログ処理を行うためのポイントカットとインターセプターを組み合わせます。
   *
   * @param handlerExceptionResolverLoggingInterceptor 例外解決プロセス中にログ処理を行う
   * HandlerExceptionResolverLoggingInterceptorのインスタンス
   * @return 例外解決プロセスにログ処理を追加するために使用できるAdvisorのインスタンス
   */
  /* @Bean
  public Advisor handlerExceptionResolverLoggingInterceptorAdvisor(
      HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(
        "execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))");
    return new DefaultPointcutAdvisor(pointcut, handlerExceptionResolverLoggingInterceptor);
  }*/
}
