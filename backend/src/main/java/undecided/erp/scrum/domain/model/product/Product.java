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
import undecided.erp.scrum.domain.model.team.Member;

/**
 * Productクラスは、エンティティとしてスクラム開発におけるプロダクトを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`productId`: プロダクト固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`name`: プロダクトの名前。</li>
 *   <li>`vision`: プロダクトのビジョン。</li>
 *   <li>`description`: プロダクトの詳細説明。</li>
 *   <li>`productOwner`: プロダクトオーナー。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product extends PptEntity<Product> implements Serializable {

  /**
   * ユニークなプロダクト識別子を表す変数。
   * <p>
   * アプリケーションにおける各プロダクトを一意に識別するために使用されます。 データベース上の "product_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "product_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId productId;

  /**
   * プロダクトの名前を表す変数。
   * <p>
   * プロダクトの識別に使用される名前です。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * プロダクトのビジョンを表す変数。
   * <p>
   * プロダクトの目標や方向性を示すビジョンステートメントです。 データベース上の "vision" カラムに対応します。
   */
  @Getter
  @Column(name = "vision", length = 500)
  private String vision;

  /**
   * プロダクトの詳細説明を表す変数。
   * <p>
   * プロダクトの詳細な説明や背景情報を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * プロダクトオーナーを表す変数。
   * <p>
   * このプロダクトのオーナーとなるチームメンバーです。 データベース上の "product_owner_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "product_owner_id", nullable = false)
  private Member productOwner;

  /**
   * このメソッドはProductクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Productオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Product{" +
        "productId=" + productId +
        ", name='" + name + '\'' +
        ", vision='" + vision + '\'' +
        ", description='" + description + '\'' +
        ", productOwner=" + productOwner +
        '}';
  }
}
