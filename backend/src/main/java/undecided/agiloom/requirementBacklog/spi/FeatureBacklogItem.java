package undecided.agiloom.requirementBacklog.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャーとバックログアイテムの関連エンティティ。
 *
 * <p>複合主キー（featureId, backlogItemId）で一意性を保証します。
 * 1つのバックログアイテムは最大1つのフィーチャーにのみ割り当てられる（決定3）。
 */
@Entity
@Table(schema = "undecided_ag", name = "feature_backlog_items")
@IdClass(FeatureBacklogItem.FeatureBacklogItemId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureBacklogItem {

  /** フィーチャーID（複合主キー）。 */
  @Id
  @Column(name = "feature_id")
  @Convert(disableConversion = true)
  protected SnowflakeId featureId;

  /** バックログアイテムID（複合主キー）。 */
  @Id
  @Column(name = "backlog_item_id")
  @Convert(disableConversion = true)
  protected SnowflakeId backlogItemId;

  /** 紐付け日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  /**
   * 複合主キークラス。
   */
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FeatureBacklogItemId implements Serializable {
    private static final long serialVersionUID = 1L;
    private SnowflakeId featureId;
    private SnowflakeId backlogItemId;
  }

  /**
   * フィーチャーとバックログアイテムの関連をビルダーパターンで構築します。
   */
  @Builder(builderMethodName = "builder")
  private static FeatureBacklogItem build(
      SnowflakeId featureId,
      SnowflakeId backlogItemId,
      LocalDateTime createdAt) {
    FeatureBacklogItem item = new FeatureBacklogItem();
    item.featureId = featureId;
    item.backlogItemId = backlogItemId;
    if (createdAt == null) {
      throw new IllegalArgumentException("createdAt must not be null");
    }
    item.createdAt = createdAt;
    return item;
  }
}
