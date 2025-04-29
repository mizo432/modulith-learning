package undecided.erp.scrum.domain.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * Roadmapクラスは、エンティティとしてスクラム開発におけるプロダクトロードマップを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`roadmapId`: ロードマップ固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: このロードマップが属するプロダクト。</li>
 *   <li>`name`: ロードマップの名前。</li>
 *   <li>`description`: ロードマップの詳細説明。</li>
 *   <li>`startDate`: ロードマップの開始日。</li>
 *   <li>`endDate`: ロードマップの終了日。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "roadmaps")
@NoArgsConstructor
public class Roadmap extends PptEntity<Roadmap> implements Serializable {

  /**
   * ユニークなロードマップ識別子を表す変数。
   * <p>
   * アプリケーションにおける各ロードマップを一意に識別するために使用されます。
   * データベース上の "roadmap_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "roadmap_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId roadmapId;

  /**
   * このロードマップが属するプロダクトを表す変数。
   * <p>
   * プロダクトとロードマップの関連付けを表します。
   * データベース上の "product_id" カラムに対応し、null 値は許可されていません。
   */
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @Getter
  private Product product;

  /**
   * ロードマップの名前を表す変数。
   * <p>
   * ロードマップの識別に使用される名前です。
   * データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * ロードマップの詳細説明を表す変数。
   * <p>
   * ロードマップの詳細な説明や背景情報を提供します。
   * データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * ロードマップの開始日を表す変数。
   * <p>
   * ロードマップの計画開始日を示します。
   * データベース上の "start_date" カラムに対応します。
   */
  @Getter
  @Column(name = "start_date")
  private LocalDate startDate;

  /**
   * ロードマップの終了日を表す変数。
   * <p>
   * ロードマップの計画終了日を示します。
   * データベース上の "end_date" カラムに対応します。
   */
  @Getter
  @Column(name = "end_date")
  private LocalDate endDate;

  /**
   * このメソッドはRoadmapクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Roadmapオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Roadmap{" +
        "roadmapId=" + roadmapId +
        ", product=" + (product != null ? product.toString() : "null") +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }
}
