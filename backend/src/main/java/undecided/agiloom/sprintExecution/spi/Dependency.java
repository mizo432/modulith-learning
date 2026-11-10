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
import com.github.mizo432.shared.common.dateProvider.DateProvider;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprint_dependencies")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {
    @Id
    private SnowflakeId id;

    @Column(name = "source_item_id", nullable = false)
    private SnowflakeId sourceItemId;

    @Column(name = "target_item_id", nullable = false)
    private SnowflakeId targetItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type", nullable = false, length = 20)
    private DependencyType dependencyType;

    @Column(length = 1000)
    private String description;

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

    public void update(@NonNull DependencyType dependencyType, String description) {
        checkNotNull(dependencyType, () -> new BusinessException("dependencyType must not be null"));
        if (description != null && description.trim().length() > 1000) {
            throw new BusinessException("description must not exceed 1000 characters");
        }
        this.dependencyType = dependencyType;
        this.description = description;
        this.updatedAt = DateProvider.currentLocalDateTime();
    }

    void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setSourceItemId(SnowflakeId sourceItemId) {
        this.sourceItemId = sourceItemId;
    }

    void setTargetItemId(SnowflakeId targetItemId) {
        this.targetItemId = targetItemId;
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
