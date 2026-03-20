package undecided.erp.shared.applicatoion;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.concurrent.Executors;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.tomcat.reactive.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.shared.common.exception.ExceptionLogger;
import undecided.shared.web.exception.HandlerExceptionResolverLoggingInterceptor;
import undecided.shared.web.logging.RestTraceLoggingInterceptor;

/** Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {
  /**
   * HandlerExceptionResolverLoggingInterceptorを生成し、例外ログ設定を適用した後に返します。
   *
   * @param exceptionLogger 例外をログに記録するためのExceptionLoggerオブジェクト
   * @return 初期化されたHandlerExceptionResolverLoggingInterceptorのインスタンス
   */
  @Bean
  public HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor(
      ExceptionLogger exceptionLogger) {
    HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor =
        new HandlerExceptionResolverLoggingInterceptor();
    handlerExceptionResolverLoggingInterceptor.setExceptionLogger(exceptionLogger);
    return handlerExceptionResolverLoggingInterceptor;
  }

  /**
   * HandlerExceptionResolverLoggingInterceptorを利用して、例外解決時のロギングを行う アドバイザーを生成します。
   *
   * @param interceptor HandlerExceptionResolverLoggingInterceptorインスタンス。 例外処理時のロギング機能を提供します。
   * @return ExceptionResolverLoggingInterceptorを適用するAdvisorインスタンス。
   */
  @Bean
  public Advisor exceptionResolverLoggingInterceptorAdvisor(
      HandlerExceptionResolverLoggingInterceptor interceptor) {
    JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
    pointcut.setPattern("undecided.erp..internal.*Api.*");
    return new DefaultPointcutAdvisor(pointcut, interceptor);
  }

  /**
   * Spring MVCにおけるインターセプターを追加するためのメソッド。
   *
   * <p>このメソッドでは、InterceptorRegistryを使用して、 必要なインターセプターを登録します。 主にリクエストのトレースログを記録するための
   * TraceLoggingInterceptorが追加されます。
   *
   * @param registry インターセプターを登録するためのInterceptorRegistryオブジェクト
   */
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor());
  }

  /**
   * 標準的な日付フォーマットオブジェクトを生成して返します。
   *
   * <p>このメソッドで生成されるStdDateFormatは、日付および時刻を文字列に 変換する際に使用されます。
   *
   * @return 新しいStdDateFormatインスタンス
   */
  public StdDateFormat stdDateFormat() {
    return new StdDateFormat();
  }

  /**
   * PageableHandlerMethodArgumentResolverを生成し、返します。
   *
   * <p>このメソッドで提供されるPageableHandlerMethodArgumentResolverは、 Spring
   * MVCでページング機能を使用するためのリクエストパラメータ解析を行う役割を持ちます。
   *
   * @return PageableHandlerMethodArgumentResolverの新しいインスタンス
   */
  @Bean
  public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  /**
   * TraceLoggingInterceptorを生成し、指定の設定を適用して返します。
   *
   * <p>このメソッドで構築されたTraceLoggingInterceptorは、リクエストの処理時間を計測し、
   * ログに記録する機能を提供します。また、処理時間が指定の閾値（デフォルトで3秒）を 超えた場合に警告ログを出力します。
   *
   * @return 初期化されたTraceLoggingInterceptorのインスタンス
   */
  @Bean
  public RestTraceLoggingInterceptor traceLoggingInterceptor() {
    RestTraceLoggingInterceptor traceLoggingInterceptor = new RestTraceLoggingInterceptor();
    traceLoggingInterceptor.setWarnHandlingNanos(3000000000L);
    return traceLoggingInterceptor;
  }

  /**
   * TomcatのリアクティブWebサーバーをカスタマイズするためのメソッド。
   *
   * <p>このメソッドは、Tomcatのコネクター設定を変更し、仮想スレッド（Virtual Thread）を使用する
   * Executorをプロトコルハンドラーに設定します。これにより、スレッド管理を効率化します。
   *
   * @return TomcatReactiveWebServerFactoryのカスタマイズを行うためのWebServerFactoryCustomizerオブジェクト
   */
  @Bean
  public WebServerFactoryCustomizer<TomcatReactiveWebServerFactory> tomCatCustomizeer() {
    return factory ->
        factory.addConnectorCustomizers(
            connector ->
                connector
                    .getProtocolHandler()
                    .setExecutor(Executors.newVirtualThreadPerTaskExecutor()));
  }
}
