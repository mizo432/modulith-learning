package undecided.erp.shared.applicatoion;

import com.fasterxml.jackson.databind.util.StdDateFormat;
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
   * handlerExceptionnterceptorメソッドは、リクエストおよびレスポンスの処理中に
   * ログを記録するためのHandlerInterceptorを初期化し、Beanとして登録します。
   *
   * <p>主にTraceLoggingInterceptorを使用し、コントローラーハンドラーの実行時間 や必要なトレース情報をログに記録します。また、処理時間が長い場合には
   * WARNレベルのログを出力する機能を提供します。
   *
   * @return ログトレースを行うために構成されたHandlerInterceptorのインスタンス
   */
  @Bean
  public HandlerInterceptor handlerExceptionnterceptor() {
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
}
