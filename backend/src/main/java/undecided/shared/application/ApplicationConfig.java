package undecided.shared.application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import undecided.shared.exception.ExceptionCodeResolver;
import undecided.shared.exception.ExceptionLogger;
import undecided.shared.exception.ResultMessagesLoggingInterceptor;
import undecided.shared.exception.SimpleMappingExceptionCodeResolver;

/**
 * アプリケーションの構成を定義するための設定クラス。
 *
 * <p>Spring Frameworkのコンフィギュレーションアノテーションを使用して、アプリケーションが必要とする
 * Beanの生成や構成を管理します。本クラスでは、ロギングやタスク実行、および例外ハンドリングに関連する コンポーネントを定義します。
 *
 * <p>主な目的: - アプリケーション全体で共通して使用されるサービスやユーティリティの初期化 - モジュール間で共有されるコンポーネントの中央管理 - 読みやすく再利用可能なシステム設定の提供
 */
@Configuration
public class ApplicationConfig {

  /**
   * ロギングに使用される例外ロガーの名前を定義する定数。
   *
   * <p>この名前は例外発生時のログ記録に関連するコンポーネント間で一貫した識別子として 使用されます。主に {@link ExceptionLogger}
   * クラスのインスタンス生成や設定に利用されます。
   *
   * <p>例外に関するログの一元管理を行い、システム全体で一貫したロギングをサポートします。
   */
  private static final String EXCEPTION_LOGGER_NAME = "EXCEPTION_LOGGER_NAME";

  @Bean
  public ExceptionCodeResolver exceptionCodeResolver() {
    return new SimpleMappingExceptionCodeResolver();
  }

  /**
   * 例外ログを記録するための {@link ExceptionLogger} インスタンスを生成します。
   *
   * <p>このメソッドで生成される {@link ExceptionLogger} は、アプリケーション全体で発生する
   * 例外の記録に使用されます。一貫性のある方法で例外情報をログに出力することにより、 デバッグやエラー解析を効率化します。
   *
   * @return 例外ログの記録を行うために初期化された {@link ExceptionLogger} インスタンス
   */
  @Bean
  public ExceptionLogger exceptionLogger(ExceptionCodeResolver exceptionCodeResolver) {
    ExceptionLogger logger = new ExceptionLogger(EXCEPTION_LOGGER_NAME);
    logger.setExceptionCodeResolver(exceptionCodeResolver);
    return logger;
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
   * resultMessagesLoggingInterceptorメソッドは、{@link ResultMessagesLoggingInterceptor}のインスタンスを作成し、
   * 結果メッセージや例外に関するログ記録を行う機能を提供します。
   *
   * @return 結果メッセージおよび例外のログ記録機能を持つResultMessagesLoggingInterceptorインスタンス
   */
  @Bean("resultMessagesLoggingInterceptor")
  public ResultMessagesLoggingInterceptor resultMessagesLoggingInterceptor(
      ExceptionLogger exceptionLogger) {
    ResultMessagesLoggingInterceptor bean = new ResultMessagesLoggingInterceptor();
    bean.setExceptionLogger(exceptionLogger); // (2)
    return bean;
  }

  /**
   * {@link ResultMessagesLoggingInterceptor} を適用する Advice（インターセプタ）を生成し、Springフレームワークに登録するための Bean
   * を作成します。 本メソッドは、{@code @Service} アノテーションで装飾されたクラス内の全メソッドを対象とする AspectJ Pointcut
   * を定義し、指定されたインターセプタを適用します。これにより、サービス層のメソッド呼び出し時に例外や結果メッセージのログ記録処理が自動的に行われます。 生成された {@link
   * Advisor} は、Springの AOP 機能と連携し、指定された Pointcut に一致するメソッドの実行前後でインターセプタの処理を実行します。 注意事項：本メソッドは
   * {@code @Bean} アノテーションを付与しており、Spring コンテナにより自動的に管理されます。 この構成により、例外ハンドリングやログ出力のロジックを Service
   * クラスから分離し、単一責任の原則に則ったクリーンなコード構造を維持できます。 本メソッドは {@link ResultMessagesLoggingInterceptor}
   * によって、{@code @Service} クラス内のメソッド呼び出し時に発生する {@link ResultMessagesNotificationException}
   * などの例外をログに記録し、正常な処理フローを維持します。
   *
   * @param resultMessagesLoggingInterceptor
   *     適用するインターセプタの実装インスタンス。メソッド呼び出しのインターセプト処理、例外ログ記録、スレッドローカルなコンテキスト管理などを担当します。
   * @return 指定された Pointcut とインターセプタを組み合わせた {@link Advisor} インスタンス。Spring AOP フレームワークにより自動的に適用されます。
   */
  @Bean
  public Advisor resultMessagesLoggingInterceptorAdvisor(
      ResultMessagesLoggingInterceptor resultMessagesLoggingInterceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("@within(org.springframework.stereotype.Service)"); // (3)
    return new DefaultPointcutAdvisor(pointcut, resultMessagesLoggingInterceptor);
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
