package undecided.erp.shared.applicatoion;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.tomcat.reactive.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import undecided.erp.common.web.logging.TraceLoggingInterceptor;

/** Spring MVCの設定を行うための構成クラス。 このクラスはSpring MVCのWeb設定や、カスタムビーンの登録を行い、 REST APIを構築する際の主要な設定を提供します。 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class SpringMvcRestConfig implements WebMvcConfigurer {

  private final Executor executor;

  public SpringMvcRestConfig(Executor executor) {
    this.executor = executor;
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
   * 例外およびリクエストのトレースログを記録するためのHandlerInterceptorを提供します。
   *
   * <p>このメソッドは、トレースログの記録機能を持つカスタムインターセプターである {@link TraceLoggingInterceptor} を生成して返します。このインターセプターは、
   * HTTPリクエストの処理開始から終了までの実行時間を測定し、ログに出力します。
   *
   * <p>また、実行時間があらかじめ設定された閾値を超えた場合には、警告ログとして 出力される機能も含まれています。
   *
   * @return 例外記録およびリクエスト処理時間測定用のHandlerInterceptor
   */
  @Bean
  public HandlerInterceptor handlerExceptionInterceptor() {
    return new TraceLoggingInterceptor();
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
  public TraceLoggingInterceptor traceLoggingInterceptor() {
    TraceLoggingInterceptor traceLoggingInterceptor = new TraceLoggingInterceptor();
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
