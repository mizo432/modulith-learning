package undecided.shared.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import undecided.shared.common.exception.ExceptionLogger;

/**
 * HandlerExceptionResolverLoggingInterceptorクラスは、SpringフレームワークのMethodInterceptorを実装したクラスです。
 *
 * <p>HandlerExceptionResolverをターゲットにしたメソッド呼び出しをインターセプトし、例外発生時のログ処理を制御します。
 *
 * <p>主な機能:
 *
 * <p>1. 指定したターゲットがHandlerExceptionResolverインタフェースを実装していない場合は警告ログを出力します。 2.
 * 例外情報に基づき、特定の条件下で例外のロギングを実行します。 3. 発生した例外とHTTPレスポンスのステータスコードに応じて、ログレベルを変更してログ出力を行います。
 *
 * <p>構成要素:
 *
 * <p>- exceptionLogger: 例外メッセージを出力するためのロガー。具体的なロギング実装に委譲されます。 - Logger: ログ出力を制御するためのロガーインスタンス。
 *
 * <p>注意:
 *
 * <p>- ターゲットオブジェクトがHandlerExceptionResolverを実装していない場合、処理を進めず警告ログのみを出力します。 -
 * レスポンスステータスコードの範囲に基づいて異なるロギングレベルが設定されるため、ロギング設定が適切であることを確認してください。
 */
