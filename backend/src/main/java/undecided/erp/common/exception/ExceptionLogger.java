package undecided.erp.common.exception;

import jakarta.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 例外情報をログとして記録するためのユーティリティクラス。
 * <p>
 * このクラスは、アプリケーションログとモニタリングログを分離し、 各種例外レベル（INFO、WARN、ERROR）に基づいて適切にログ出力を行います。
 * また、例外コードや例外メッセージを基にしたカスタムログメッセージの生成や、 ログフォーマットの妥当性チェック機能も提供します。
 */
@RequiredArgsConstructor
public class ExceptionLogger {

  private static final String MONITORING_LOG_LOGGER_SUFFIX = ".Monitoring";
  private final Logger applicationLogger;
  private final Logger monitoringLogger;
  private final Map<ExceptionLevel, ExceptionLogger.LogLevelWrappingLogger> exceptionLevelLoggers;
  private final ExceptionLogger.InfoLogger infoLogger;
  private final ExceptionLogger.WarnLogger warnLogger;
  private final ExceptionLogger.ErrorLogger errorLogger;
  @Setter
  private ExceptionCodeResolver exceptionCodeResolver;
  @Setter
  private ExceptionLevelResolver exceptionLevelResolver;
  private String PLACEHOLDER_OF_EXCEPTION_CODE;
  private String PLACEHOLDER_OF_EXCEPTION_MESSAGE;
  @Setter
  private String logMessageFormat;
  @Setter
  private String defaultCode;
  @Setter
  private String defaultMessage;
  @Setter
  private boolean trimLogMessage;

  /**
   * 指定された名前を使用して ExceptionLogger を初期化します。
   * <p>
   * このクラスは指定された名前を基にアプリケーションロガーおよびモニタリングロガーを設定し、 各種ログレベル（INFO、WARN、ERROR）用のロガーを内部的に準備します。
   *
   * @param name ロガーに使用する名前。通常はアプリケーションのコンポーネント名やモジュール名を指定します。
   */
  public ExceptionLogger(String name) {
    this.exceptionLevelLoggers = new ConcurrentHashMap<>();
    this.exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
    this.PLACEHOLDER_OF_EXCEPTION_CODE = "{0}";
    this.PLACEHOLDER_OF_EXCEPTION_MESSAGE = "{1}";
    this.logMessageFormat = String.format("[%s] %s", this.PLACEHOLDER_OF_EXCEPTION_CODE,
        this.PLACEHOLDER_OF_EXCEPTION_MESSAGE);
    this.defaultCode = "UNDEFINED-CODE";
    this.defaultMessage = "UNDEFINED-MESSAGE";
    this.trimLogMessage = true;
    this.applicationLogger = LoggerFactory.getLogger(name);
    this.monitoringLogger = LoggerFactory.getLogger(name + MONITORING_LOG_LOGGER_SUFFIX);
    this.infoLogger = new ExceptionLogger.InfoLogger();
    this.warnLogger = new ExceptionLogger.WarnLogger();
    this.errorLogger = new ExceptionLogger.ErrorLogger();
  }

  /**
   * このメソッドは、オブジェクトのプロパティがセットされた後に呼び出され、初期化のための操作を実行します。
   * <p>
   * 主に以下の操作を行います: 1. ログメッセージフォーマットの妥当性を検証します。 2. 例外レベル解決ロジックが設定されていない場合、デフォルトの例外レベル解決ロジックを設定します。 3.
   * 各例外レベル（INFO、WARN、ERROR）に対応するロガーを登録します。
   * <p>
   * Spring Frameworkの{@code InitializingBean}インターフェースを実装しており、
   * オブジェクトの依存関係が設定された後、Beanのライフサイクル内でこのメソッドが自動的に実行されます。
   * <p>
   * このメソッドを通じて、例外ログを適切に処理するための内部設定を整えます。
   */
  @PostConstruct
  public void afterPropertiesSet() {
    this.validateLogMessageFormat(this.logMessageFormat);
    if (this.exceptionLevelResolver == null) {
      this.exceptionLevelResolver = new DefaultExceptionLevelResolver(this.exceptionCodeResolver);
    }

    this.registerExceptionLevelLoggers(ExceptionLevel.INFO, this.infoLogger);
    this.registerExceptionLevelLoggers(ExceptionLevel.WARN, this.warnLogger);
    this.registerExceptionLevelLoggers(ExceptionLevel.ERROR, this.errorLogger);
  }

