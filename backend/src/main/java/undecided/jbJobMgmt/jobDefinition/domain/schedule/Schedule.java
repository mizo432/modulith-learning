package undecided.jbJobMgmt.jobDefinition.domain.schedule;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.JobCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 実行日時、繰り返し条件を管理するエンティティです。
 */
@Entity
@Table(name = "schedule")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private JobCode jobCode;

  @Embedded
  private RepetitionCondition repetitionCondition;

  @Embedded
  private ValidPeriod validPeriod;

  @jakarta.persistence.Column(name = "enabled", nullable = false)
  private boolean enabled;

  @jakarta.persistence.Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @jakarta.persistence.Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Schedule(JobCode jobCode, RepetitionCondition repetitionCondition, ValidPeriod validPeriod) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.jobCode = jobCode;
    this.repetitionCondition = repetitionCondition;
    this.validPeriod = validPeriod;
    this.enabled = true;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void changeRepetitionCondition(RepetitionCondition repetitionCondition) {
    if (repetitionCondition == null) {
      throw new IllegalArgumentException("RepetitionCondition must not be null");
    }
    this.repetitionCondition = repetitionCondition;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeValidPeriod(ValidPeriod validPeriod) {
    if (validPeriod == null) {
      throw new IllegalArgumentException("ValidPeriod must not be null");
    }
    this.validPeriod = validPeriod;
    this.updatedAt = LocalDateTime.now();
  }

  public void enable() {
    this.enabled = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void disable() {
    this.enabled = false;
    this.updatedAt = LocalDateTime.now();
  }
}
