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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprint_backlogs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
