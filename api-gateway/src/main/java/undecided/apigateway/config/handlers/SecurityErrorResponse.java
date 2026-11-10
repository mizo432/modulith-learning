package undecided.apigateway.config.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import org.jspecify.annotations.NonNull;

/**
 * セキュリティエラー時の共通レスポンスDTO
 *
 * <p>設計書 4.1節で定義されたカスタムレスポンス形式を提供します。
 * 401(認証失敗)、403(認可失敗)などで共通して使用されます。
 *
 * <p>イミュータブルなデータモデルのため、Java record を使用します。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SecurityErrorResponse(@NonNull ErrorDetail error) {

  public SecurityErrorResponse(
      int status, @NonNull String code, @NonNull String message, @NonNull String path) {
    this(new ErrorDetail(status, code, message, Instant.now(), path));
  }

  /**
   * エラー詳細情報
   *
   * <p>イミュータブルなデータモデルのため、Java record を使用します。
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record ErrorDetail(
      int status,
      @NonNull String code,
      @NonNull String message,
      @NonNull Instant timestamp,
      @NonNull String path) {
  }
}
