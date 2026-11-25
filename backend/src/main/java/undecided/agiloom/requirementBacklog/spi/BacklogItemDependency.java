package undecided.agiloom.requirementBacklog.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテム間の依存関係（RDRA INF-BL-07 項目関連付け）。
 *
 * <p>依存は直接の関係のみとし、依存先が DONE になるまで依存元は着手できません（Issue #665／#666／#668）。
 */
@Entity
@Table(schema = "undecided_ag", name = "backlog_item_dependencies")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BacklogItemDependency {

  /** 依存関係の一意な識別子。 */
  @Id protected SnowflakeId id;

  /** 依存元のバックログアイテムID。 */
  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  /** 依存先のバックログアイテムID。 */
  @Column(name = "dependency_item_id", nullable = false)
  protected SnowflakeId dependencyItemId;

  /** 依存関係を定義した従業員ID（POまたはSM）。 */
  @Column(name = "created_by", nullable = false)
  protected SnowflakeId createdBy;

  /** 定義日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** 楽観ロック用バージョン列。 */
  @Version
  @Column(name = "version_col")
  protected Long versionCol;
}