@Setter
@NoArgsConstructor
public class HandlerExceptionResolverLoggingInterceptor
    implements MethodInterceptor, InitializingBean {

  /**
   * ログ出力を行うためのロガーオブジェクト。
   *
   * <p>このクラスにおける主な目的は、{@code HandlerExceptionResolverLoggingInterceptor} クラスの
   * 実行中に記録が必要な情報や例外のログを記録することです。
   *
   * <p>「ログ出力」には例外やHTTPリクエスト・レスポンスのコンテキストに基づくログ分類が含まれます。 主にサーバーエラーやクライアントエラー、リダイレクト、成功メッセージ等のレベルに応じて
   * 適切にログを記録します。
   */
  private static final Logger logger =
      LoggerFactory.getLogger(HandlerExceptionResolverLoggingInterceptor.class);

  /**
   * 例外のログ記録を処理するためのコンポーネントを保持するフィールドです。
   *
   * <p>このオブジェクトは、{@link HandlerExceptionResolverLoggingInterceptor} クラス内で 例外のログ記録を実行する際に使用されます。
   *
   * <p>主に、{@link HandlerExceptionResolver} を実装したオブジェクトに対する メソッド呼び出し処理や、HTTPリクエストおよびレスポンスに関連する
   * 例外情報のログ記録に利用されます。
   *
   * <p>このフィールドは不変であり、初期化後に変更されることはありません。
   */
  private ExceptionLogger exceptionLogger;

  /**
   * ログ記録時に無視される例外クラスを保持するセット。
   *
   * <p>このセットに含まれる例外クラスに一致する場合、それらの例外は処理対象外として扱われ、 ログ記録が行われません。
   *
   * <p>このフィールドは、例外処理の柔軟性を向上させるために使用され、 通常、特定の要件に基づき、無視すべき例外クラスを明示的に設定して使用されます。
   */
  private Set<Class<? extends Exception>> ignoreExceptions = new HashSet<>();

  /**
   * 指定された {@link MethodInvocation} を処理し、必要に応じて例外のログ記録を行います。 メソッドの実行結果を返します。ターゲットオブジェクトが {@link
   * HandlerExceptionResolver} を 実装していない場合、警告ログが出力されます。 特定の例外が処理対象の場合、例外情報を基にログを記録します。
   *
   * @param invocation 実行対象のメソッド呼び出し情報をカプセル化した {@link MethodInvocation} オブジェクト
   * @return メソッド実行結果のオブジェクト。プロセス内で生成された戻り値が返されます。 戻り値が null の場合は null を返します。
   * @throws Throwable 実行中に発生した例外。ターゲットメソッドの実行中にスローされた例外も含まれます。
   */
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object returnObj = invocation.proceed();
    if (returnObj == null) {
      return null;
    } else {
      Object targetObject = invocation.getThis();
      if (!(targetObject instanceof HandlerExceptionResolver)) {
        if (logger.isWarnEnabled()) {
          assert targetObject != null;
          logger.warn(
              "target object does not implement the HandlerExceptionResolver interface. target object is '{}'.",
              targetObject.getClass().getName());
        }

      } else {
        Exception exception = (Exception) invocation.getArguments()[3];
        if (this.isTargetException(exception)) {
          HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
          HttpServletResponse response = (HttpServletResponse) invocation.getArguments()[1];
          Object handler = invocation.getArguments()[2];
          this.log(exception, request, response, handler);
        }
      }
      return returnObj;
    }
  }

  /**
   * 指定された例外がターゲットとして処理されるべき例外であるかを判定します。 `ignoreExceptions` フィールドに設定されている例外クラスに一致する場合は、
   * 対象外として判定されます。
   *
   * @param ex 判定対象の例外オブジェクト
   * @return ターゲット例外である場合はtrue、`ignoreExceptions` の例外クラスに一致する場合はfalse
   */
  protected boolean isTargetException(Exception ex) {
    if (this.ignoreExceptions != null) {
      for (Class<? extends Exception> ignoreClass : this.ignoreExceptions) {
        if (ignoreClass.isInstance(ex)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 指定された例外およびリクエスト・レスポンス情報を基に、HTTPステータスコードに応じたログ出力を行います。 ステータスコードが100～199の場合は情報ログ、200～299の場合は成功ログ、
   * 300～399の場合はリダイレクトログ、400～499の場合はクライアントエラーログ、 500以上の場合はサーバーエラーログとして処理されます。
   *
   * @param exception ログに記録する例外オブジェクト
   * @param request ログのコンテキストとなるHttpServletRequest
   * @param response ログのコンテキストとなるHttpServletResponse
   * @param handler 対象となるハンドラーオブジェクト（コントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void log(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    int statusCode = response.getStatus();
    if (500 <= statusCode) {
      this.logServerError(exception, request, response, handler);
    } else if (400 <= statusCode) {
      this.logClientError(exception, request, response, handler);
    } else if (300 <= statusCode) {
      this.logRedirection(exception, request, response, handler);
    } else if (200 <= statusCode) {
      this.logSuccess(exception, request, response, handler);
    } else if (100 <= statusCode) {
      this.logInformational(exception, request, response, handler);
    }
  }

  /**
   * 指定された例外を情報ログとして記録します。
   *
   * @param exception ログに記録する例外オブジェクト
   * @param request ログのコンテキストとなるHttpServletRequest
   * @param response ログのコンテキストとなるHttpServletResponse
   * @param handler 対象となるハンドラーオブジェクト（コントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void logInformational(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.info(exception);
  }

  /**
   * 成功ログを記録するためのメソッドです。
   *
   * @param exception 記録する例外オブジェクト
   * @param request ログ記録時のコンテキストとなるHttpServletRequest
   * @param response ログ記録時のコンテキストとなるHttpServletResponse
   * @param handler 実行対象のハンドラーオブジェクト（通常はコントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void logSuccess(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.info(exception);
  }

  /**
   * 指定された例外、リクエスト情報、レスポンス情報、およびハンドラー情報を基に、 リダイレクトに関するログを記録します。
   *
   * @param exception ログに記録する例外オブジェクト
   * @param request ログ記録時のコンテキストとなる HttpServletRequest
   * @param response ログ記録時のコンテキストとなる HttpServletResponse
   * @param handler 実行対象のハンドラーオブジェクト （通常はコントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void logRedirection(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.info(exception);
  }

  /**
   * クライアントエラーに対する例外情報をログ出力します。
   *
   * @param exception ログに記録する例外オブジェクト
   * @param request ログ記録時のコンテキストとなる HttpServletRequest
   * @param response ログ記録時のコンテキストとなる HttpServletResponse
   * @param handler 実行対象のハンドラーオブジェクト（通常はコントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void logClientError(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.warn(exception);
  }

  /**
   * サーバーエラーに関する例外情報をログ出力します。
   *
   * @param exception ログに記録する例外オブジェクト
   * @param request ログ記録のコンテキストとなる HttpServletRequest
   * @param response ログ記録のコンテキストとなる HttpServletResponse
   * @param handler 実行対象のハンドラーオブジェクト（通常はコントローラやそのメソッド、またはハンドラインターセプターなど）
   */
  protected void logServerError(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.error(exception);
  }

  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;
  }

  /**
   * Beanのプロパティが設定された後に呼び出されるメソッドで、Spring InitializingBeanインターフェースの一部として実装されています。
   * このメソッドをオーバーライドし、初期化処理を実行します。 実装クラスでは、このメソッドを利用して必要な初期化作業を行せられまます。
   *
   * @throws Exception 初期化処理中に発生した例外
   */
  @Override
  public void afterPropertiesSet() throws Exception {}
}
