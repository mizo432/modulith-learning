package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
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
 * Sprintエンティティ。
 *
 * <p>アジャイル開発におけるSprintを管理します。Sprintは固定期間の開発サイクルであり、
 * チームが計画・実行・レビュー・振り返りを行う単位として機能します。
 * Sprintは状態遷移（PLANNED → ACTIVE → COMPLETED/CANCELLED）を持ち、適切なフローで管理されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられました。
 * インスタンスは{@code @Builder}を使用して作成します。
 * {@code @AllArgsConstructor}は{@code @Builder}の動作に必要な内部用コンストラクタです。
 */
@Entity
@Table(name = "sprints", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sprint_product_number", columnNames = {"productId", "sprintNumber"})
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sprint {

  /** Sprintの一意な識別子。 */
  @Id
  protected SnowflakeId id;

  /** 関連する製品のID。 */
  @Column(name = "product_id", nullable = false)
  protected UUID productId;

  /** Sprintの番号。製品ごとに連番で管理されます。 */
  @Column(name = "sprint_number", nullable = false)
  protected Integer sprintNumber;

  /** Sprintのタイトル。Sprintのテーマや目的を簡潔に表現します。 */
  @Column(nullable = false, length = 100)
  protected String title;

  /** Sprintの目標。チームがSprintの期間中に達成すべき目標を記述します。 */
  @Column(name = "goal", length = 1000)
  protected String goal;

  /** Sprintの開始日。 */
  @Column(name = "start_date", nullable = false)
  protected LocalDate startDate;

  /** Sprintの終了日。 */
  @Column(name = "end_date", nullable = false)
  protected LocalDate endDate;

  /** Sprintの状態（PLANNED, ACTIVE, COMPLETED, CANCELLEDなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected SprintStatus status = SprintStatus.PLANNED;

  /** Sprintを担当するチームのID。 */
  @Column(name = "team_id", nullable = false)
  protected UUID teamId;

  /** Sprintを担当するチームの名前。 */
  @Column(name = "team_name", length = 100)
  protected String teamName;

  /** チームのベロシティ。Sprintで処理できる作業量の目安です。 */
  @Column(length = 10)
  protected Integer velocity;

  /** Sprintに関する補足メモ。 */
  @Column(length = 2000)
  protected String notes;

  /** Sprintが作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** Sprintが最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** Sprintが論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    public void start() {
        if (this.status != SprintStatus.PLANNED) {
            throw new IllegalStateException("Sprint can only be started from PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != SprintStatus.ACTIVE) {
            throw new IllegalStateException("Sprint can only be completed from ACTIVE status, current status: " + this.status);
        }
        this.status = SprintStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != SprintStatus.ACTIVE && this.status != SprintStatus.PLANNED) {
            throw new IllegalStateException("Sprint can only be cancelled from ACTIVE or PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String title, @NonNull String goal, @Nullable LocalDate endDate, @Nullable String notes) {
        checkNotNull(title, () -> new IllegalArgumentException("title must not be null"));
        checkNotNull(goal, () -> new IllegalArgumentException("goal must not be null"));
        this.title = title;
        this.goal = goal;
        if (endDate != null) {
            this.endDate = endDate;
        }
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
}
