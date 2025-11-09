package undecided.erp.shared.presentation.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * アプリケーション全体で発生する例外をハンドリングするクラス。
 *
 * <p>Spring Boot の @RestControllerAdvice アノテーションを使用しており、 グローバル例外処理を提供します。
 *
 * <p>主に、予期しない例外をキャッチし、適切なエラーレスポンスをクライアントに返却します。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
  /**
   * 例外を処理し、HTTPレスポンスとしてエラーメッセージを返却します。
   *
   * @param e 処理中に発生した例外
   * @return 内部サーバエラー (500) ステータスコードとエラーメッセージを含むレスポンスエンティティ
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception e) {
    Map<String, Object> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
