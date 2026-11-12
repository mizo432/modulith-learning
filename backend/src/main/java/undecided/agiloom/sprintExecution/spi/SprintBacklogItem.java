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
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprint_backlogs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

  /**
   * 新しいSprintBacklogItemを作成する静的ファクトリーメソッドです。
   *
   * @param id SprintBacklogItemのID
   * @param sprintId スプリントID
   * @param backlogItemId バックログアイテムID
   * @param workDescription 作業説明
   * @param status ステータス（nullの場合TODO）
   * @param assigneeId 担当者ID
   * @param createdAt 作成日時（nullの場合自動設定）
   * @param updatedAt 更新日時（nullの場合自動設定）
   * @return 新しく作成されたSprintBacklogItemインスタンス
   */
  public static SprintBacklogItem of(
      SnowflakeId id,
      SnowflakeId sprintId,
      SnowflakeId backlogItemId,
      String workDescription,
      TaskStatus status,
      UUID assigneeId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    SprintBacklogItem item = new SprintBacklogItem();
    item.id = id;
    item.sprintId = sprintId;
    item.backlogItemId = backlogItemId;
    item.workDescription = workDescription;
    item.status = (status != null) ? status : TaskStatus.TODO;
    item.assigneeId = assigneeId;
    item.createdAt = createdAt;
    item.updatedAt = updatedAt;
    return item;
  }

  public void updateStatus(@NonNull TaskStatus status) {
    if (this.deletedAt != null) {
      throw new BusinessException("SprintBacklogItem is already deleted");
    }
    checkNotNull(status, () -> new BusinessException("status must not be null"));
    this.status = status;
  }

  public void assignTo(@NonNull UUID assigneeId) {
    if (this.deletedAt != null) {
      throw new BusinessException("SprintBacklogItem is already deleted");
    }
    checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
    this.assigneeId = assigneeId;
  }

  public void update(
      @NonNull String workDescription, @NonNull TaskStatus status, @NonNull UUID assigneeId) {
    if (this.deletedAt != null) {
      throw new BusinessException("SprintBacklogItem is already deleted");
    }
    checkNotNull(workDescription, () -> new BusinessException("workDescription must not be null"));
    checkNotNull(status, () -> new BusinessException("status must not be null"));
    checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
    if (workDescription.trim().length() > 2000) {
      throw new BusinessException("workDescription must not exceed 2000 characters");
    }
    this.workDescription = workDescription;
    this.status = status;
    this.assigneeId = assigneeId;
  }

  void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SprintBacklogItem item = (SprintBacklogItem) o;
    return Objects.equals(id, item.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
