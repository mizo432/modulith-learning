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
  protected SnowflakeId productId;

  /** Sprintの番号。製品ごとに連番で管理されます。 */
  @Column(name = "sprint_number", nullable = false)
  protected Integer sprintNumber;

  /** Sprintのタイトル。Sprintのテーマや目的を簡潔に表現します。 */
  @Column(nullable = false, length = 100)
  protected String title;

  /** Sprintの目標。チームがSprintの期間中に達成すべき目標を記述します。 */
  @Column(name = "goal", length = 1000)
  protected String goal;

  /** Sprintの開始日。スプリント0の場合はnullです。 */
  @Column(name = "start_date")
  protected LocalDate startDate;

  /** Sprintの終了日。スプリント0の場合はnullです。 */
  @Column(name = "end_date")
  protected LocalDate endDate;

  /** Sprintの状態（PLANNED, ACTIVE, COMPLETED, CANCELLEDなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected SprintStatus status = SprintStatus.PLANNED;

  /** Sprintを担当するチームのID。 */
  @Column(name = "team_id", nullable = false)
  protected SnowflakeId teamId;

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
  protected LocalDateTime createdAt;

  /** Sprintが最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  protected LocalDateTime updatedAt;

  /** Sprintが論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  /** スプリント0かどうかを示すフラグ。スプリント0は特別な初期スプリントです。 */
  @Column(name = "is_sprint_zero", nullable = false)
  @Builder.Default
  protected Boolean isSprintZero = false;

  /**
   * スプリント0かどうかを返します。
   * スプリント0は削除・名前変更が制限される特別なスプリントです。
   */
  public boolean isSprintZero() {
    return this.isSprintZero != null && this.isSprintZero;
  }

  /**
   * スプリント0としてマークします。
   */
  public void markAsSprintZero(@NonNull LocalDateTime now) {
    this.isSprintZero = true;
    this.sprintNumber = 0;
    this.updatedAt = now;
  }

    public void start(@NonNull LocalDateTime now) {
        if (this.status != SprintStatus.PLANNED) {
            throw new IllegalStateException("Sprint can only be started from PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.ACTIVE;
        this.updatedAt = now;
    }

    public void complete(@NonNull LocalDateTime now) {
        if (this.status != SprintStatus.ACTIVE) {
            throw new IllegalStateException("Sprint can only be completed from ACTIVE status, current status: " + this.status);
        }
        this.status = SprintStatus.COMPLETED;
        this.updatedAt = now;
    }

    public void cancel(@NonNull LocalDateTime now) {
        if (this.status != SprintStatus.ACTIVE && this.status != SprintStatus.PLANNED) {
            throw new IllegalStateException("Sprint can only be cancelled from ACTIVE or PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.CANCELLED;
        this.updatedAt = now;
    }

    public void update(@NonNull String title, @NonNull String goal, @Nullable LocalDate endDate, @Nullable String notes, @NonNull LocalDateTime now) {
        checkNotNull(title, () -> new IllegalArgumentException("title must not be null"));
        checkNotNull(goal, () -> new IllegalArgumentException("goal must not be null"));
        // スプリント0の名前変更は禁止
        if (!this.isSprintZero()) {
            this.title = title;
        }
        this.goal = goal;
        if (endDate != null) {
            this.endDate = endDate;
        }
        this.notes = notes;
        this.updatedAt = now;
    }

    /**
     * スプリントの開始日と終了日を更新します。
     * スプリント0でも日付の更新は許可されます。
     *
     * @param startDate 開始日（nullの場合は変更なし）
     * @param endDate 終了日（nullの場合は変更なし）
     */
    public void updateDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, @NonNull LocalDateTime now) {
        if (startDate != null) {
            this.startDate = startDate;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }
        this.updatedAt = now;
    }

    /**
     * スプリントを論理削除します。
     * スプリント0は削除できないため、例外をスローします。
     */
    public void delete(@NonNull LocalDateTime now) {
        if (this.isSprintZero()) {
            throw new IllegalStateException("Sprint 0 cannot be deleted");
        }
        this.deletedAt = now;
        this.updatedAt = now;
    }

    /**
     * このスプリントが削除可能かどうかを返します。
     * スプリント0は削除できません。
     */
    public boolean isDeletable() {
        return !this.isSprintZero();
    }

    /**
     * スプリント0を生成するファクトリメソッドです。
     * スプリント0はチームの初期準備期間を表す特別なスプリントであり、
     * 削除・名前変更が制限されます。
     *
     * @param productId 関連する製品のID
     * @param teamId スクラムチームのID
     * @param teamName スクラムチームの名前
     * @return スプリント0のインスタンス
     */
    public static Sprint createSprintZero(@NonNull SnowflakeId productId, @NonNull SnowflakeId teamId, @NonNull String teamName, @NonNull LocalDateTime now) {
        checkNotNull(productId, () -> new IllegalArgumentException("productId must not be null"));
        checkNotNull(teamId, () -> new IllegalArgumentException("teamId must not be null"));
        checkNotNull(teamName, () -> new IllegalArgumentException("teamName must not be null"));

        Sprint sprint = Sprint.builder()
                .productId(productId)
                .sprintNumber(0)
                .title("Sprint 0")
                .goal("チームの初期準備期間")
                .startDate(null)
                .endDate(null)
                .status(SprintStatus.PLANNED)
                .teamId(teamId)
                .teamName(teamName)
                .isSprintZero(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return sprint;
    }
}
