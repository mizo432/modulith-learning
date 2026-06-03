package undecided.shared.web.exception;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import undecided.shared.common.exception.ExceptionCodeResolver;
import undecided.shared.common.exception.ResultMessagesNotificationException;
import undecided.shared.common.exception.SimpleMappingExceptionCodeResolver;
import undecided.shared.common.message.ResultMessages;

/**
 * SystemExceptionResolverは、Spring MVCのSimpleMappingExceptionResolverを拡張したクラスです。
 *
 * <p>発生した例外を処理し、それに基づいて適切なビューを決定したり、リクエスト属性やレスポンスヘッダーに例外に関する情報を設定します。
 */
public class SystemExceptionResolver extends SimpleMappingExceptionResolver {

  /**
   * 例外処理時にリクエスト属性へ設定される結果メッセージの属性名を表すフィールドです。
   *
   * <p>この属性名は、例外発生時に関連する結果メッセージを HTTP リクエストの属性として アプリケーション内で扱うために使用されます。
   *
   * <p>デフォルト値は {@code ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME} に初期化されます。
   */
  @Setter private String resultMessagesAttribute;

  /**
   * 例外コードを表す属性名を保持する変数です。
   *
   * <p>この変数は例外処理時に、リクエストスコープなどに設定される例外コードのキー名として使用されます。 {@code SystemExceptionResolver}
   * における例外処理の動作をカスタマイズする際に設定できます。
   */
  @Setter private String exceptionCodeAttribute;

  /**
   * このフィールドは、HTTPレスポンスヘッダーで使用される例外コードを指定するためのヘッダー名を保持します。
   *
   * <p>{@code SystemExceptionResolver} のインスタンスでは、このプロパティを使用して
   * サーバーからクライアントへのレスポンスヘッダーに例外コードを設定する際のキーとして扱います。
   *
   * <p>デフォルト値は "X-Exception-Code" に設定されています。
   */
  @Setter private String exceptionCodeHeader;

  /**
   * 例外コードを解決するための {@link ExceptionCodeResolver} を設定します。
   *
   * <p>このフィールドは、例外が発生した際に適切な例外コードを解決するために使用されます。 {@link ExceptionCodeResolver}
   * は、例外オブジェクトを基に例外コードを返すインタフェースです。 このフィールドには任意のカスタム実装を設定できます、デフォルトでは {@code
   * SimpleMappingExceptionCodeResolver} が使用されます。
   *
   * <p>設定された {@code exceptionCodeResolver} は、例外情報の解析やレスポンスに 設定される例外コードの決定を行う内部ロジックで利用されます。
   */
  @Setter private ExceptionCodeResolver exceptionCodeResolver;

  @Nullable private Class<?>[] excludedExceptions;

  /**
   * 例外チェーンを遡り、原因例外を確認するかどうかを示すフラグです。
   *
   * <p>このプロパティが {@code true} に設定されている場合、例外処理時にスローされた 例外の原因チェーンをたどり、適切な例外解決を行います。 {@code false}
   * に設定されている場合は、現在の例外のみが処理されます。
   *
   * <p>デフォルト値は {@code false} です。
   */
  @Setter private boolean checkCause;

  /**
   * サブクラスを考慮するかどうかを判定するフラグを保持します。
   *
   * <p>このフラグが {@code true} に設定されている場合、例外の解決時に対象のクラスの サブクラスも処理の対象とします。{@code false}
   * の場合は、指定されたクラスそのもの のみを対象とします。
   */
  @Setter private boolean checkSubClass;

  /**
   * デフォルト設定で {@code SystemExceptionResolver} のインスタンスを初期化するコンストラクタです。
   *
   * <p>このコンストラクタは、例外処理時の設定を以下の内容で初期化します:
   *
   * <p>- {@code resultMessagesAttribute}: {@code ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME}
   * に設定 - {@code exceptionCodeAttribute}: "exceptionCode" に設定 - {@code exceptionCodeHeader}:
   * "X-Exception-Code" に設定 - {@code exceptionCodeResolver}: {@code
   * SimpleMappingExceptionCodeResolver} インスタンスで初期化 - {@code checkCause}: false に設定 - {@code
   * checkSubClass}: false に設定
   *
   * <p>このクラスは、Spring MVC の {@code SimpleMappingExceptionResolver} を拡張し、
   * 例外に関連するコードやメッセージをリクエストやレスポンスに設定できるように設計されています。
   */
  public SystemExceptionResolver() {
    this.resultMessagesAttribute = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;
    this.exceptionCodeAttribute = "exceptionCode";
    this.exceptionCodeHeader = "X-Exception-Code";
    this.exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
    this.checkCause = false;
    this.checkSubClass = false;
  }

  /**
   * 除外対象の例外クラスを設定します。このメソッドで設定された例外は処理対象から除外されます。
   *
   * @param excludedExceptions 処理対象から除外する例外クラスを可変長引数で指定します
   */
  public void setExcludedExceptions(@Nonnull Class<?>... excludedExceptions) {
    this.excludedExceptions = excludedExceptions;
  }

