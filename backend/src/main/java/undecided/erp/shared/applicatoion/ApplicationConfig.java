package undecided.erp.shared.applicatoion;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import undecided.erp.common.exception.ExceptionLogger;
import undecided.erp.common.exception.ResultMessagesLoggingInterceptor;
import undecided.erp.common.web.exception.ExceptionLoggingFilter;

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

  @Bean
  public ResultMessagesLoggingInterceptor resultMessagesLoggingInterceptor() {
    return new ResultMessagesLoggingInterceptor();
  }

  // @Bean
  // HandlerExceptionResolverLoggingInterceptor handlerExceptionResolverLoggingInterceptor() {
  //  return new HandlerExceptionResolverLoggingInterceptor();
  // }

  @Bean
  public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
    return new SimpleMappingExceptionResolver();
  }
}
