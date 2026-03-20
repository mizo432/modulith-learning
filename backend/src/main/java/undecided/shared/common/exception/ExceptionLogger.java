package undecided.shared.common.exception;

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
 *
 * <p>このクラスは、アプリケーションログとモニタリングログを分離し、 各種例外レベル（INFO、WARN、ERROR）に基づいて適切にログ出力を行います。
 * また、例外コードや例外メッセージを基にしたカスタムログメッセージの生成や、 ログフォーマットの妥当性チェック機能も提供します。
 */
@RequiredArgsConstructor
public class ExceptionLogger {

  /**
   * モニタリング用のロガー名に付加されるサフィックスを表します。
   *
   * <p>この定数は、モニタリング用のログ出力に特化したロガー名を一意に識別するために使用されます。
   * ロガー名の構造に統一性を持たせるために、アプリケーションロガーの名前にこのサフィックスを追加することを 意図しています。
   */
  private static final String MONITORING_LOG_LOGGER_SUFFIX = ".Monitoring";

  /**
   * アプリケーションロガーを保持するためのフィールド。
   *
   * <p>このロガーは、アプリケーション全体にわたるログ出力を担います。 主にアプリケーションの動作や重要な情報を記録するために使用されます。
   *
   * <p>このフィールドは不変であり、インスタンスの初期化時に設定されます。
   */
  private final Logger applicationLogger;

  /**
   * モニタリング用途で使用されるロガーインスタンス。
   *
   * <p>このロガーは、アプリケーションの監視やシステム運用に関するログを記録するために使用されます。 主にエラーログやシステムの状態を追跡する目的で設定されており、
   * システム管理者やオペレーションチームにとって重要な情報を提供します。
   */
  private final Logger monitoringLogger;

  /**
   * 例外レベルに対応するロギングを管理するマップ。
   *
   * <p>このマップは {@link ExceptionLevel} をキーとし、対応するロギング処理を行う {@link
   * ExceptionLogger.LogLevelWrappingLogger} インスタンスを値として保持します。
   * 各例外レベル（INFO、WARN、ERROR、UNKNOWN）ごとのロガーを定義するために使用されます。
   *
   * <p>このフィールドは、例外処理においてレベルごとに適切なロギングロジックを実行するために利用されます。
   */
  private final Map<ExceptionLevel, ExceptionLogger.LogLevelWrappingLogger> exceptionLevelLoggers;

  /**
   * infoLoggerは、INFOレベルの例外ロギングを処理するためのロガーインスタンスを表します。
   * このインスタンスは、ExceptionLoggerクラス内で例外をINFOレベルでログ出力する際に使用されます。
   *
   * <p>InfoLoggerは、インスタンス化されたExceptionLoggerクラス内で監視ロガーおよびアプリケーションロガーの
   * INFOレベル有効状態を確認し、該当するロガーにログメッセージを出力する役割を持ちます。
   */
  private final ExceptionLogger.InfoLogger infoLogger;

  /**
   * warnLogger は、WARN レベルのログメッセージを処理するためのロガーです。
   *
   * <p>このロガーは、内部的に {@code ExceptionLogger.WarnLogger} クラスのインスタンスとして実装され、
   * アプリケーションロガーおよびモニタリングロガーを通じて WARN レベルのログ出力を行います。
   * 主に警告として扱われる例外の処理や、重要度が中程度のログメッセージを記録する用途で利用されます。
   */
  private final ExceptionLogger.WarnLogger warnLogger;

  /**
   * エラーログを処理および記録するためのロガーインスタンス。
   *
   * <p>このロガーは、重大度レベルがエラーの例外を記録するために使用されます。 内部的には {@link ExceptionLogger.ErrorLogger}
   * クラスを利用し、記録が有効である場合に アプリケーションロガーおよびモニタリングロガーを通じてログを出力します。
   */
  private final ExceptionLogger.ErrorLogger errorLogger;

  /**
   * 例外コードを解決するための {@link ExceptionCodeResolver} インスタンスを保持します。
   *
   * <p>このフィールドは、例外発生時に該当する例外コードを取得するロジックを提供します。 例外ログの出力メッセージに例外コードを追加するために使用されます。
   *
   * <p>設定されていない場合、例外コード解決処理をスキップし、ログ出力メッセージにはコードが含まれません。
   */
  @Setter private ExceptionCodeResolver exceptionCodeResolver;

  /**
   * 例外のレベルを解決するためのロジックを提供する {@link ExceptionLevelResolver} の実装を設定します。
   *
   * <p>このフィールドは、例外のレベル (INFO, WARN, ERROR, UNKNOWN) を特定するために使用されます。 {@link ExceptionLogger}
   * クラスのインスタンスにおいて、例外の発生時に適切なログレベルで ログを記録するための重要なコンポーネントです。
   *
   * <p>カスタムの例外レベル解決ロジックを用いる場合、このフィールドを適切に設定してください。
   */
  @Setter private ExceptionLevelResolver exceptionLevelResolver;

