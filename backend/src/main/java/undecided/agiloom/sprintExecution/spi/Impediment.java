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
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "impediments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Impediment {
  @Id private SnowflakeId id;

  @Column(name = "sprint_id", nullable = false)
  private SnowflakeId sprintId;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false, length = 2000)
  private String description;

  @Column(length = 1000)
  private String impact;

  @Column(name = "assignee_id")
  private UUID assigneeId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  private ImpedimentStatus status = ImpedimentStatus.NEW;

  @Column(name = "resolved_at")
  private LocalDateTime resolvedAt;

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

  public void resolve() {
    if (this.status != ImpedimentStatus.IN_PROGRESS) {
      throw new BusinessException(
          "Impediment can only be resolved from IN_PROGRESS status, current status: "
              + this.status);
    }
    this.status = ImpedimentStatus.RESOLVED;
    this.resolvedAt = DateProvider.currentLocalDateTime();
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  public void startWorkingOn() {
    if (this.status != ImpedimentStatus.NEW) {
      throw new BusinessException(
          "Impediment can only start working on from NEW status, current status: " + this.status);
    }
    this.status = ImpedimentStatus.IN_PROGRESS;
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  public void update(
      @NonNull String title,
      @NonNull String description,
      @Nullable String impact,
      @Nullable UUID assigneeId) {
    checkNotNull(title, () -> new BusinessException("title must not be null"));
    checkNotNull(description, () -> new BusinessException("description must not be null"));
    if (title.trim().isEmpty()) {
      throw new BusinessException("title must not be empty");
    }
    if (title.trim().length() > 200) {
      throw new BusinessException("title must not exceed 200 characters");
    }
    if (description.trim().isEmpty()) {
      throw new BusinessException("description must not be empty");
    }
    if (description.trim().length() > 2000) {
      throw new BusinessException("description must not exceed 2000 characters");
    }
    this.title = title;
    this.description = description;
    this.impact = impact;
    this.assigneeId = assigneeId;
    this.updatedAt = DateProvider.currentLocalDateTime();
  }
}
