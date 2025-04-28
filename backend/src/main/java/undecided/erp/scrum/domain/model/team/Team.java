package undecided.erp.scrum.domain.model.team;

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
import undecided.erp.scrum.domain.model.product.Product;

/**
 * Teamクラスは、エンティティとしてスクラム開発におけるチームを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`teamId`: チーム固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: 関連するプロダクト。</li>
 *   <li>`name`: チームの名前。</li>
 *   <li>`description`: チームの詳細説明。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "teams")
@NoArgsConstructor
public class Team extends PptEntity<Team> implements Serializable {

  /**
   * ユニークなチーム識別子を表す変数。
   * <p>
   * アプリケーションにおける各チームを一意に識別するために使用されます。 データベース上の "team_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "team_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId teamId;

  /**
   * このチームが関連するプロダクトを表す変数。
   * <p>
   * チームが担当するプロダクトへの参照です。 データベース上の "product_id" カラムに対応します。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  /**
   * チームの名前を表す変数。
   * <p>
   * チームの識別に使用される名前です。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * チームの詳細説明を表す変数。
   * <p>
   * チームの詳細な説明や目的を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * このメソッドはTeamクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Teamオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Team{" +
        "teamId=" + teamId +
        ", product=" + product +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
