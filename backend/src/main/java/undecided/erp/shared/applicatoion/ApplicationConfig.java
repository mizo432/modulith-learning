package undecided.erp.shared.applicatoion;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import undecided.erp.common.exception.ExceptionLogger;

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
}
