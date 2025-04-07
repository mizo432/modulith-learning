package undecided.erp.common.exception;

import javax.annotation.Nonnull;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

/**
 * ResultMessagesLoggingInterceptorは、メソッドインタセプターとして動作し、例外や結果メッセージに関する ログを記録するための機能を提供します。
 * <p>
 * このクラスは、MethodInvocationに基づいてメソッド処理を制御し、発生した例外や通知用の
 * ResultMessagesNotificationExceptionに対応します。また、スレッドローカル変数を使用して 呼び出しの開始ポイントを管理します。
 * <p>
 * 実装するインターフェース: - MethodInterceptor: メソッドインターセプトをサポートします。 - InitializingBean: プロパティ設定後の初期化を提供します。
 * <p>
 * 主な機能: - ResultMessagesNotificationExceptionの検出とログ出力。 - 例外ロギングのカスタマイズが可能なExceptionLoggerの設定と管理。
 */
public class ResultMessagesLoggingInterceptor implements MethodInterceptor, InitializingBean {

  private final ThreadLocal<MethodInvocation> startingPoint = new ThreadLocal<>();
  @Setter
  private ExceptionLogger exceptionLogger = null;

  public ResultMessagesLoggingInterceptor() {
  }

  public Object invoke(@Nonnull MethodInvocation invocation)
      throws Throwable {
    if (this.startingPoint.get() == null) {
      this.startingPoint.set(invocation);
    }

    Object e;
    try {
      e = invocation.proceed();
    } catch (ResultMessagesNotificationException var6) {
      if (this.isStartingPoint(invocation)) {
        this.logResultMessagesNotificationException(var6);
      }

      throw var6;
    } finally {
      if (this.isStartingPoint(invocation)) {
        this.startingPoint.remove();
      }

    }

    return e;
  }

  public void afterPropertiesSet() {
    if (this.exceptionLogger == null) {
      this.exceptionLogger = new ExceptionLogger(this.getClass().getName());
      this.exceptionLogger.afterPropertiesSet();
    }

  }

  protected boolean isStartingPoint(MethodInvocation invocation) {
    return this.startingPoint.get() == invocation;
  }

  protected void logResultMessagesNotificationException(ResultMessagesNotificationException e) {
    this.exceptionLogger.warn(e);
  }

  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;

  }

}
