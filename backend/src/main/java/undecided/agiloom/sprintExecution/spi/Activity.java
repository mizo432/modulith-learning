package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * アクティビティエンティティ。
 *
 * <p>Sprint 内のアクティビティを記録します。チームメンバーの行動や出来事を追跡し、
 * Sprintの進捗状況を可視化するために使用されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "activities")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Activity {

  /** アクティビティの一意な識別子。 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected SnowflakeId id;

  /** 関連するSprintのID。 */
  @Column(name = "sprint_id", nullable = false)
  protected SnowflakeId sprintId;

  /** アクティビティを実行したユーザーのID。 */
  @Column(name = "actor_id", nullable = false)
  protected UUID actorId;

  /** アクティビティの種類（TASK_CREATED, TASK_UPDATED, COMMENT_ADDEDなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(name = "activity_type", nullable = false, length = 30)
  protected ActivityType activityType;

  /** アクティビティの詳細な説明。 */
  @Column(length = 2000)
  protected String description;

  /** アクティビティが作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

    /**
     * アクティビティを新規作成します。
     *
     * @param id ID
     * @param sprintId Sprint ID
     * @param actorId アクター ID
     * @param activityType アクティビティ種別
     * @param description 説明
     * @param createdAt 作成日時
     * @return 新規作成されたActivityインスタンス
     */
    public static Activity create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sprintId,
            @NonNull UUID actorId,
            @NonNull ActivityType activityType,
            @Nullable String description,
            @NonNull LocalDateTime createdAt) {
        return Activity.builder()
                .id(id)
                .sprintId(sprintId)
                .actorId(actorId)
                .activityType(activityType)
                .description(description)
                .createdAt(createdAt)
                .build();
    }
}
