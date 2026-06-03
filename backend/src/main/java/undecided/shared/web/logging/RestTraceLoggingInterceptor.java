package undecided.shared.web.logging;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * コントローラーハンドラーの実行時間をログに記録するためのインターセプターです。
 *
 * <p>HTTPリクエストが開始されたタイミングと終了したタイミングを記録し、それぞれのハンドリングにかかった時間を測定・ログ出力します。
 * また、一定時間を超える処理が行われた場合は、警告ログとして出力します。
 *
 * <p>このクラスはSpring MVCのHandlerInterceptorインタフェースを実装しており、リクエスト処理の前後に 追加の処理（ログ出力）を行います。
 *
 * <p>ログの出力レベルはWARNおよびTRACEを使用します。処理時間が指定された閾値を超えた場合にWARNログを出力します。
 */
@Setter
public class RestTraceLoggingInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(RestTraceLoggingInterceptor.class);

  /**
   * START_ATTR は、リクエストの開始時刻を保持するための属性キーです。 このキーは、リクエスト処理中のトレース情報を記録するために使用されます。
   * 主に、リクエストの応答時間を計測するために利用されます。 TraceLoggingInterceptor クラス内で使用され、各リクエストのライフサイクルにおける
   * ログ情報を管理する役割を果たします。
   */
  private static final String START_ATTR =
      RestTraceLoggingInterceptor.class.getName() + ".startTime";

  /**
   * `HANDLING_ATTR` は、リクエスト処理時間を記録するために使用される属性名を表す定数です。 この属性は、リクエスト処理のトレースログにおいて識別子として利用されます。
   * 属性名は、`TraceLoggingInterceptor` クラスの完全修飾クラス名と ".handlingTime" を結合した値です。
   *
   * <p>この定数は不変であり、静的かつグローバルに利用可能です。 主に、リクエスト処理の開始時刻や処理時間計測のためのキーとして使用されます。
   */
  private static final String HANDLING_ATTR =
      RestTraceLoggingInterceptor.class.getName() + ".handlingTime";

  private static final long DEFAULT_WARN_NANOS;

  static {
    DEFAULT_WARN_NANOS = TimeUnit.SECONDS.toNanos(3L);
  }

  private long warnHandlingNanos;

  public RestTraceLoggingInterceptor() {
    this.warnHandlingNanos = DEFAULT_WARN_NANOS;
  }

  /**
   * 指定されたHandlerMethodのパラメータリストを構築し、カンマ区切りの文字列として返します。
   *
   * <p>各パラメータの型名を簡単な形式（クラス名のみ、パッケージ名なし）で取得します。
   *
   * @param handlerMethod 対象となるHandlerMethodオブジェクト
   * @return メソッドパラメータの型名をカンマ区切りで結合した文字列
   */
  protected static String buildMethodParams(HandlerMethod handlerMethod) {
    MethodParameter[] params = handlerMethod.getMethodParameters();
    List<String> lst = new ArrayList<>(params.length);

    for (MethodParameter p : params) {
      lst.add(p.getParameterType().getSimpleName());
    }

    return StringUtils.collectionToCommaDelimitedString(lst);
  }

  /**
   * リクエストが処理される前に実行されるメソッドです。 HandlerMethodの情報をログに記録し、リクエスト開始時刻をリクエスト属性に設定します。
   *
   * @param request クライアントからの HTTPリクエスト
   * @param response クライアントへの HTTPレスポンス
   * @param handler 処理対象のハンドラー（通常はHandlerMethodインスタンス）
   * @return 処理を続行する場合はtrueを返します。それ以外の場合はfalseを返します。
   */
  public boolean preHandle(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler) {
    if (handler instanceof HandlerMethod handlerMethod) {
      if (logger.isTraceEnabled()) {
        Method m = handlerMethod.getMethod();
        logger.trace(
            "[START CONTROLLER] {}#{}({})",
            m.getDeclaringClass().getSimpleName(),
            m.getName(),
            buildMethodParams(handlerMethod));
      }

      long startTime = System.nanoTime();
      request.setAttribute(START_ATTR, startTime);
    }

    return true;
  }

  /**
   * リクエスト処理の完了後に実行されるメソッドです。
   * <p>
   * 処理時間の計測結果をログに記録し、必要に応じて警告ログを出力します。
   *
   * @param request クライアントからのHTTP リクエスト
   * @param response クライアントへのHTTP レスポンス
   * @param handler 処理対象のハンドラー（通常はHandlerMethodのインスタンス）
   * @param ex 発生した例外オブジェクト（例外が発生していない場合はnull）
   * @throws Exception 実行中にエラーが発生した場合
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    logger.trace("afterCompletion");
    if (handler instanceof HandlerMethod handlerMethod) {
      long startTime = 0L;
      if (request.getAttribute(START_ATTR) != null) {
        startTime = (Long) request.getAttribute(START_ATTR);
      }

      long handlingTime = System.nanoTime() - startTime;
      request.removeAttribute(START_ATTR);
      request.setAttribute(HANDLING_ATTR, handlingTime);
      String formattedHandlingTime = String.format("%1$,3d", handlingTime);
      boolean isWarnHandling = handlingTime > this.warnHandlingNanos;
      if (isEnabledLogLevel(isWarnHandling)) {
        Method m = handlerMethod.getMethod();
        logger.trace(
            "[END CONTROLLER  ] {}#{}({})",
            m.getDeclaringClass().getSimpleName(),
            m.getName(),
            buildMethodParams(handlerMethod));
        final String handlingTimeMessage = "[HANDLING TIME   ] {}#{}({})-> {} ns";
        if (isWarnHandling) {
          logger.warn(
              handlingTimeMessage + " > {}",
              m.getDeclaringClass().getSimpleName(),
              m.getName(),
              buildMethodParams(handlerMethod),
              formattedHandlingTime,
              this.warnHandlingNanos);
        } else {
          logger.trace(
              handlingTimeMessage,
              m.getDeclaringClass().getSimpleName(),
              m.getName(),
              buildMethodParams(handlerMethod),
              formattedHandlingTime);
        }
      }
    }
  }

  /**
   * 指定された条件に基づいて、ログレベルが有効かどうかを判定します。
   *
   * @param isWarnHandling 警告ログレベル（WARN）をチェックする場合はtrueを渡します。 トレースログレベル（TRACE）をチェックする場合はfalseを渡します。
   * @return 指定されたログレベルが有効な場合はtrue、無効な場合はfalseを返します。
   */
  private boolean isEnabledLogLevel(boolean isWarnHandling) {
    if (isWarnHandling) {
      return logger.isWarnEnabled();
    } else {
      return logger.isTraceEnabled();
    }
  }
}
