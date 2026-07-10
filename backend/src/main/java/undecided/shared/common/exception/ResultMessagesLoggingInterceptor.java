package undecided.shared.common.exception;

import jakarta.annotation.Nonnull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import undecided.shared.common.message.StandardResultMessageType;

/**
 * メソッド呼び出しをインターセプトし、例外のログ記録やスレッドローカルなコンテキスト管理を行うインターセプタです。
 *
 * <p>このクラスは、指定された {@link MethodInvocation} の呼び出しをラップし、処理の前後で必要なロジックを挿入します。 特に {@link
 * ResultMessagesNotificationException} やその他の例外のログ記録を行う役割を持ちます。
 *
 * <p>主な機能: - メソッドの呼び出しにおける開始ポイントの管理。 - 例外のログ記録。 - Springフレームワークの {@link InitializingBean}
 * に基づいた初期化処理。
 *
 * <p>注意事項: - 本クラスを使用する場合は、インターセプト対象のメソッドが適切に設定されていることを確認してください。 -
 * スレッドごとに固有のコンテキスト情報を保持するため、複数スレッド環境で正確に動作することを保証します。
 */
@Slf4j
public class ResultMessagesLoggingInterceptor implements MethodInterceptor, InitializingBean {

  /**
   * {@link MethodInvocation} を格納するためのスレッドローカル変数。
   *
   * <p>このフィールドは、メソッド呼び出しの開始ポイントを追跡する目的で使用されます。特に、 処理のインターセプト中にメソッド呼び出しがどのようにネストまたはチェーンされるかを管理するために
   * 必要不可欠な情報を保持します。
   *
   * <p>主な特徴: - スレッドごとに固有の {@link MethodInvocation} インスタンスを保持します。 -
   * フィールドへのアクセスはスレッドセーフであり、複数スレッド環境下でも 隔離されたコンテキストで動作します。 -
   * このフィールドを使用して、メソッドの特定の呼び出しが「開始ポイント」であるかどうかを判定します。
   *
   * <p>注意事項: - このフィールドは {@code final} 修飾子が付けられているため、一度設定された値を変更することはできません。 -
   * 値の初期化およびクリアは、一貫性のあるスレッドロックなしの状態を保障するために適切に実施する必要があります。
   */
  private final ThreadLocal<MethodInvocation> startingPoint = new ThreadLocal<>();

  /**
   * 例外のロギングを行うための {@link ExceptionLogger} インスタンスを保持するためのフィールド。
   *
   * <p>主にメソッドの実行中に発生した例外の記録を目的として使用されます。このフィールドは Setter メソッドによって設定可能であり、{@code afterPropertiesSet}
   * メソッドで 初期化が補完される場合もあります。
   *
   * <p>注意事項: - このフィールドが未設定の場合には、クラスの初期化時にデフォルトの値が設定される 場合があります。 -
   * このインスタンスを利用して例外の詳細を適切にログに記録する責任があります。
   */
  @Setter
  private ExceptionLogger exceptionLogger = null;

  /**
   * メソッド呼び出しをインターセプトし、指定された処理を実行します。
   *
   * @param invocation インターセプト対象のメソッド呼び出しを表す {@link MethodInvocation} オブジェクト
   * @return メソッド呼び出しの結果として返されるオブジェクト
   * @throws Throwable メソッドの実行中に発生した例外
   */
  public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
    boolean isFirstCall = markStartingPointIfNecessary(invocation);

    try {
      return invocation.proceed();
    } catch (ResultMessagesNotificationException ex) {
      if (isFirstCall) {
        switch (ex.getResultMessages().getType()) {
          case StandardResultMessageType.ERROR:
            log.error("msg p1", ex);
            exceptionLogger.error(ex);
            break;
          case StandardResultMessageType.WARNING:
            exceptionLogger.warn(ex);
            log.error("msg p2", ex);
            break;
          case StandardResultMessageType.INFO:
            exceptionLogger.info(ex);
            log.error("msg p3", ex);
            break;
          case StandardResultMessageType.DANGER:
            exceptionLogger.log(ex);
            log.error("msg p4", ex);
            break;
          default:
            log.error("msg p5", ex);
        }
      }
      throw ex;
    } catch (Throwable ex) {
      log.error("msg p6", ex);
      throw ex;
    } finally {
      if (isFirstCall) {
        startingPoint.remove();
      }
    }
  }

  /**
   * {@code afterPropertiesSet} メソッドは、Springフレームワークの {@link InitializingBean} インターフェースに基づき、
   * プロパティ設定後に呼び出される初期化処理を実行します。
   *
   * <p>このメソッドの主な機能: - {@code exceptionLogger} プロパティが設定されていない場合、クラス名を使用して新しい
   * {@link ExceptionLogger} インスタンスを作成し設定します。 - 作成した {@code exceptionLogger} の初期化処理を実行します。
   *
   * <p>この処理により、例外のロギング処理が正常に機能する状態を保証します。
   *
   * <p>注意事項: - {@code exceptionLogger} が既に設定されている場合、新しいインスタンスは作成されません。 -
   * このメソッドは通常、Springコンテナにより自動的に呼び出されます。
   */
  public void afterPropertiesSet() {
    if (this.exceptionLogger != null) {
      return;
    }
    this.exceptionLogger = new ExceptionLogger(this.getClass().getName());
    this.exceptionLogger.afterPropertiesSet();
  }

  /**
   * 指定された {@link MethodInvocation} インスタンスが呼び出しの開始ポイントであるかを判定します。
   *
   * @param invocation 判定対象の {@link MethodInvocation} インスタンス
   * @return {@code true} の場合、指定された {@link MethodInvocation} が開始ポイントであることを示します。 {@code false}
   * の場合、異なります。
   */
  protected boolean isStartingPoint(MethodInvocation invocation) {
    return this.startingPoint.get() == invocation;
  }

  /**
   * 必要に応じて開始ポイントをマークします。
   *
   * <p>与えられた {@link MethodInvocation} インスタンスが開始ポイントとして設定されていない場合、
   * このメソッドはそのインスタンスを開始ポイントとして設定し、{@code true} を返します。 既に開始ポイントが設定されている場合は何も行わず、{@code false} を返します。
   *
   * @param invocation 開始ポイントとして判定・設定する対象の {@link MethodInvocation} インスタンス
   * @return {@code true} の場合、指定された {@link MethodInvocation} が開始ポイントとして設定されたことを示します。 {@code false}
   * の場合、既に開始ポイントが設定されていることを示します。
   */
  private boolean markStartingPointIfNecessary(MethodInvocation invocation) {
    if (startingPoint.get() == null) {
      startingPoint.set(invocation);
      return true;
    }
    return false;
  }

  /**
   * {@code ResultMessagesNotificationException} 型の例外をログに記録します。
   *
   * <p>主に {@code WARN} レベルのログを使用して、例外の詳細を記録します。 このメソッドは、例外の内容がデバッグやモニタリングの目的で 記録されるべき場合に呼び出されます。
   *
   * @param e ログに記録する対象の {@code ResultMessagesNotificationException} のインスタンス
   */
  protected void logResultMessagesNotificationException(ResultMessagesNotificationException e) {
    this.exceptionLogger.warn(e);
  }
}
