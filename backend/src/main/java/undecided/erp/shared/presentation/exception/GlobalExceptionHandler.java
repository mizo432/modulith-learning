package undecided.erp.shared.presentation.exception;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import undecided.shared.common.exception.ResultMessagesNotificationException;
import undecided.shared.common.primitiveOld.Lists2;

import java.util.HashMap;
import java.util.Map;

import static undecided.shared.common.precondition.ObjectPrecondition.checkNotNull;

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
    @ExceptionHandler(ResultMessagesNotificationException.class)
    public @NonNull ResponseEntity<ProblemDetail> handleBusinessException(
            @NonNull ResultMessagesNotificationException e) {
        checkNotNull(e, () -> new NullPointerException("e must not be null."));
        Map<String, Object> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST, Lists2.getLast(e.getResultMessages().getList()).text()));
    }
}
