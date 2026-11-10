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
import jakarta.persistence.UniqueConstraint;
import com.github.mizo432.shared.common.dateProvider.DateProvider;
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
@Table(name = "sprint_goals", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sprint_goal_sprint", columnNames = {"sprintId"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintGoal {
    @Id
    private SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    private SnowflakeId sprintId;

    @Column(name = "goal_description", nullable = false, length = 1000)
    private String goalDescription;

    @Column(name = "related_items", length = 2000)
    private String relatedItems;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = DateProvider.currentLocalDateTime();
        }
        if (this.updatedAt == null) {
            this.updatedAt = DateProvider.currentLocalDateTime();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = DateProvider.currentLocalDateTime();
    }

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void update(@NonNull String goalDescription, String relatedItems) {
        checkNotNull(goalDescription, () -> new BusinessException("goalDescription must not be null"));
        if (goalDescription.trim().isEmpty()) {
            throw new BusinessException("goalDescription must not be empty");
        }
        if (goalDescription.trim().length() > 1000) {
            throw new BusinessException("goalDescription must not exceed 1000 characters");
        }
        this.goalDescription = goalDescription;
        this.relatedItems = relatedItems;
        this.updatedAt = DateProvider.currentLocalDateTime();
    }

}