  /**
   * 例外コードをプレースホルダーとして一時的に格納するためのフィールド。
   *
   * <p>このフィールドは、例外コードをログメッセージに埋め込む際に利用されます。 具体的には、例外コードが生成または解決された後に、一時的なフォーマット処理目的で使用されます。
   *
   * <p>運用環境におけるロギングの一貫性を保つため、このプレースホルダーは フィールドの初期値や既存値に注意を払って取り扱う必要があります。
   */
  private String PLACEHOLDER_OF_EXCEPTION_CODE;

  /**
   * 例外メッセージのプレースホルダーを定義するフィールド。
   *
   * <p>このフィールドは、ログメッセージの生成時に例外のメッセージを挿入するための プレースホルダーとして使用されます。通常、ログメッセージのフォーマット文字列
   * に挿入される形で利用され、例外のメッセージを動的に埋め込む役割を果たします。
   *
   * <p>例外ログ出力機能において、統一されたフォーマットで例外情報を記録するために 使用される重要な設定値です。
   */
  private String PLACEHOLDER_OF_EXCEPTION_MESSAGE;

  /**
   * ログメッセージのフォーマットを定義するためのフィールドです。
   *
   * <p>このフィールドには、例外ログメッセージのフォーマット文字列を設定します。 フォーマット文字列は、例外コード（{@code
   * PLACEHOLDER_OF_EXCEPTION_CODE}）と メッセージ（{@code
   * PLACEHOLDER_OF_EXCEPTION_MESSAGE}）のプレースホルダーを含む必要があります。 例外情報を出力する際に、このフォーマットに基づいてログメッセージが生成されます。
   *
   * <p>注意: フィールドの値は {@link #validateLogMessageFormat(String)} によって妥当性が
   * 検証され、適切なフォーマットでない場合は例外がスローされる可能性があります。
   */
  @Setter private String logMessageFormat;

  /**
   * デフォルトの例外コードを格納するためのフィールド。
   *
   * <p>このフィールドは、ログメッセージや例外処理において使用される標準的な例外コードを定義します。 アプリケーション内で、特定の例外に対応するコードが指定されていない場合に利用されます。
   * 必要に応じて Setter メソッドを使用して値を設定できます。
   */
  @Setter private String defaultCode;

  /**
   * デフォルトのエラーメッセージを格納するフィールド。
   *
   * <p>このフィールドは、明示的にメッセージが設定されない場合に使用されるメッセージを保持します。 ExceptionLogger クラス内の例外処理において、例外コードやカスタムメッセージが
   * 指定されない場合のバックアップとして利用されます。ログ出力や通知用途に活用されます。
   */
  @Setter private String defaultMessage;

  /**
   * ログメッセージのトリム処理を制御するフラグ。
   *
   * <p>このフラグが {@code true} に設定されている場合、ログメッセージが生成される際に 周囲の空白文字がトリム（除去）されます。 {@code false}
   * に設定されている場合、ログメッセージはそのままの形で保持されます。
   *
   * <p>このプロパティは、ログ出力の一貫性やフォーマットを調整するために使用されます。
   */
  @Setter private boolean trimLogMessage;

