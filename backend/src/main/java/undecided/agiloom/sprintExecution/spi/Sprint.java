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
 * <p>アジャイル開発におけるSprintを管理します。
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
    @Id
    protected SnowflakeId id;

    @Column(name = "product_id", nullable = false)
    protected UUID productId;

    @Column(name = "sprint_number", nullable = false)
    protected Integer sprintNumber;

    @Column(nullable = false, length = 100)
    protected String title;

    @Column(name = "goal", length = 1000)
    protected String goal;

    @Column(name = "start_date", nullable = false)
    protected LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    protected LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    protected SprintStatus status = SprintStatus.PLANNED;

    @Column(name = "team_id", nullable = false)
    protected UUID teamId;

    @Column(name = "team_name", length = 100)
    protected String teamName;

    @Column(length = 10)
    protected Integer velocity;

    @Column(length = 2000)
    protected String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    protected LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    /**
     * Sprintを新規作成します。
     *
     * @param id ID
     * @param productId 製品ID
     * @param sprintNumber Sprint番号
     * @param title タイトル
     * @param goal 目標
     * @param startDate 開始日
     * @param endDate 終了日
     * @param status 状態
     * @param teamId チームID
     * @param teamName チーム名
     * @param velocity ベロシティ
     * @param notes メモ
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたSprintインスタンス
     */
    public static Sprint create(
            @NonNull SnowflakeId id,
            @NonNull UUID productId,
            @NonNull Integer sprintNumber,
            @NonNull String title,
            @Nullable String goal,
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate,
            @NonNull SprintStatus status,
            @NonNull UUID teamId,
            @Nullable String teamName,
            @Nullable Integer velocity,
            @Nullable String notes,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @Nullable LocalDateTime deletedAt) {
        return Sprint.builder()
                .id(id)
                .productId(productId)
                .sprintNumber(sprintNumber)
                .title(title)
                .goal(goal)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .teamId(teamId)
                .teamName(teamName)
                .velocity(velocity)
                .notes(notes)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

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
