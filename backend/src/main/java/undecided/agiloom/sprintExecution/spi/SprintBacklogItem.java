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
 * <p>Sprint に含まれるバックログ項目を管理します。
 */
@Entity
@Table(name = "sprint_backlogs")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprintBacklogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    protected SnowflakeId sprintId;

    @Column(name = "backlog_item_id", nullable = false)
    protected SnowflakeId backlogItemId;

    @Column(name = "work_description", length = 2000)
    protected String workDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    protected TaskStatus status = TaskStatus.TODO;

    @Column(name = "assignee_id")
    protected UUID assigneeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    protected LocalDateTime updatedAt = LocalDateTime.now();

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
