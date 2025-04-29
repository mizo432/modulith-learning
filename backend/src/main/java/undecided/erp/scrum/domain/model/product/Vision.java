package undecided.erp.scrum.domain.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * Visionクラスは、エンティティとしてスクラム開発におけるプロダクトビジョンを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`visionId`: ビジョン固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: このビジョンが属するプロダクト。</li>
 *   <li>`statement`: ビジョンステートメント。</li>
 *   <li>`targetCustomers`: ターゲットとなる顧客層。</li>
 *   <li>`customerNeeds`: 顧客のニーズ。</li>
 *   <li>`keyBenefits`: 主要なベネフィット。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "visions")
@NoArgsConstructor
public class Vision extends PptEntity<Vision> implements Serializable {

  /**
   * ユニークなビジョン識別子を表す変数。
   * <p>
   * アプリケーションにおける各ビジョンを一意に識別するために使用されます。 データベース上の "vision_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "vision_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId visionId;

  /**
   * このビジョンが属するプロダクトを表す変数。
   * <p>
   * プロダクトとビジョンの関連付けを表します。 データベース上の "product_id" カラムに対応し、null 値は許可されていません。
   */
  @OneToOne
  @JoinColumn(name = "product_id", nullable = false)
  @Getter
  private Product product;

  /**
   * ビジョンステートメントを表す変数。
   * <p>
   * プロダクトの目標や方向性を示すビジョンステートメントです。 データベース上の "statement" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "statement", nullable = false, length = 500)
  private String statement;

  /**
   * ターゲットとなる顧客層を表す変数。
   * <p>
   * プロダクトがターゲットとする顧客層の説明です。 データベース上の "target_customers" カラムに対応します。
   */
  @Getter
  @Column(name = "target_customers", length = 500)
  private String targetCustomers;

  /**
   * 顧客のニーズを表す変数。
   * <p>
   * プロダクトが解決する顧客のニーズや問題の説明です。 データベース上の "customer_needs" カラムに対応します。
   */
  @Getter
  @Column(name = "customer_needs", length = 500)
  private String customerNeeds;

  /**
   * 主要なベネフィットを表す変数。
   * <p>
   * プロダクトが提供する主要なベネフィットの説明です。 データベース上の "key_benefits" カラムに対応します。
   */
  @Getter
  @Column(name = "key_benefits", length = 500)
  private String keyBenefits;

  /**
   * このメソッドはVisionクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Visionオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Vision{" +
        "visionId=" + visionId +
        ", product=" + (product != null ? product.toString() : "null") +
        ", statement='" + statement + '\'' +
        ", targetCustomers='" + targetCustomers + '\'' +
        ", customerNeeds='" + customerNeeds + '\'' +
        ", keyBenefits='" + keyBenefits + '\'' +
        '}';
  }
}
