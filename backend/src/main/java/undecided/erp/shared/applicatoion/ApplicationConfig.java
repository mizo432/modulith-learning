package undecided.erp.shared.applicatoion;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import undecided.shared.common.exception.ExceptionLogger;
import undecided.shared.common.exception.ResultMessagesLoggingInterceptor;
import undecided.shared.web.exception.ExceptionLoggingFilter;

@Configuration
public class ApplicationConfig {

  private static final String EXCEPTION_LOGGER_NAME = "EXCEPTION_LOGGER_NAME";

  @Bean
  public ExceptionLogger exceptionLogger() {
    return new ExceptionLogger(EXCEPTION_LOGGER_NAME);
  }

  /**
   * タスク専用の実行サービス(Executor)を提供するメソッド。 このメソッドで生成されたExecutorは仮想スレッド(Virtual Thread)を使用してタスクを並列実行します。
   *
   * @return 新しい仮想スレッドベースのタスク実行サービス
   */
  @Bean
  public Executor taskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }

  /**
   * exceptionLoggingFilterメソッドは、例外をキャッチしてログに記録するための {@link
   * ExceptionLoggingFilter}インスタンスを生成し、設定を行います。
   *
   * @param exceptionLogger ログ記録のために使用されるExceptionLoggerインスタンス
   * @return 例外ログ出力用に初期化されたExceptionLoggingFilterインスタンス
   */
  @Bean
  public ExceptionLoggingFilter exceptionLoggingFilter(ExceptionLogger exceptionLogger) {
    ExceptionLoggingFilter exceptionLoggingFilter = new ExceptionLoggingFilter();
    exceptionLoggingFilter.setExceptionLogger(exceptionLogger);

    return exceptionLoggingFilter;
  }

  /**
   * resultMessagesLoggingInterceptorメソッドは、{@link ResultMessagesLoggingInterceptor}のインスタンスを作成し、
   * 結果メッセージや例外に関するログ記録を行う機能を提供します。
   *
   * @return 結果メッセージおよび例外のログ記録機能を持つResultMessagesLoggingInterceptorインスタンス
   */
  @Bean
  public ResultMessagesLoggingInterceptor resultMessagesLoggingInterceptor() {
    return new ResultMessagesLoggingInterceptor();
  }

  /**
   * simpleMappingExceptionResolverメソッドは、例外処理を行うための {@link
   * SimpleMappingExceptionResolver}インスタンスを生成および提供します。
   *
   * <p>このクラスを使用すると、例外とビューのマッピングを簡単に構成でき、 特定の例外発生時に自動的に適切なビューをレンダリングすることが可能です。
   *
   * @return 例外とビューのマッピング機能を提供するSimpleMappingExceptionResolverインスタンス
   */
  @Bean
  public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
    return new SimpleMappingExceptionResolver();
  }
}
