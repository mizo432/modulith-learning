package undecided.config.presentation.exception;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import undecided.config.presentation.web.idempotency.IdempotencyKeyException;
import undecided.shared.exception.ExceptionLogger;
import undecided.shared.exception.ResultMessagesNotificationException;
import undecided.shared.primitiveOld.Lists2;

/**
 * アプリケーション全体で発生する例外をハンドリングするクラス。
 *
 * <p>Spring Boot の @RestControllerAdvice アノテーションを使用しており、 グローバル例外処理を提供します。
 *
 * <p>主に、予期しない例外をキャッチし、適切なエラーレスポンスをクライアントに返却します。
 */
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private final ExceptionLogger log;

  /**
   * {@code ResultMessagesNotificationException} をハンドリングし、適切なエラーレスポンスを構築して返却します。
   *
   * <p>エラー詳細として、最後の結果メッセージをレスポンスに含めています。
   *
   * @param e 処理対象の {@code ResultMessagesNotificationException} オブジェクト。この例外には通知すべき結果メッセージが含まれます。
   * @return エラーの詳細情報を含む {@code ProblemDetail} オブジェクト。HTTPステータスと最後のメッセージが格納されています。
   */
  @ExceptionHandler(ResultMessagesNotificationException.class)
  public @NonNull ProblemDetail handleBusinessException(
      @NonNull ResultMessagesNotificationException e) {
    checkNotNull(e, () -> new NullPointerException("e must not be null."));
    Map<String, Object> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, Lists2.getLast(e.getResultMessages().getList()).text());
  }

  /**
   * {@code MethodArgumentNotValidException} をハンドリングし、バリデーションエラーに基づく適切なエラーレスポンスを返却します。
   *
   * @param ex 処理対象の {@code MethodArgumentNotValidException}
   *     オブジェクト。この例外には違反しているフィールドとエラーメッセージの詳細が含まれます。
   * @param request エラーが発生した際のリクエスト情報を表す {@code HttpServletRequest} オブジェクト。
   * @return エラーの詳細情報を含む {@code ProblemDetail}
   *     オブジェクト。HTTPステータス、タイトル、インスタンスURI、エラーコード、フィールドエラーの詳細、トレースIDが含まれます。
   */
  // --- Validation ---
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @NonNull ProblemDetail handleValidation(
      @NonNull MethodArgumentNotValidException ex, @NonNull HttpServletRequest request) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    problem.setTitle("Validation Error");
    problem.setInstance(URI.create(request.getRequestURI()));
    problem.setProperty("errorCode", "VALIDATION_FAILED");
    problem.setProperty(
        "fieldErrors",
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> Map.of("field", e.getField(), "message", e.getDefaultMessage()))
            .toList());
    problem.setProperty("traceId", MDC.get("traceId"));
    return problem;
  }

  /**
   * {@code ConstraintViolationException} をハンドリングし、バリデーション違反に基づくエラーレスポンスを構築して返却します。
   *
   * @param ex 処理対象の {@code ConstraintViolationException}
   *     オブジェクト。この例外には違反しているプロパティパスとエラーメッセージの詳細が含まれます。
   * @param request 問題が発生したリクエスト情報を表す {@code HttpServletRequest} オブジェクト。
   * @return エラーの詳細情報を含む {@code ProblemDetail} オブジェクト。HTTPステータス、エラータイトル、インスタンスURI、エラーコード、
   *     違反項目の詳細が含まれます。
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public @NonNull ProblemDetail handleConstraintViolation(
      @NonNull ConstraintViolationException ex, @NonNull HttpServletRequest request) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Constraint violation");
    problem.setTitle("Validation Error");
    problem.setInstance(URI.create(request.getRequestURI()));
    problem.setProperty("errorCode", "CONSTRAINT_VIOLATION");
    problem.setProperty(
        "violations",
        ex.getConstraintViolations().stream()
            .map(v -> Map.of("path", v.getPropertyPath().toString(), "message", v.getMessage()))
            .toList());
    return problem;
  }

  /**
   * 未処理の例外をハンドリングし、適切なエラーレスポンスを構築して返却します。
   *
   * <p>リクエスト情報やトレースIDを含むエラー詳細をレスポンスに含めます。
   *
   * @param ex 処理対象の {@code Exception} オブジェクト。この例外は未処理の例外を表します。
   * @param request エラーが発生した際のリクエスト情報を表す {@code HttpServletRequest} オブジェクト。
   * @return エラーの詳細情報を含む {@code ProblemDetail} オブジェクト。HTTPステータス、エラータイトル、
   *     エラーメッセージ、インスタンスURI、エラーコード、トレースIDが含まれます。
   */
  /**
   * {@code IdempotencyKeyException} をハンドリングし、409 Conflict のエラーレスポンスを返却します。
   *
   * <p>同じ冪等性キーを持つリクエストが重複して送信された場合に発生します。
   *
   * @param e 処理対象の {@code IdempotencyKeyException}
   * @return 409 Conflict の {@code ProblemDetail} オブジェクト
   */
  @ExceptionHandler(IdempotencyKeyException.class)
  public @NonNull ProblemDetail handleIdempotencyKey(@NonNull IdempotencyKeyException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    problem.setTitle("Idempotency Key Conflict");
    problem.setProperty("errorCode", "IDEMPOTENCY_KEY_CONFLICT");
    return problem;
  }

  @ExceptionHandler(Exception.class)
  public @NonNull ProblemDetail handleUnexpected(
      @NonNull Exception ex, @NonNull HttpServletRequest request) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    try {
      MDC.put("httpMethod", method);
      MDC.put("requestUri", uri);
      log.error(ex);
    } finally {
      MDC.remove("httpMethod");
      MDC.remove("requestUri");
    }
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please contact support with the traceId.");
    problem.setTitle("Internal Server Error");
    problem.setInstance(URI.create(request.getRequestURI()));
    problem.setProperty("errorCode", "INTERNAL_ERROR");
    problem.setProperty("traceId", MDC.get("traceId"));
    return problem;
  }
}
