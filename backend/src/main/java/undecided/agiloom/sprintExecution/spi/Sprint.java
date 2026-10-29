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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprints", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sprint_product_number", columnNames = {"productId", "sprintNumber"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private SnowflakeId id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "sprint_number", nullable = false)
    private Integer sprintNumber;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "goal", length = 1000)
    private String goal;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SprintStatus status = SprintStatus.PLANNED;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "team_name", length = 100)
    private String teamName;

    @Column(length = 10)
    private Integer velocity;

    @Column(length = 2000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void start() {
        this.status = SprintStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = SprintStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = SprintStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(@NonNull String title, @NonNull String goal, LocalDate endDate, String notes) {
        this.title = title;
        this.goal = goal;
        if (endDate != null) {
            this.endDate = endDate;
        }
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
}
