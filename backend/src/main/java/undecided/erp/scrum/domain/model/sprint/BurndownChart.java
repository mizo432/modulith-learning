package undecided.erp.scrum.domain.model.sprint;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * BurndownChartクラスは、エンティティとしてスクラム開発におけるバーンダウンチャートを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`chartId`: バーンダウンチャート固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`sprint`: このチャートが属するスプリント。</li>
 *   <li>`date`: 記録日。</li>
 *   <li>`totalPoints`: 合計ポイント。</li>
 *   <li>`remainingPoints`: 残りポイント。</li>
 *   <li>`idealBurndown`: 理想的なバーンダウン。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "burndown_charts")
@NoArgsConstructor
public class BurndownChart extends PptEntity<BurndownChart> implements Serializable {

  /**
   * ユニークなバーンダウンチャート識別子を表す変数。
   * <p>
   * アプリケーションにおける各バーンダウンチャートを一意に識別するために使用されます。 データベース上の "chart_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "chart_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId chartId;

  /**
   * このチャートが属するスプリントを表す変数。
   * <p>
   * スプリントとバーンダウンチャートの関連付けを表します。 データベース上の "sprint_id" カラムに対応し、null 値は許可されていません。
   */
  @OneToOne
  @JoinColumn(name = "sprint_id", nullable = false)
  @Getter
  private Sprint sprint;

  /**
   * 記録日を表す変数。
   * <p>
   * バーンダウンチャートの記録日を示します。 データベース上の "date" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "date", nullable = false)
  private LocalDate date;

  /**
   * 合計ポイントを表す変数。
   * <p>
   * スプリントの合計ストーリーポイントを示します。 データベース上の "total_points" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "total_points", nullable = false)
  private Integer totalPoints;

  /**
   * 残りポイントを表す変数。
   * <p>
   * スプリントの残りストーリーポイントを示します。 データベース上の "remaining_points" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "remaining_points", nullable = false)
  private Integer remainingPoints;

  /**
   * 理想的なバーンダウンを表す変数。
   * <p>
   * 理想的なバーンダウンの値を示します。 データベース上の "ideal_burndown" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "ideal_burndown", nullable = false)
  private Integer idealBurndown;

  /**
   * このメソッドはBurndownChartクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return BurndownChartオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "BurndownChart{" +
        "chartId=" + chartId +
        ", sprint=" + (sprint != null ? sprint.toString() : "null") +
        ", date=" + date +
        ", totalPoints=" + totalPoints +
        ", remainingPoints=" + remainingPoints +
        ", idealBurndown=" + idealBurndown +
        '}';
  }
}
