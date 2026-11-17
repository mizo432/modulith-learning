package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

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
 * 障害事項エンティティ。
 *
 * <p>Sprint 内の障害事項を管理します。開発の進捗を妨げる要因を記録し、チームが協力して解決するために使用されます。
 * 障害事項は状態遷移（NEW → IN_PROGRESS → RESOLVED）を持ち、適切なフローで管理されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "impediments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Impediment {

  /** 障害事項の一意な識別子。 */
  @Id
  protected SnowflakeId id;

  /** 関連するSprintのID。 */
  @Column(name = "sprint_id", nullable = false)
  protected SnowflakeId sprintId;

  /** 障害事項のタイトル。簡潔に問題を表現します。 */
  @Column(nullable = false, length = 200)
  protected String title;

  /** 障害事項の詳細な説明。問題の内容や背景を記述します。 */
  @Column(nullable = false, length = 2000)
  protected String description;

  /** 障害事項が与える影響。開発進捗やビジネスへの影響を記述します。 */
  @Column(length = 1000)
  protected String impact;

  /** 障害事項の担当者のID。nullの場合は未割り当てです。 */
  @Column(name = "assignee_id")
  protected UUID assigneeId;

  /** 障害事項の状態（NEW, IN_PROGRESS, RESOLVEDなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected ImpedimentStatus status = ImpedimentStatus.NEW;

  /** 障害事項が解決された日時。nullの場合は未解決です。 */
  @Column(name = "resolved_at")
  protected LocalDateTime resolvedAt;

  /** 障害事項が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** 障害事項が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** 障害事項が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    /**
     * 障害事項を新規作成します。
     *
     * @param id ID
     * @param sprintId Sprint ID
     * @param title タイトル
     * @param description 説明
     * @param impact 影響
     * @param assigneeId 担当者ID
     * @param status 状態
     * @param resolvedAt 解決日時
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたImpedimentインスタンス
     */
    public static Impediment create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sprintId,
            @NonNull String title,
            @NonNull String description,
            @Nullable String impact,
            @Nullable UUID assigneeId,
            @NonNull ImpedimentStatus status,
            @Nullable LocalDateTime resolvedAt,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @Nullable LocalDateTime deletedAt) {
        return Impediment.builder()
                .id(id)
                .sprintId(sprintId)
                .title(title)
                .description(description)
                .impact(impact)
                .assigneeId(assigneeId)
                .status(status)
                .resolvedAt(resolvedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void resolve() {
        if (this.status != ImpedimentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Impediment can only be resolved from IN_PROGRESS status, current status: " + this.status);
        }
        this.status = ImpedimentStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void startWorkingOn() {
        if (this.status != ImpedimentStatus.NEW) {
            throw new IllegalStateException("Impediment can only start working on from NEW status, current status: " + this.status);
        }
        this.status = ImpedimentStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String title, @NonNull String description, @Nullable String impact, @Nullable UUID assigneeId) {
        checkNotNull(title, () -> new IllegalArgumentException("title must not be null"));
        checkNotNull(description, () -> new IllegalArgumentException("description must not be null"));
        this.title = title;
        this.description = description;
        this.impact = impact;
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }
}
