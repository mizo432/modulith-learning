package undecided.erp.shared.presentation.web;

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
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/** Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
// (7)
// @ComponentScan("com.example.project.api") // (6)
// @EnableWebMvc
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {
  /**
   * TraceLoggingInterceptorのインスタンスを生成し、Spring MVCのHandlerInterceptorとして使用可能にします。
   * このInterceptorはリクエストをトレースしてログ出力を行い、リクエスト処理の追跡やエラーハンドリングに役立てます。
   *
   * @return HandlerInterceptorとして利用可能なTraceLoggingInterceptorの新しいインスタンス
   */
  @Bean
  public HandlerInterceptor handlerExceptionnterceptor() {
    return new TraceLoggingInterceptor();
  }

  /**
   * 標準の日付フォーマットを提供するStdDateFormatインスタンスを作成します。 このメソッドは、日付のシリアライズ/デシリアライズ処理を統一された形式で設定するために使用されます。
   *
   * @return 標準の日付フォーマットを表すStdDateFormatインスタンス
   */
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
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
   * JSONメッセージコンバータを作成します。このメソッドは、指定されたObjectMapperを使用して
   * MappingJackson2HttpMessageConverterを設定します。これにより、HTTPリクエスト/レスポンスの
   * JSONのシリアライズ/デシリアライズ処理がカスタマイズされます。
   *
   * @param objectMapper JSONのシリアライズ/デシリアライズ処理をカスタマイズするためのObjectMapperのインスタンス
   * @return 設定済みのMappingJackson2HttpMessageConverterインスタンス
   */
  @Bean("jsonMessageConverter")
  public MappingJackson2HttpMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
    bean.setObjectMapper(objectMapper);
    return bean;
  }

  /**
   * メッセージコンバータを設定するメソッドです。
   *
   * <p>このメソッドでは、HTTPリクエストやレスポンスのJSONのシリアライズ/デシリアライズ処理を 行うためのカスタムメッセージコンバータを追加します。
   *
   * @param converters HTTPメッセージコンバータを保持するリスト
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jsonMessageConverter(objectMapper()));
  }

  /**
   * コントローラメソッドで使用される引数リゾルバを追加します。 このメソッドを使用することで、Spring
   * MVCにカスタムのHandlerMethodArgumentResolverを登録できます。
   *
   * @param argumentResolvers HandlerMethodArgumentResolverを保持するリスト
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(pageableHandlerMethodArgumentResolver());
  }

  /**
   * PageableHandlerMethodArgumentResolverのインスタンスを作成し、Spring MVCで使用するための {@link
   * HandlerMethodArgumentResolver}として設定します。このメソッドは、 ページネーションをサポートするデフォルトの設定を提供します。
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
}
