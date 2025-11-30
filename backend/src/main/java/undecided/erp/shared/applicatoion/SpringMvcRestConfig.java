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
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(traceLoggingInterceptor());
  }

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
