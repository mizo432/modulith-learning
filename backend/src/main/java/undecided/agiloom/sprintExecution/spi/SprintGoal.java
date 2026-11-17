package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
 * Sprint目標エンティティ。
 *
 * <p>Sprint の目標を管理します。チームがSprintの期間中に達成すべき目標を明確にし、
 * Sprintの方向性を示すために使用されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "sprint_goals", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sprint_goal_sprint", columnNames = {"sprintId"})
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprintGoal {

  /** Sprint目標の一意な識別子。 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected SnowflakeId id;

  /** 関連するSprintのID。一意制約が設定されており、Sprintごとに1つの目標のみを設定できます。 */
  @Column(name = "sprint_id", nullable = false)
  protected SnowflakeId sprintId;

  /** Sprint目標の詳細な説明。チームが達成すべき具体的な目標を記述します。 */
  @Column(name = "goal_description", nullable = false, length = 1000)
  protected String goalDescription;

  /** 目標に関連する項目の一覧。バックログ項目やタスクのIDなどを記録します。 */
  @Column(name = "related_items", length = 2000)
  protected String relatedItems;

  /** Sprint目標が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** Sprint目標が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** Sprint目標が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    /**
     * Sprint目標を新規作成します。
     *
     * @param id ID
     * @param sprintId Sprint ID
     * @param goalDescription 目標説明
     * @param relatedItems 関連項目
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたSprintGoalインスタンス
     */
    public static SprintGoal create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sprintId,
            @NonNull String goalDescription,
            @Nullable String relatedItems,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @Nullable LocalDateTime deletedAt) {
        return SprintGoal.builder()
                .id(id)
                .sprintId(sprintId)
                .goalDescription(goalDescription)
                .relatedItems(relatedItems)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void update(@NonNull String goalDescription, String relatedItems) {
        this.goalDescription = goalDescription;
        this.relatedItems = relatedItems;
        this.updatedAt = LocalDateTime.now();
    }
}
