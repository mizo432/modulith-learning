package undecided.jbJobMgmt.jobExecution.domain.jobexecution;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * エラーコード、メッセージ、発生箇所を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorInfo {

  @Column(name = "error_code", length = 50)
  private String errorCode;

  @Column(name = "error_message", length = 1000)
  private String errorMessage;

  @Column(name = "error_location", length = 200)
  private String errorLocation;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime occurredAt;

  public ErrorInfo(String errorCode, String errorMessage) {
    if (errorCode == null || errorCode.isBlank()) {
      throw new IllegalArgumentException("Error code must not be null or blank");
    }
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.occurredAt = LocalDateTime.now();
  }

  public ErrorInfo(String errorCode, String errorMessage, String errorLocation) {
    this(errorCode, errorMessage);
    this.errorLocation = errorLocation;
  }
}
