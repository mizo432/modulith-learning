package undecided.shared.web.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.filter.GenericFilterBean;
import undecided.shared.common.exception.ExceptionLogger;

/**
 * ExceptionLoggingFilterは、フィルタチェーン内で発生する例外をキャッチし、ログへ記録するためのフィルタクラスです。
 * 主にIOException、ServletException、およびRuntimeExceptionを処理します。
 *
 * <p>フィルタチェーン内の次のエレメントを実行中にスローされた例外は、適切なログメソッドによって記録されます。 捕捉した例外は再スローされるため、例外の伝播に影響を与えません。
 */
@Setter
@RequiredArgsConstructor
public class ExceptionLoggingFilter extends GenericFilterBean {

  private ExceptionLogger exceptionLogger;

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
   * @param request エラーログのコンテキストとなるServletRequest
   * @param response エラーログのコンテキストとなるServletResponse
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