  /**
   * 指定された名前を使用して ExceptionLogger を初期化します。
   *
   * <p>このクラスは指定された名前を基にアプリケーションロガーおよびモニタリングロガーを設定し、 各種ログレベル（INFO、WARN、ERROR）用のロガーを内部的に準備します。
   *
   * @param name ロガーに使用する名前。通常はアプリケーションのコンポーネント名やモジュール名を指定します。
   */
  public ExceptionLogger(String name) {
    this.exceptionLevelLoggers = new ConcurrentHashMap<>();
    this.exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
    this.PLACEHOLDER_OF_EXCEPTION_CODE = "{0}";
    this.PLACEHOLDER_OF_EXCEPTION_MESSAGE = "{1}";
    this.logMessageFormat =
        String.format(
            "[%s] %s", this.PLACEHOLDER_OF_EXCEPTION_CODE, this.PLACEHOLDER_OF_EXCEPTION_MESSAGE);
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
   *
   * <p>主に以下の操作を行います: 1. ログメッセージフォーマットの妥当性を検証します。 2. 例外レベル解決ロジックが設定されていない場合、デフォルトの例外レベル解決ロジックを設定します。
   * 3. 各例外レベル（INFO、WARN、ERROR）に対応するロガーを登録します。
   *
   * <p>Spring Frameworkの{@code InitializingBean}インターフェースを実装しており、
   * オブジェクトの依存関係が設定された後、Beanのライフサイクル内でこのメソッドが自動的に実行されます。
   *
   * <p>このメソッドを通じて、例外ログを適切に処理するための内部設定を整えます。
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
   *
   * <p>指定されたフォーマットに例外コードのプレースホルダーおよび例外メッセージのプレースホルダーが含まれていない場合、 {@link IllegalArgumentException}
   * をスローします。
   *
   * @param logMessageFormat ログメッセージのフォーマット文字列。 例外コードのプレースホルダーと例外メッセージのプレースホルダーが必要です。
   */
  protected void validateLogMessageFormat(String logMessageFormat) {
    if (logMessageFormat == null
        || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_CODE)
        || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_MESSAGE)) {
      String message =
          "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced "
              + "with exception message. current logMessageFormat is \""
              + logMessageFormat
              + "\".";
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

    String message =
        MessageFormat.format(this.logMessageFormat, bindingExceptionCode, bindingExceptionMessage);
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
   * アプリケーションロガーを取得します。
   *
   * <p>このロガーは、アプリケーション全体で使用するログ出力に利用されます。
   *
   * @return アプリケーションロガーのインスタンス
   */
  protected Logger getApplicationLogger() {
    return this.applicationLogger;
  }

  /**
   * モニタリング用途で使用するロガーを取得します。
   *
   * <p>このロガーは主にシステムの監視や運用に関するログ出力に適しています。
   *
   * @return モニタリングロガーのインスタンス
   */
  protected Logger getMonitoringLogger() {
    return this.monitoringLogger;
  }

  /**
   * 指定された例外とロガーを使用してログを記録します。
   *
   * <p>提供されたロガーが有効である場合、例外情報を基にログメッセージを生成し、 ロガーを用いてログを出力します。
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
   *
   * <p>ユーザーはこのインターフェースを実装することで、各ロギングレベルに対応する カスタムロジックを提供できます。
   */
  protected interface LogLevelWrappingLogger {

    boolean isEnabled();

    /**
     * 指定されたメッセージと例外を使用してログを記録します。
     *
     * @param var1 ログメッセージとして記録する文字列
     * @param var2 ログに関連付けられる例外オブジェクト
     */
    void log(String var1, Exception var2);
  }

  /**
   * InfoLoggerクラスは、ログレベルがINFOの場合にログメッセージを処理するための
   * ロジックを提供します。このクラスは、ExceptionLogger.LogLevelWrappingLogger インターフェースを実装しています。
   *
   * <p>InfoLoggerは、監視ログとアプリケーションログの両方を検査し、それぞれのロガーが INFOレベルのログを有効にしている場合にログを記録します。
   */
  private final class InfoLogger implements ExceptionLogger.LogLevelWrappingLogger {

    private InfoLogger() {}

    /**
     * INFOレベルのログが有効かどうかを判定します。
     *
     * <p>監視ロガーまたはアプリケーションロガーのいずれかがINFOレベルのログを有効にしている場合にtrueを返します。
     *
     * @return INFOレベルのログが有効であればtrue、無効であればfalse
     */
    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isInfoEnabled()
          || ExceptionLogger.this.applicationLogger.isInfoEnabled();
    }

    /**
     * 指定されたログメッセージと例外情報をINFOレベルのログとして記録します。
     * 監視ロガーおよびアプリケーションロガーがINFOレベルのログを有効にしている場合、それぞれに対してメッセージが出力されます。
     *
     * @param logMessage 記録するログメッセージ
     * @param ex 記録する例外情報
     */
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
   *
   * <p>このクラスの目的は、WARN レベルでのログ記録を可能にし、 モニタリングロガーおよびアプリケーションロガーの両方で適切な ログメッセージを記録することです。
   */
  private final class WarnLogger implements ExceptionLogger.LogLevelWrappingLogger {

    private WarnLogger() {}

    /**
     * WARN レベルのログ記録が有効かどうかを判定します。
     *
     * <p>このメソッドは、モニタリングロガーおよびアプリケーションロガーの両方の設定を確認し、 どちらか一方でも WARN レベルが有効である場合に true を返します。
     *
     * @return WARN レベルのログ記録が有効であれば true、無効であれば false
     */
    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isWarnEnabled()
          || ExceptionLogger.this.applicationLogger.isWarnEnabled();
    }

    /**
     * 指定されたログメッセージおよび例外オブジェクトを WARN レベルで記録します。
     *
     * <p>このメソッドは、モニタリングロガーおよびアプリケーションロガーの両方を使用し、 それぞれが WARN レベルでログ記録を行う設定になっている場合に、
     * 指定されたメッセージおよび例外情報をログに出力します。
     *
     * @param logMessage 記録するログメッセージ
     * @param ex 記録する例外オブジェクト
     */
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
  private final class ErrorLogger implements ExceptionLogger.LogLevelWrappingLogger {

    private ErrorLogger() {}

    /**
     * 現在のロガー状態を確認し、エラーレベルのログが有効かどうかを判定します。
     *
     * @return trueの場合、いずれかのロガーでエラーレベルのログが有効であることを意味します。 falseの場合、どちらのロガーもエラーレベルのログが無効であることを示します。
     */
    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isErrorEnabled()
          || ExceptionLogger.this.applicationLogger.isErrorEnabled();
    }

    /**
     * エラーレベルのログメッセージを記録します。 指定されたログメッセージと例外情報を使用して、モニタリングロガーおよびアプリケーションロガーに
     * エラー情報を出力します。それぞれのロガーがエラーレベルのログ出力を有効にしている場合にのみ動作します。
     *
     * @param logMessage 記録するログメッセージ。
     * @param ex 記録する例外情報。アプリケーションロガーにのみ使用されます。
     */
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
