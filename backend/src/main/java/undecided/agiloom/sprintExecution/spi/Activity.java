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
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "activities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

  /**
   * 新しいActivityを作成する静的ファクトリーメソッドです。
   *
   * @param id ActivityのID
   * @param sprintId スプリントID
   * @param actorId 実行者ID
   * @param activityType アクティビティタイプ
   * @param description 説明
   * @param createdAt 作成日時（nullの場合自動設定）
   * @return 新しく作成されたActivityインスタンス
   */
  public static Activity of(
      SnowflakeId id,
      SnowflakeId sprintId,
      UUID actorId,
      ActivityType activityType,
      String description,
      LocalDateTime createdAt) {
    Activity activity = new Activity();
    activity.id = id;
    activity.sprintId = sprintId;
    activity.actorId = actorId;
    activity.activityType = activityType;
    activity.description = description;
    activity.createdAt = createdAt;
    return activity;
  }

  public void update(@NonNull ActivityType activityType, String description) {
    checkNotNull(activityType, () -> new BusinessException("activityType must not be null"));
    if (description != null && description.trim().length() > 2000) {
      throw new BusinessException("description must not exceed 2000 characters");
    }
    this.activityType = activityType;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Activity activity = (Activity) o;
    return Objects.equals(id, activity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
