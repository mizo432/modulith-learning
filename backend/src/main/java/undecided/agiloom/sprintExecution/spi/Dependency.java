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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 依存関係エンティティ。
 *
 * <p>Sprint 内のバックログ項目間の依存関係を管理します。タスクやストーリー間の依存関係を明確にし、
 * 開発の順序やブロック要因を把握するために使用されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "sprint_dependencies")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dependency {

  /** 依存関係の一意な識別子。 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected SnowflakeId id;

  /** 依存元のバックログ項目のID。 */
  @Column(name = "source_item_id", nullable = false)
  protected SnowflakeId sourceItemId;

  /** 依存先のバックログ項目のID。 */
  @Column(name = "target_item_id", nullable = false)
  protected SnowflakeId targetItemId;

  /** 依存関係の種類（BLOCKS, REQUIRES, AFFECTSなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(name = "dependency_type", nullable = false, length = 20)
  protected DependencyType dependencyType;

  /** 依存関係の詳細な説明。 */
  @Column(length = 1000)
  protected String description;

  /** 依存関係が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** 依存関係が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** 依存関係が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    /**
     * 依存関係を新規作成します。
     *
     * @param id ID
     * @param sourceItemId ソース項目ID
     * @param targetItemId ターゲット項目ID
     * @param dependencyType 依存種別
     * @param description 説明
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたDependencyインスタンス
     */
    public static Dependency create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sourceItemId,
            @NonNull SnowflakeId targetItemId,
            @NonNull DependencyType dependencyType,
            @Nullable String description,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @Nullable LocalDateTime deletedAt) {
        return Dependency.builder()
                .id(id)
                .sourceItemId(sourceItemId)
                .targetItemId(targetItemId)
                .dependencyType(dependencyType)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void update(@NonNull DependencyType dependencyType, String description) {
        this.dependencyType = dependencyType;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
}