  /**
   * 渡された例外をログ出力します。 指定された例外のレベルを解決し、その結果に応じた適切なロガーを使用してログを出力します。
   * 例外レベルが解決できない場合や対応するロガーが存在しない場合は、エラーロガーを使用します。
   *
   * @param ex ログ出力対象の例外
   */
  public void log(Exception ex) {
    ExceptionLevel level = this.exceptionLevelResolver.resolveExceptionLevel(ex);
    ExceptionLogger.LogLevelWrappingLogger logger = null;
    if (level != null) {
      logger = this.exceptionLevelLoggers.get(level);
    }

    if (logger == null) {
      logger = this.errorLogger;
    }

    this.log(ex, logger);
  }

  /**
   * INFOレベルのロガーを使用して、指定された例外をログ出力します。
   *
   * @param ex ログ出力対象の例外
   */
  public void info(Exception ex) {
    this.log(ex, this.infoLogger);
  }

  /**
   * WARN レベルのロガーを使用して、指定された例外をログ出力します。
   *
   * @param ex ログ出力対象の例外
   */
  public void warn(Exception ex) {
    this.log(ex, this.warnLogger);
  }

  /**
   * ERRORレベルのロガーを使用して、指定された例外をログ出力します。
   *
   * @param ex ログ出力対象の例外
   */
  public void error(Exception ex) {
    this.log(ex, this.errorLogger);
  }

