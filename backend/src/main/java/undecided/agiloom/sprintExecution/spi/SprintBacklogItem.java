package undecided.agiloom.sprintExecution.spi;

import com.github.mizo432.shared.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprint_backlogs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklogItem {
    @Id
    private SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    private SnowflakeId sprintId;

    @Column(name = "backlog_item_id", nullable = false)
    private SnowflakeId backlogItemId;

    @Column(name = "work_description", length = 2000)
    private String workDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;

    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updateStatus(@NonNull TaskStatus status) {
        checkNotNull(status, () -> new BusinessException("status must not be null"));
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(@NonNull UUID assigneeId) {
        checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String workDescription, @NonNull TaskStatus status, @NonNull UUID assigneeId) {
        checkNotNull(workDescription, () -> new BusinessException("workDescription must not be null"));
        checkNotNull(status, () -> new BusinessException("status must not be null"));
        checkNotNull(assigneeId, () -> new BusinessException("assigneeId must not be null"));
        if (workDescription.trim().length() > 2000) {
            throw new BusinessException("workDescription must not exceed 2000 characters");
        }
        this.workDescription = workDescription;
        this.status = status;
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }

    void setId(SnowflakeId id) {
        this.id = id;
    }

    void setSprintId(SnowflakeId sprintId) {
        this.sprintId = sprintId;
    }

    void setBacklogItemId(SnowflakeId backlogItemId) {
        this.backlogItemId = backlogItemId;
    }

    void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    void setStatus(TaskStatus status) {
        this.status = status;
    }

    void setAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
    }

    void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
