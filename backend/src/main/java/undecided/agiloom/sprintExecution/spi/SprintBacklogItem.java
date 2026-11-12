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
import jakarta.persistence.PreUpdate;
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
@Table(name = "sprint_backlogs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklogItem {
  @Id private SnowflakeId id;

  @Column(name = "sprint_id", nullable = false)
  private SnowflakeId sprintId;

  @Column(name = "backlog_item_id", nullable = false)
  private SnowflakeId backlogItemId;

  @Column(name = "work_description", length = 2000)
  private String workDescription;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  private TaskStatus status = TaskStatus.TODO;

  @Column(name = "assignee_id")
  private UUID assigneeId;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null) {
      this.createdAt = DateProvider.currentLocalDateTime();
    }
    if (this.updatedAt == null) {
      this.updatedAt = DateProvider.currentLocalDateTime();
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  public void updateStatus(@NonNull TaskStatus status) {
    checkNotNull(status, () -> new BusinessException("status must not be null"));
    this.status = status;
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  public void assignTo(@NonNull UUID assigneeId) {
    checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
    this.assigneeId = assigneeId;
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  public void update(
      @NonNull String workDescription, @NonNull TaskStatus status, @NonNull UUID assigneeId) {
    checkNotNull(workDescription, () -> new BusinessException("workDescription must not be null"));
    checkNotNull(status, () -> new BusinessException("status must not be null"));
    checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
    if (workDescription.trim().length() > 2000) {
      throw new BusinessException("workDescription must not exceed 2000 characters");
    }
    this.workDescription = workDescription;
    this.status = status;
    this.assigneeId = assigneeId;
    this.updatedAt = DateProvider.currentLocalDateTime();
  }
}