  /**
   * 例外を解決し、適切なビューを返すメソッドです。 指定された例外に基づき、{@link ModelAndView} を生成します。 必要に応じて例外情報をリクエストやレスポンスに設定します。
   *
   * @param request クライアントから送信されたHTTPリクエスト
   * @param response サーバーからクライアントへのHTTP レスポンス
   * @param handler 例外が発生したハンドラーオブジェクト（nullの場合もあります）
   * @param ex 処理対象の例外
   * @return 解決された例外に基づくビューを表す {@link ModelAndView} オブジェクト。解決できない場合は null を返します。
   */
  @Nullable
  protected ModelAndView doResolveException(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      Object handler,
      @Nonnull Exception ex) {
    ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
    if (modelAndView == null) {
      return null;
    } else {
      this.setExceptionInfo(ex, request, response);
      return modelAndView;
    }
  }

  /**
   * 指定された例外およびリクエストに基づいてビュー名を決定するメソッドです。
   *
   * <p>指定された例外が除外対象の例外リストに含まれる場合や、該当する例外がその原因に含まれる場合、 null を返します。そうでない場合は、親クラスのメソッドを用いてビュー名を決定します。
   *
   * @param ex 処理対象の例外オブジェクト
   * @param request クライアントから送信されたHTTPリクエストオブジェクト
   * @return 決定されたビュー名を示す文字列。ビューが決定できない場合は null を返します。
   */
  @Nullable
  protected String determineViewName(@Nonnull Exception ex, @Nonnull HttpServletRequest request) {
    if (this.excludedExceptions != null) {
      if (this.checkExcludedExceptions(ex)) {
        return null;
      }

      if (this.checkCause) {
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
          if (this.checkExcludedExceptions(cause)) {
            return null;
          }
        }
      }
    }

    return super.determineViewName(ex, request);
  }

  /**
   * 指定された例外が除外対象の例外リストに含まれるかどうかを確認します。
   *
   * <p>除外対象の例外リストは予め設定されている必要があります。
   *
   * @param ex 確認対象の例外オブジェクト
   * @return 指定された例外が除外対象に含まれる場合は true、それ以外の場合は false
   */
  private boolean checkExcludedExceptions(Throwable ex) {
    for (Class<?> excludedException : Objects.requireNonNull(this.excludedExceptions)) {
      if (this.checkSubClass && excludedException.isInstance(ex)
          || !this.checkSubClass && excludedException.equals(ex.getClass())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 指定された例外情報を基に、リクエストおよびレスポンスへ必要な情報を設定します。
   *
   * <p>このメソッドでは内部的に例外コードや結果メッセージの設定を行います。
   *
   * @param ex 処理対象の例外オブジェクト。例外コードやメッセージの解決に使用されます。
   * @param request クライアントから送信されたHTTPリクエストオブジェクト。例外情報が設定されます。
   * @param response サーバーからクライアントへのHTTPレスポンスオブジェクト。例外コードが設定される場合があります。
   */
  protected void setExceptionInfo(
      Exception ex, HttpServletRequest request, HttpServletResponse response) {
    this.setExceptionCode(ex, request, response);
    this.setResultMessages(ex, request);
  }

  /**
   * 指定された例外情報を基に、リクエストおよびレスポンスに例外コードを設定します。
   *
   * @param ex 処理対象の例外オブジェクト。例外コード解決に使用されます。
   * @param request クライアントから送信されたHTTPリクエストオブジェクト。例外コードが属性として設定されます。
   * @param response サーバーからクライアントへのHTTPレスポンスオブジェクト。例外コードがヘッダーとして設定される場合があります。
   */
  protected void setExceptionCode(
      Exception ex, HttpServletRequest request, HttpServletResponse response) {
    String exceptionCode = null;
    if (this.exceptionCodeResolver != null
        && (StringUtils.hasText(this.exceptionCodeAttribute)
            || StringUtils.hasText(this.exceptionCodeHeader))) {
      exceptionCode = this.exceptionCodeResolver.resolveExceptionCode(ex);
    }

    if (exceptionCode != null) {
      if (StringUtils.hasText(this.exceptionCodeAttribute)) {
        request.setAttribute(this.exceptionCodeAttribute, exceptionCode);
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(this.exceptionCodeAttribute, exceptionCode);
      }

      if (StringUtils.hasText(this.exceptionCodeHeader)) {
        response.setHeader(this.exceptionCodeHeader, exceptionCode);
      }
    }
  }

  /**
   * 指定された例外に基づき、リクエストおよびフラッシュマップに結果メッセージを設定します。
   *
   * @param ex 処理対象の例外オブジェクト。この例外に関連する結果メッセージが取得されます。
   * @param request クライアントから送信されたHTTPリクエストオブジェクト。結果メッセージが属性として設定されます。
   */
  protected void setResultMessages(Exception ex, HttpServletRequest request) {
    if (StringUtils.hasText(this.resultMessagesAttribute)) {
      if (ex instanceof ResultMessagesNotificationException) {
        ResultMessages resultMessages =
            ((ResultMessagesNotificationException) ex).getResultMessages();
        request.setAttribute(this.resultMessagesAttribute, resultMessages);
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(this.resultMessagesAttribute, resultMessages);
      }
    }
  }
}
