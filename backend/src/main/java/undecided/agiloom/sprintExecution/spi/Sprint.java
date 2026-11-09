package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import com.github.mizo432.shared.exception.BusinessException;

import com.github.mizo432.shared.common.dateProvider.DateProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprints", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sprint_product_number", columnNames = {"productId", "sprintNumber"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    @Id
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
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = DateProvider.currentLocalDateTime();
        this.updatedAt = DateProvider.currentLocalDateTime();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = DateProvider.currentLocalDateTime();
    }

    @PostLoad
    protected void onLoad() {
        // @Builder.DefaultはJPAロード時に機能しないため、nullの場合はデフォルト値を設定する
        if (this.status == null) {
            this.status = SprintStatus.PLANNED;
        }
    }

    public void start() {
        if (this.status != SprintStatus.PLANNED) {
            throw new BusinessException("Sprint can only be started from PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.ACTIVE;
    }

    public void complete() {
        if (this.status != SprintStatus.ACTIVE) {
            throw new BusinessException("Sprint can only be completed from ACTIVE status, current status: " + this.status);
        }
        this.status = SprintStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status != SprintStatus.ACTIVE && this.status != SprintStatus.PLANNED) {
            throw new BusinessException("Sprint can only be cancelled from ACTIVE or PLANNED status, current status: " + this.status);
        }
        this.status = SprintStatus.CANCELLED;
    }

    public void update(@NonNull String title, @NonNull String goal, @Nullable LocalDate endDate, @Nullable String notes) {
        checkNotNull(title, () -> new BusinessException("title must not be null"));
        checkNotNull(goal, () -> new BusinessException("goal must not be null"));
        if (title.trim().isEmpty()) {
            throw new BusinessException("title must not be empty");
        }
        if (title.trim().length() > 100) {
            throw new BusinessException("title must not exceed 100 characters");
        }
        if (goal.trim().isEmpty()) {
            throw new BusinessException("goal must not be empty");
        }
        if (goal.trim().length() > 1000) {
            throw new BusinessException("goal must not exceed 1000 characters");
        }
        this.title = title;
        this.goal = goal;
        if (endDate != null) {
            if (!endDate.isAfter(this.startDate)) {
                throw new BusinessException("endDate must be after startDate");
            }
            this.endDate = endDate;
        }
        this.notes = notes;
    }
}