  /**
   * ログメッセージフォーマットの妥当性を検証します。
   * <p>
   * 指定されたフォーマットに例外コードのプレースホルダーおよび例外メッセージのプレースホルダーが含まれていない場合、 {@link IllegalArgumentException}
   * をスローします。
   *
   * @param logMessageFormat ログメッセージのフォーマット文字列。 例外コードのプレースホルダーと例外メッセージのプレースホルダーが必要です。
   */
  protected void validateLogMessageFormat(String logMessageFormat) {
    if (logMessageFormat == null || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_CODE)
        || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_MESSAGE)) {
      String message =
          "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced "
              + "with exception message. current logMessageFormat is \""
              + logMessageFormat + "\".";
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * 指定された例外を基に例外コードを解決します。 このメソッドは内部的に例外コード解決ロジックを使用して、例外コードを文字列として返します。
   * 例外コード解決ロジックが設定されていない場合は、null を返します。
   *
   * @param ex 解決対象の例外
   * @return 解決された例外コード、もしくは解決できない場合は null
   */
  protected String resolveExceptionCode(Exception ex) {
    String exceptionCode = null;
    if (this.exceptionCodeResolver != null) {
      exceptionCode = this.exceptionCodeResolver.resolveExceptionCode(ex);
    }

    return exceptionCode;
  }

  /**
   * 指定された例外情報を基にログメッセージを生成します。 このメソッドは、例外コードを解決し、それと例外メッセージをフォーマットして ログメッセージを構成します。
   *
   * @param ex ログメッセージ生成の基となる例外オブジェクト
   * @return 生成されたログメッセージ文字列
   */
  protected String makeLogMessage(Exception ex) {
    String exceptionCode = this.resolveExceptionCode(ex);
    return this.formatLogMessage(exceptionCode, ex.getMessage());
  }

  /**
   * 指定された例外コードと例外メッセージを基にログメッセージをフォーマットします。 例外コードまたは例外メッセージが指定されていない場合、それぞれデフォルト値が使用されます。
   * また、ログメッセージの設定によっては、生成されたメッセージをトリム処理します。
   *
   * @param exceptionCode ログメッセージに使用する例外コード
   * @param exceptionMessage ログメッセージに使用する例外メッセージ
   * @return フォーマットされたログメッセージ
   */
  protected String formatLogMessage(String exceptionCode, String exceptionMessage) {
    String bindingExceptionCode = exceptionCode;
    String bindingExceptionMessage = exceptionMessage;
    if (!StringUtils.hasText(exceptionCode)) {
      bindingExceptionCode = this.defaultCode;
    }

    if (!StringUtils.hasText(exceptionMessage)) {
      bindingExceptionMessage = this.defaultMessage;
    }

    String message = MessageFormat.format(this.logMessageFormat, bindingExceptionCode,
        bindingExceptionMessage);
    if (this.trimLogMessage) {
      message = message.trim();
    }

    return message;
  }

  /**
   * 指定された例外レベルとロガーを登録します。 このメソッドを使用して、各例外レベルに対するログ出力のロジックを設定できます。
   *
   * @param level 例外レベル（INFO、WARN、ERROR、UNKNOWN）
   * @param logger 該当レベルのログ出力に使用するロガーの実装
   */
  protected void registerExceptionLevelLoggers(
      ExceptionLevel level, ExceptionLogger.LogLevelWrappingLogger logger) {
    this.exceptionLevelLoggers.put(level, logger);
  }

  /**
   * アプリケーションロガーを取得します。 このロガーは、アプリケーション全体で使用するログ出力に利用されます。
   *
   * @return アプリケーションロガーのインスタンス
   */
  protected Logger getApplicationLogger() {
    return this.applicationLogger;
  }

  /**
   * モニタリング用途で使用するロガーを取得します。 このロガーは主にシステムの監視や運用に関するログ出力に適しています。
   *
   * @return モニタリングロガーのインスタンス
   */
  protected Logger getMonitoringLogger() {
    return this.monitoringLogger;
  }

  /**
   * 指定された例外とロガーを使用してログを記録します。 提供されたロガーが有効である場合、例外情報を基にログメッセージを生成し、 ロガーを用いてログを出力します。
   *
   * @param ex ログ出力対象の例外オブジェクト
   * @param logger ログ出力を担う {@link ExceptionLogger.LogLevelWrappingLogger} インスタンス
   */
  private void log(Exception ex, ExceptionLogger.LogLevelWrappingLogger logger) {
    if (logger.isEnabled()) {
      String logMessage = this.makeLogMessage(ex);
      logger.log(logMessage, ex);
    }
  }

  /**
   * ログレベルに基づいてログメッセージのフィルタリングおよび記録を行うインターフェース。 このインターフェースは、特定のログレベル（例: INFO, WARN, ERROR）に適合する
   * ログ処理のロジックを定義するために使用されます。
   * <p>
   * ユーザーはこのインターフェースを実装することで、各ロギングレベルに対応する カスタムロジックを提供できます。
   */
  protected interface LogLevelWrappingLogger {

    boolean isEnabled();

    void log(String var1, Exception var2);
  }

  /**
   * InfoLoggerクラスは、ログレベルがINFOの場合にログメッセージを処理するための
   * ロジックを提供します。このクラスは、ExceptionLogger.LogLevelWrappingLogger インターフェースを実装しています。
   * <p>
   * InfoLoggerは、監視ログとアプリケーションログの両方を検査し、それぞれのロガーが INFOレベルのログを有効にしている場合にログを記録します。
   */
  private final class InfoLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private InfoLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isInfoEnabled()
          || ExceptionLogger.this.applicationLogger.isInfoEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isInfoEnabled()) {
        ExceptionLogger.this.monitoringLogger.info(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isInfoEnabled()) {
        ExceptionLogger.this.applicationLogger.info(logMessage, ex);
      }

    }
  }

  /**
   * WarnLogger クラスは、ExceptionLogger クラスにおいて WARN レベルの ログ処理を実行するための実装クラスです。このクラスは内部クラスとして定義されており、
   * ExceptionLogger.LogLevelWrappingLogger インターフェースを実装しています。
   * <p>
   * このクラスの目的は、WARN レベルでのログ記録を可能にし、 モニタリングロガーおよびアプリケーションロガーの両方で適切な ログメッセージを記録することです。
   */
  private final class WarnLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private WarnLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isWarnEnabled()
          || ExceptionLogger.this.applicationLogger.isWarnEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isWarnEnabled()) {
        ExceptionLogger.this.monitoringLogger.warn(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isWarnEnabled()) {
        ExceptionLogger.this.applicationLogger.warn(logMessage, ex);
      }

    }
  }

  /**
   * ErrorLoggerクラスは、ExceptionLogger.LogLevelWrappingLoggerインターフェースを実装し、
   * エラーレベルのログメッセージを処理および記録するためのロジックを提供します。 このクラスは、関連するロガーのエラー有効状態を確認し、有効な場合にログを記録します。
   */
  private final class ErrorLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private ErrorLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isErrorEnabled() ||
          ExceptionLogger.this.applicationLogger.isErrorEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isErrorEnabled()) {
        ExceptionLogger.this.monitoringLogger.error(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isErrorEnabled()) {
        ExceptionLogger.this.applicationLogger.error(logMessage, ex);
      }

    }
  }
}
