package com.github.mizo432.shared.sensitive;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * 機微な個人情報を自動的にマスクするSLF4Jロガーラッパークラスです。
 *
 * <p>このクラスは {@link Logger} インターフェースを実装し、内部のロガーをラップして、
 * ログ出力前にメッセージに含まれる機微な個人情報を {@link SensitiveDataMasker} を使用して
 * 自動的にマスクします。
 *
 * <p>ログ出力時に氏名、メールアドレス、電話番号、生年月日などの機微な情報が
 * 自動的に検出され、マスクされた状態でログに記録されます。
 *
 * <p>使用例：
 * <pre>
 * Logger wrappedLogger = SensitiveLoggerWrapper.wrap(LoggerFactory.getLogger(MyClass.class));
 * wrappedLogger.info("User email: {}", "taro@example.com");
 * // ログ出力: "User email: t***@example.com"
 * </pre>
 *
 * <p>マスク機能は設定により有効/無効を切り替えられます。
 * <pre>
 * SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(logger, false);
 * // マスク無効状態では元のメッセージがそのまま出力される
 * </pre>
 */
public class SensitiveLoggerWrapper implements Logger {

  /** 内部で使用する実際のSLF4Jロガーインスタンス。 */
  private final Logger logger;

  /** 機微な情報マスクを有効にするかどうかのフラグ。 */
  private final boolean maskingEnabled;

  /**
   * 指定されたロガーをラップする {@code SensitiveLoggerWrapper} を作成します。
   * マスク機能はデフォルトで有効になります。
   *
   * @param logger ラップ対象のSLF4Jロガー
   */
  public SensitiveLoggerWrapper(Logger logger) {
    this(logger, true);
  }

  /**
   * 指定されたロガーをラップする {@code SensitiveLoggerWrapper} を作成します。
   *
   * @param logger ラップ対象のSLF4Jロガー
   * @param maskingEnabled 機微な情報マスクを有効にするかどうか
   */
  public SensitiveLoggerWrapper(Logger logger, boolean maskingEnabled) {
    if (logger == null) {
      throw new IllegalArgumentException("logger must not be null");
    }
    this.logger = logger;
    this.maskingEnabled = maskingEnabled;
  }

  /**
   * 指定されたロガーをラップする {@code SensitiveLoggerWrapper} を作成します。
   * マスク機能はデフォルトで有効になります。
   *
   * @param logger ラップ対象のSLF4Jロガー
   * @return 機微な情報マスクを有効にしたラッパーロガー
   * @throws IllegalArgumentException loggerがnullの場合
   */
  public static Logger wrap(Logger logger) {
    return new SensitiveLoggerWrapper(logger);
  }

  /**
   * 指定されたロガーをラップする {@code SensitiveLoggerWrapper} を作成します。
   *
   * @param logger ラップ対象のSLF4Jロガー
   * @param maskingEnabled 機微な情報マスクを有効にするかどうか
   * @return 指定された設定で機微な情報マスクを行うラッパーロガー
   * @throws IllegalArgumentException loggerがnullの場合
   */
  public static Logger wrap(Logger logger, boolean maskingEnabled) {
    return new SensitiveLoggerWrapper(logger, maskingEnabled);
  }

  /**
   * 機微な情報マスクが有効かどうかを返します。
   *
   * @return マスクが有効な場合は {@code true}
   */
  public boolean isMaskingEnabled() {
    return maskingEnabled;
  }

  /**
   * 内部のラップされているロガーを返します。
   *
   * @return 内部のロガーインスタンス
   */
  public Logger unwrap() {
    return logger;
  }

