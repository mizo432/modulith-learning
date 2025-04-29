package undecided.erp.scrum.domain.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * ProductBacklogクラスは、エンティティとしてスクラム開発におけるプロダクトバックログを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`backlogId`: プロダクトバックログ固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: このバックログが属するプロダクト。</li>
 *   <li>`name`: プロダクトバックログの名前。</li>
 *   <li>`description`: プロダクトバックログの詳細説明。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "product_backlogs")
@NoArgsConstructor
public class ProductBacklog extends PptEntity<ProductBacklog> implements Serializable {

  /**
   * ユニークなプロダクトバックログ識別子を表す変数。
   * <p>
   * アプリケーションにおける各プロダクトバックログを一意に識別するために使用されます。
   * データベース上の "backlog_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "backlog_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId backlogId;

  /**
   * このバックログが属するプロダクトを表す変数。
   * <p>
   * プロダクトとプロダクトバックログの関連付けを表します。
   * データベース上の "product_id" カラムに対応し、null 値は許可されていません。
   */
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @Getter
  private Product product;

  /**
   * プロダクトバックログの名前を表す変数。
   * <p>
   * プロダクトバックログの識別に使用される名前です。
   * データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * プロダクトバックログの詳細説明を表す変数。
   * <p>
   * プロダクトバックログの詳細な説明や背景情報を提供します。
   * データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * このメソッドはProductBacklogクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return ProductBacklogオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "ProductBacklog{" +
        "backlogId=" + backlogId +
        ", product=" + (product != null ? product.toString() : "null") +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
