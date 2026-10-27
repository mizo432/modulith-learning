package undecided.config.presentation.web.exception;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.Setter;
import undecided.shared.exception.ExceptionLogger;

/**
 * ExceptionLoggingFilterは、フィルタチェーン内で発生する例外をキャッチし、ログへ記録するためのフィルタクラスです。
 *
 * <p>主にIOException、ServletException、およびRuntimeExceptionを処理します。
 *
 * <p>フィルタチェーン内の次のエレメントを実行中にスローされた例外は、適切なログメソッドによって記録されます。 捕捉した例外は再スローされるため、例外の伝播に影響を与えません。
 */
public class ExceptionLoggingFilter implements Filter {

  @Setter private ExceptionLogger exceptionLogger;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Filter インターフェースの実装。GenericFilterBean を拡張しないため、CGLIB プロキシによる
    // logger の初期化問題を回避する。
  }

  @Override
  public void destroy() {
    // クリーンアップ処理なし
  }

  /**
   * フィルタチェーン内でリクエストとレスポンスを処理します。 処理の途中で発生する特定の例外(IOException, ServletException,
   * RuntimeException)を捕捉し、 ログに記録した後、例外を再スローします。
   *
   * @param servletRequest クライアントから送信されたリクエスト
   * @param servletResponse サーバーからクライアントへのレスポンス
   * @param filterChain フィルタチェーンオブジェクト。次のフィルタまたはターゲットリソースを呼び出します
   * @throws IOException 入出力エラーが発生した場合
   * @throws ServletException サーブレットに関するエラーが発生した場合
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (IOException e) {
      this.logIOException(e, servletRequest, servletResponse);
      throw e;
    } catch (ServletException e) {
      this.logServletException(e, servletRequest, servletResponse);
      throw e;
    } catch (RuntimeException e) {
      this.logRuntimeException(e, servletRequest, servletResponse);
      throw e;
    }
  }

  /**
   * IOExceptionをログとして記録します。 指定された例外オブジェクト、リクエスト、レスポンスを基にエラーログを出力します。
   *
   * @param ioException 記録するIOExceptionオブジェクト
   * @param request エラーログのコンテキストとなる ServletRequest
   * @param response エラーログのコンテキストとなる ServletResponse
   */
  protected void logIOException(
      IOException ioException, ServletRequest request, ServletResponse response) {
    this.exceptionLogger.error(ioException);
  }

  /**
   * ServletExceptionをログとして記録します。 指定された例外オブジェクト、リクエスト、レスポンスを基にエラーログを出力します。
   *
   * @param servletException 記録するServletExceptionオブジェクト
   * @param request エラーログのコンテキストとなるServletRequest
   * @param response エラーログのコンテキストとなるServletResponse
   */
  protected void logServletException(
      ServletException servletException, ServletRequest request, ServletResponse response) {
    this.exceptionLogger.error(servletException);
  }

  /**
   * RuntimeExceptionをログとして記録します。 指定された例外オブジェクト、リクエスト、レスポンスを基にエラーログを出力します。
   *
   * @param runtimeException 記録するRuntimeExceptionオブジェクト
   * @param request エラーログのコンテキストとなるServletRequest
   * @param response エラーログのコンテキストとなるServletResponse
   */
  protected void logRuntimeException(
      RuntimeException runtimeException, ServletRequest request, ServletResponse response) {
    this.exceptionLogger.error(runtimeException);
  }

  /**
   * ExceptionLoggerインスタンスを取得します。
   *
   * @return このフィルタで使用されているExceptionLoggerオブジェクト
   */
  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;
  }
}
