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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "impediments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Impediment {
    @Id
    private SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    private SnowflakeId sprintId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(length = 1000)
    private String impact;

    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ImpedimentStatus status = ImpedimentStatus.NEW;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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

    public void update(@NonNull String title, @NonNull String description, String impact, UUID assigneeId) {
        if (title == null) {
            throw new IllegalArgumentException("title must not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        this.title = title;
        this.description = description;
        this.impact = impact;
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }
}