  @Override
  public String getName() {
    return logger.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public void trace(String msg) {
    logger.trace(mask(msg));
  }

  @Override
  public void trace(String format, Object arg) {
    logger.trace(mask(format), mask(arg));
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    logger.trace(mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void trace(String format, Object... arguments) {
    logger.trace(mask(format), maskAll(arguments));
  }

  @Override
  public void trace(String msg, Throwable t) {
    logger.trace(mask(msg), t);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public void debug(String msg) {
    logger.debug(mask(msg));
  }

  @Override
  public void debug(String format, Object arg) {
    logger.debug(mask(format), mask(arg));
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    logger.debug(mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void debug(String format, Object... arguments) {
    logger.debug(mask(format), maskAll(arguments));
  }

  @Override
  public void debug(String msg, Throwable t) {
    logger.debug(mask(msg), t);
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  @Override
  public void info(String msg) {
    logger.info(mask(msg));
  }

  @Override
  public void info(String format, Object arg) {
    logger.info(mask(format), mask(arg));
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    logger.info(mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void info(String format, Object... arguments) {
    logger.info(mask(format), maskAll(arguments));
  }

  @Override
  public void info(String msg, Throwable t) {
    logger.info(mask(msg), t);
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  @Override
  public void warn(String msg) {
    logger.warn(mask(msg));
  }

  @Override
  public void warn(String format, Object arg) {
    logger.warn(mask(format), mask(arg));
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    logger.warn(mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void warn(String format, Object... arguments) {
    logger.warn(mask(format), maskAll(arguments));
  }

  @Override
  public void warn(String msg, Throwable t) {
    logger.warn(mask(msg), t);
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public void error(String msg) {
    logger.error(mask(msg));
  }

  @Override
  public void error(String format, Object arg) {
    logger.error(mask(format), mask(arg));
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    logger.error(mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void error(String format, Object... arguments) {
    logger.error(mask(format), maskAll(arguments));
  }

  @Override
  public void error(String msg, Throwable t) {
    logger.error(mask(msg), t);
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.isTraceEnabled(marker);
  }

  @Override
  public void trace(Marker marker, String msg) {
    logger.trace(marker, mask(msg));
  }

  @Override
  public void trace(Marker marker, String format, Object arg) {
    logger.trace(marker, mask(format), mask(arg));
  }

  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    logger.trace(marker, mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void trace(Marker marker, String format, Object... arguments) {
    logger.trace(marker, mask(format), maskAll(arguments));
  }

  @Override
  public void trace(Marker marker, String msg, Throwable t) {
    logger.trace(marker, mask(msg), t);
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.isDebugEnabled(marker);
  }

  @Override
  public void debug(Marker marker, String msg) {
    logger.debug(marker, mask(msg));
  }

  @Override
  public void debug(Marker marker, String format, Object arg) {
    logger.debug(marker, mask(format), mask(arg));
  }

  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    logger.debug(marker, mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void debug(Marker marker, String format, Object... arguments) {
    logger.debug(marker, mask(format), maskAll(arguments));
  }

  @Override
  public void debug(Marker marker, String msg, Throwable t) {
    logger.debug(marker, mask(msg), t);
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.isInfoEnabled(marker);
  }

  @Override
  public void info(Marker marker, String msg) {
    logger.info(marker, mask(msg));
  }

  @Override
  public void info(Marker marker, String format, Object arg) {
    logger.info(marker, mask(format), mask(arg));
  }

  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    logger.info(marker, mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void info(Marker marker, String format, Object... arguments) {
    logger.info(marker, mask(format), maskAll(arguments));
  }

  @Override
  public void info(Marker marker, String msg, Throwable t) {
    logger.info(marker, mask(msg), t);
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.isWarnEnabled(marker);
  }

  @Override
  public void warn(Marker marker, String msg) {
    logger.warn(marker, mask(msg));
  }

  @Override
  public void warn(Marker marker, String format, Object arg) {
    logger.warn(marker, mask(format), mask(arg));
  }

  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    logger.warn(marker, mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void warn(Marker marker, String format, Object... arguments) {
    logger.warn(marker, mask(format), maskAll(arguments));
  }

  @Override
  public void warn(Marker marker, String msg, Throwable t) {
    logger.warn(marker, mask(msg), t);
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.isErrorEnabled(marker);
  }

  @Override
  public void error(Marker marker, String msg) {
    logger.error(marker, mask(msg));
  }

  @Override
  public void error(Marker marker, String format, Object arg) {
    logger.error(marker, mask(format), mask(arg));
  }

  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    logger.error(marker, mask(format), mask(arg1), mask(arg2));
  }

  @Override
  public void error(Marker marker, String format, Object... arguments) {
    logger.error(marker, mask(format), maskAll(arguments));
  }

  @Override
  public void error(Marker marker, String msg, Throwable t) {
    logger.error(marker, mask(msg), t);
  }

  @Override
  public LoggingEventBuilder atTrace() {
    return delegateLoggingEventBuilder(logger.atTrace());
  }

  @Override
  public LoggingEventBuilder atDebug() {
    return delegateLoggingEventBuilder(logger.atDebug());
  }

  @Override
  public LoggingEventBuilder atInfo() {
    return delegateLoggingEventBuilder(logger.atInfo());
  }

  @Override
  public LoggingEventBuilder atWarn() {
    return delegateLoggingEventBuilder(logger.atWarn());
  }

  @Override
  public LoggingEventBuilder atError() {
    return delegateLoggingEventBuilder(logger.atError());
  }

  @Override
  public LoggingEventBuilder makeLoggingEventBuilder(Level level) {
    return delegateLoggingEventBuilder(logger.makeLoggingEventBuilder(level));
  }

  /**
   * {@link LoggingEventBuilder} をラップして、引数に機微な情報マスクを適用します。
   *
   * @param builder 元の {@link LoggingEventBuilder}
   * @return 機微な情報マスクを適用する {@link LoggingEventBuilder}
   */
  private LoggingEventBuilder delegateLoggingEventBuilder(LoggingEventBuilder builder) {
    return new MaskingLoggingEventBuilder(builder, maskingEnabled);
  }

  /**
   * 文字列型の引数を機微な情報マスク処理にかけた結果を返します。
   *
   * @param arg マスク対象の引数
   * @return マスクされた引数。文字列の場合は機微な情報がマスクされ、
   *         文字列以外の場合はそのまま返されます。
   */
  @Nullable
  private Object mask(@Nullable Object arg) {
    if (!maskingEnabled || arg == null) {
      return arg;
    }
    if (arg instanceof String str) {
      return SensitiveDataMasker.mask(str);
    }
    return arg;
  }

  /**
   * 文字列のフォーマットを機微な情報マスク処理にかけた結果を返します。
   *
   * @param format マスク対象のフォーマット文字列
   * @return マスクされたフォーマット文字列
   */
  @Nullable
  private String mask(@Nullable String format) {
    if (!maskingEnabled || format == null) {
      return format;
    }
    return SensitiveDataMasker.mask(format);
  }

  /**
   * 引数配列の各要素に対して機微な情報マスク処理を適用します。
   *
   * @param arguments マスク対象の引数配列
   * @return 機微な情報がマスクされた引数配列
   */
  @Nullable
  private Object[] maskAll(@Nullable Object[] arguments) {
    if (arguments == null) {
      return null;
    }
    Object[] masked = new Object[arguments.length];
    for (int i = 0; i < arguments.length; i++) {
      masked[i] = mask(arguments[i]);
    }
    return masked;
  }

  /**
   * {@link LoggingEventBuilder} をラップして、引数に機微な情報マスクを適用する内部クラスです。
   *
   * <p>SLF4J 2.0のfluent APIで使用される {@link LoggingEventBuilder} の各メソッドを
   * 呼び出す際、引数として渡される機微な個人情報を自動的にマスクします。
   */
  private static class MaskingLoggingEventBuilder implements LoggingEventBuilder {

    private final LoggingEventBuilder delegate;
    private final boolean maskingEnabled;

    /**
     * 指定された {@link LoggingEventBuilder} をラップします。
     *
     * @param delegate ラップ対象の {@link LoggingEventBuilder}
     * @param maskingEnabled 機微な情報マスクを有効にするかどうか
     */
    MaskingLoggingEventBuilder(LoggingEventBuilder delegate, boolean maskingEnabled) {
      this.delegate = delegate;
      this.maskingEnabled = maskingEnabled;
    }

    @Override
    public LoggingEventBuilder addArgument(@Nullable Object p) {
      return delegate.addArgument(maskObject(p));
    }

    @Override
    @SuppressWarnings("unchecked")
    public LoggingEventBuilder addArgument(Supplier<?> objectSupplier) {
      if (!maskingEnabled) {
        return delegate.addArgument(objectSupplier);
      }
      return delegate.addArgument((Supplier<Object>) new MaskingSupplier(objectSupplier));
    }

    @Override
    public LoggingEventBuilder addKeyValue(@Nullable String key, @Nullable Object value) {
      return delegate.addKeyValue(key, maskObject(value));
    }

    @Override
    public LoggingEventBuilder addKeyValue(
        @Nullable String key, Supplier<Object> valueSupplier) {
      if (!maskingEnabled) {
        return delegate.addKeyValue(key, valueSupplier);
      }
      return delegate.addKeyValue(key, new MaskingSupplier(valueSupplier));
    }

    @Override
    public LoggingEventBuilder addMarker(Marker marker) {
      return delegate.addMarker(marker);
    }

    @Override
    public LoggingEventBuilder setCause(Throwable cause) {
      return delegate.setCause(cause);
    }

    @Override
    public LoggingEventBuilder setMessage(@Nullable String message) {
      return delegate.setMessage(maskSensitive(message));
    }

    @Override
    public LoggingEventBuilder setMessage(Supplier<String> messageSupplier) {
      if (!maskingEnabled) {
        return delegate.setMessage(messageSupplier);
      }
      return delegate.setMessage(new MaskingStringSupplier(messageSupplier));
    }

    @Override
    public void log() {
      delegate.log();
    }

    @Override
    public void log(@Nullable String message) {
      delegate.log(maskSensitive(message));
    }

    @Override
    public void log(Supplier<String> messageSupplier) {
      if (!maskingEnabled) {
        delegate.log(messageSupplier);
        return;
      }
      delegate.log(new MaskingStringSupplier(messageSupplier));
    }

    @Override
    public void log(@Nullable String message, @Nullable Object arg) {
      delegate.log(maskSensitive(message), maskObject(arg));
    }

    @Override
    public void log(@Nullable String message, @Nullable Object... args) {
      delegate.log(maskSensitive(message), maskAllArguments(args));
    }

    @Override
    public void log(@Nullable String message, @Nullable Object arg0, @Nullable Object arg1) {
      delegate.log(maskSensitive(message), maskObject(arg0), maskObject(arg1));
    }

    /**
     * 文字列を機微な情報マスク処理にかけた結果を返します。
     *
     * @param value マスク対象の文字列
     * @return マスクされた文字列
     */
    @Nullable
    private String maskSensitive(@Nullable String value) {
      if (!maskingEnabled || value == null) {
        return value;
      }
      return SensitiveDataMasker.mask(value);
    }

    /**
     * オブジェクトを機微な情報マスク処理にかけた結果を返します。
     *
     * @param arg マスク対象のオブジェクト
     * @return マスクされたオブジェクト。文字列の場合は機微な情報がマスクされ、
     *         文字列以外の場合はそのまま返されます。
     */
    @Nullable
    private Object maskObject(@Nullable Object arg) {
      if (!maskingEnabled || arg == null) {
        return arg;
      }
      if (arg instanceof String str) {
        return SensitiveDataMasker.mask(str);
      }
      return arg;
    }

    /**
     * 引数配列の各要素に対して機微な情報マスク処理を適用します。
     *
     * @param arguments マスク対象の引数配列
     * @return 機微な情報がマスクされた引数配列
     */
    @Nullable
    private Object[] maskAllArguments(@Nullable Object[] arguments) {
      if (arguments == null) {
        return null;
      }
      Object[] masked = new Object[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
        masked[i] = maskObject(arguments[i]);
      }
      return masked;
    }

    /**
     * {@link Supplier} をラップして、返される文字列値に機微な情報マスクを適用します。
     */
    @SuppressWarnings("unchecked")
    private static class MaskingSupplier implements Supplier<Object> {

      private final Supplier<?> delegate;

      MaskingSupplier(Supplier<?> delegate) {
        this.delegate = delegate;
      }

      @Override
      public Object get() {
        Object value = delegate.get();
        if (value instanceof String str) {
          return SensitiveDataMasker.mask(str);
        }
        return value;
      }
    }

    /**
     * {@link Supplier<String>} をラップして、返される文字列に機微な情報マスクを適用します。
     */
    private static class MaskingStringSupplier implements Supplier<String> {

      private final Supplier<String> delegate;

      MaskingStringSupplier(Supplier<String> delegate) {
        this.delegate = delegate;
      }

      @Override
      public String get() {
        String value = delegate.get();
        return value != null ? SensitiveDataMasker.mask(value) : null;
      }
    }
  }
}
