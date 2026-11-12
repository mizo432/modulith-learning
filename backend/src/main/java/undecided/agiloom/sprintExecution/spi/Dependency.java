package undecided.agiloom.sprintExecution.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import com.github.mizo432.shared.common.dateProvider.DateProvider;
import com.github.mizo432.shared.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "sprint_dependencies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Dependency {
  @Id private SnowflakeId id;

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
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

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

  /**
   * 新しいDependencyを作成する静的ファクトリーメソッドです。
   *
   * @param id DependencyのID
   * @param sourceItemId 元アイテムID
   * @param targetItemId 対象アイテムID
   * @param dependencyType 依存タイプ
   * @param description 説明
   * @param createdAt 作成日時（nullの場合自動設定）
   * @param updatedAt 更新日時（nullの場合自動設定）
   * @return 新しく作成されたDependencyインスタンス
   */
  public static Dependency of(
      SnowflakeId id,
      SnowflakeId sourceItemId,
      SnowflakeId targetItemId,
      DependencyType dependencyType,
      String description,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    Dependency dependency = new Dependency();
    dependency.id = id;
    dependency.sourceItemId = sourceItemId;
    dependency.targetItemId = targetItemId;
    dependency.dependencyType = dependencyType;
    dependency.description = description;
    dependency.createdAt = createdAt;
    dependency.updatedAt = updatedAt;
    return dependency;
  }

  public void update(@NonNull DependencyType dependencyType, String description) {
    if (this.deletedAt != null) {
      throw new BusinessException("Dependency is already deleted");
    }
    checkNotNull(dependencyType, () -> new BusinessException("dependencyType must not be null"));
    if (description != null && description.trim().length() > 1000) {
      throw new BusinessException("description must not exceed 1000 characters");
    }
    this.dependencyType = dependencyType;
    this.description = description;
  }

  void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Dependency dependency = (Dependency) o;
    return Objects.equals(id, dependency.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
