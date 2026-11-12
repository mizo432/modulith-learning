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
@Table(name = "impediments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Impediment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.status = ImpedimentStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void startWorkingOn() {
        this.status = ImpedimentStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String title, @NonNull String description, String impact, UUID assigneeId) {
        this.title = title;
        this.description = description;
        this.impact = impact;
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
    }
}
