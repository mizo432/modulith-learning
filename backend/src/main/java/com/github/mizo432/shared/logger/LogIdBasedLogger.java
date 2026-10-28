package com.github.mizo432.shared.logger;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * ログメッセージIDに基づいて、プロパティファイルからローカライズされたログメッセージを取得して出力するロガーラッパークラス。
 *
 * <p>従来のSLF4Jロガーの代わりにこのクラスを使用することで、ログメッセージを外部のプロパティファイルで管理し、
 * メッセージIDをログ出力に含めることができます。これにより、ログの国際化やメッセージの一括管理が可能になります。
 *
 * <p>ログメッセージは {@code i18n/log-messages.properties} ファイルから読み込まれます。
 * 指定されたメッセージIDが見つからない場合は、デフォルトのフォーマットでメッセージIDと引数を出力します。
 *
 * <p>使用例：
 * <pre>
 * private static final LogIdBasedLogger log = LogIdBasedLogger.getLogger(MyClass.class);
 *
 * // ログ出力
 * log.info("i.user.1001", userName, action);
 * log.warn("w.user.2001", userId, exception);
 * </pre>
 */
public class LogIdBasedLogger {

  /** メッセージIDが見つからない場合に出力されるデフォルトのフォーマット。 */
  private static final String UNDEFINED_MESSAGE_FORMAT = "UNDEFINED-MESSAGE id:{0} arg:{1}"; // (1)

  /** ログメッセージのプロパティファイルを読み込むためのメッセージソース。 */
  private static ResourceBundleMessageSource messageSource =
      new ResourceBundleMessageSource(); // (2)

  static { // (3)
    messageSource.setDefaultEncoding("UTF-8"); // (4)
    messageSource.setBasenames("i18n/log-messages"); // (5)
  }

  /** 内部で使用するSLF4Jロガーインスタンス。 */
  private final Logger logger;

  /**
   * 指定されたクラスのLogIdBasedLoggerインスタンスを生成します。
   *
   * @param clazz ロガーを取得するクラス
   */
  private LogIdBasedLogger(Class<?> clazz) {
    logger = LoggerFactory.getLogger(clazz); // (6)
  }

  /**
   * 指定されたクラスのLogIdBasedLoggerインスタンスを取得します。
   *
   * @param clazz ロガーを取得するクラス
   * @return 指定されたクラスに対応するLogIdBasedLoggerインスタンス
   */
  public static LogIdBasedLogger getLogger(Class<?> clazz) {
    return new LogIdBasedLogger(clazz);
  }

  /**
   * DEBUGレベルのログ出力が有効かどうかを確認します。
   *
   * @return DEBUGレベルのログ出力が有効な場合は {@code true}
   */
  public boolean isDebugEnabled() { // (7)
    return logger.isDebugEnabled();
  }

  /**
   * DEBUGレベルのログを、フォーマット文字列を使用して直接出力します。
   *
   * <p>このメソッドはメッセージIDを使用せず、従来のSLF4Jのフォーマット方式でログ出力を行います。
   *
   * @param format ログメッセージのフォーマット文字列
   * @param args フォーマットに埋め込む引数
   */
  public void debug(String format, Object... args) {
    logger.debug(format, args); // (8)
  }

  /**
   * INFOレベルのログを出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param args メッセージに埋め込む引数
   */
  public void info(String id, Object... args) {
    if (logger.isInfoEnabled()) {
      logger.info(createLogMessage(id, args)); // (9)
    }
  }

  /**
   * WARNレベルのログを出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param args メッセージに埋め込む引数
   */
  public void warn(String id, Object... args) {
    if (logger.isWarnEnabled()) {
      logger.warn(createLogMessage(id, args)); // (9)
    }
  }

  /**
   * ERRORレベルのログを出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param args メッセージに埋め込む引数
   */
  public void error(String id, Object... args) {
    if (logger.isErrorEnabled()) {
      logger.error(createLogMessage(id, args)); // (9)
    }
  }

  /**
   * TRACEレベルのログを出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param args メッセージに埋め込む引数
   */
  public void trace(String id, Object... args) {
    if (logger.isTraceEnabled()) {
      logger.trace(createLogMessage(id, args)); // (9)
    }
  }

  /**
   * WARNレベルのログを、例外情報とともに出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param t 関連する例外
   * @param args メッセージに埋め込む引数
   */
  public void warn(String id, Throwable t, Object... args) {
    if (logger.isWarnEnabled()) {
      logger.warn(createLogMessage(id, args), t); // (9)
    }
  }

  /**
   * ERRORレベルのログを、例外情報とともに出力します。
   *
   * @param id ログメッセージのID（プロパティファイルで定義されたキー）
   * @param t 関連する例外
   * @param args メッセージに埋め込む引数
   */
  public void error(String id, Throwable t, Object... args) {
    if (logger.isErrorEnabled()) {
      logger.error(createLogMessage(id, args), t); // (9)
    }
  }

  /**
   * 指定されたメッセージIDと引数からログメッセージを作成します。
   *
   * @param id ログメッセージのID
   * @param args メッセージに埋め込む引数
   * @return 作成されたログメッセージ文字列
   */
  private String createLogMessage(String id, Object... args) {
    return getMessage(id, args);
  }

  /**
   * 指定されたメッセージIDに対応するローカライズされたメッセージを取得します。
   *
   * <p>メッセージIDが見つからない場合は、デフォルトのフォーマットでメッセージIDと引数を文字列化したものを返します。
   *
   * @param id ログメッセージのID
   * @param args メッセージに埋め込む引数
   * @return ローカライズされたメッセージ文字列
   */
  private String getMessage(String id, Object... args) {
    String message;
    try {
      message = messageSource.getMessage(id, args, Locale.getDefault());
    } catch (NoSuchMessageException e) { // (10)
      message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, id, Arrays.toString(args));
    }
    return message;
  }
}
