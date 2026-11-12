package undecided.agiloom.sprintExecution.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import com.github.mizo432.shared.common.dateProvider.DateProvider;
import com.github.mizo432.shared.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "activities")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
  @Id private SnowflakeId id;

  @Column(name = "sprint_id", nullable = false)
  private SnowflakeId sprintId;

  @Column(name = "actor_id", nullable = false)
  private UUID actorId;

  @Enumerated(EnumType.STRING)
  @Column(name = "activity_type", nullable = false, length = 30)
  private ActivityType activityType;

  @Column(length = 2000)
  private String description;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null) {
      this.createdAt = DateProvider.currentLocalDateTime();
    }
  }

  public void update(@NonNull ActivityType activityType, String description) {
    checkNotNull(activityType, () -> new BusinessException("activityType must not be null"));
    if (description != null && description.trim().length() > 2000) {
      throw new BusinessException("description must not exceed 2000 characters");
    }
    this.activityType = activityType;
    this.description = description;
  }
}
