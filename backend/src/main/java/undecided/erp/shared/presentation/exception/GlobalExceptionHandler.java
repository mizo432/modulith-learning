package undecided.erp.shared.presentation.exception;

import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import undecided.erp.common.exception.BusinessException;
import undecided.erp.common.primitive.Lists2;

/**
 * アプリケーション全体で発生する例外をハンドリングするクラス。
 *
 * <p>Spring Boot の @RestControllerAdvice アノテーションを使用しており、 グローバル例外処理を提供します。
 *
 * <p>主に、予期しない例外をキャッチし、適切なエラーレスポンスをクライアントに返却します。
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  /**
   * BusinessExceptionをハンドリングし、適切なエラーレスポンスをクライアントに返却します。
   *
   * @param e ハンドリング対象のBusinessExceptionオブジェクト。この例外にはエラーに関連する詳細情報が含まれます。
   * @return エラーレスポンスを表すResponseEntityオブジェクト。エラーの詳細をProblemDetail形式で含みます。
   */
  @ExceptionHandler(BusinessException.class)
  public @NonNull ResponseEntity<ProblemDetail> handleBusinessException(
      @NonNull BusinessException e) {
    Map<String, Object> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, Lists2.getLast(e.getResultMessages().getList()).text()));
  }
}
