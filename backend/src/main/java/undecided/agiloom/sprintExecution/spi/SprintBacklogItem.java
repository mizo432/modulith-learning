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
 * Sprintバックログ項目エンティティ。
 *
 * <p>Sprint に含まれるバックログ項目を管理します。タスクやストーリーの状態、担当者、進捗状況を追跡し、
 * Sprintの進捗を可視化するために使用されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "sprint_backlogs")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprintBacklogItem {

  /** バックログ項目の一意な識別子。 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected SnowflakeId id;

  /** 関連するSprintのID。 */
  @Column(name = "sprint_id", nullable = false)
  protected SnowflakeId sprintId;

  /** バックログ項目のID。 */
  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  /** 作業の詳細な説明。 */
  @Column(name = "work_description", length = 2000)
  protected String workDescription;

  /** タスクの状態（TODO, IN_PROGRESS, DONEなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected TaskStatus status = TaskStatus.TODO;

  /** 担当者のID。nullの場合は未割り当てです。 */
  @Column(name = "assignee_id")
  protected UUID assigneeId;

  /** バックログ項目が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** バックログ項目が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** バックログ項目が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    /**
     * Sprintバックログ項目を新規作成します。
     *
     * @param id ID
     * @param sprintId Sprint ID
     * @param backlogItemId バックログ項目ID
     * @param workDescription 作業説明
     * @param status 状態
     * @param assigneeId 担当者ID
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたSprintBacklogItemインスタンス
     */
    public static SprintBacklogItem create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sprintId,
            @NonNull SnowflakeId backlogItemId,
            @Nullable String workDescription,
            @NonNull TaskStatus status,
            @Nullable UUID assigneeId,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @Nullable LocalDateTime deletedAt) {
        return SprintBacklogItem.builder()
                .id(id)
                .sprintId(sprintId)
                .backlogItemId(backlogItemId)
                .workDescription(workDescription)
                .status(status)
                .assigneeId(assigneeId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void updateStatus(@NonNull TaskStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(UUID assigneeId) {
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String workDescription, TaskStatus status, UUID assigneeId) {
        this.workDescription = workDescription;
        this.status = status;
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }
}
