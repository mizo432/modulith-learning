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
 * <p>Sprint 内の障害事項を管理します。
 */
@Entity
@Table(name = "impediments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Impediment {
    @Id
    protected SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    protected SnowflakeId sprintId;

    @Column(nullable = false, length = 200)
    protected String title;

    @Column(nullable = false, length = 2000)
    protected String description;

    @Column(length = 1000)
    protected String impact;

    @Column(name = "assignee_id")
    protected UUID assigneeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    protected ImpedimentStatus status = ImpedimentStatus.NEW;

    @Column(name = "resolved_at")
    protected LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    protected LocalDateTime updatedAt = LocalDateTime.now();

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
