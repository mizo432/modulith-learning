package undecided.jbJobMgmt.jobExecution.domain.jobexecution;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 途中再開のためのチェックポイント位置を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checkpoint {

  @Column(name = "checkpoint_position", length = 500)
  private String position;

  @Column(name = "checkpoint_timestamp", nullable = false)
  private LocalDateTime timestamp;

  public Checkpoint(String position) {
    if (position == null || position.isBlank()) {
      throw new IllegalArgumentException("Checkpoint position must not be null or blank");
    }
    this.position = position;
    this.timestamp = LocalDateTime.now();
  }
}
