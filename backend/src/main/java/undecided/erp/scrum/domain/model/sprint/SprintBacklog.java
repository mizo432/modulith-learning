package undecided.erp.scrum.domain.model.sprint;

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
import undecided.erp.scrum.domain.model.product.ProductBacklog;

/**
 * SprintBacklogクラスは、エンティティとしてスクラム開発におけるスプリントバックログを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`sprintBacklogId`: スプリントバックログ固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`sprint`: 関連するスプリント。</li>
 *   <li>`productBacklog`: 関連するプロダクトバックログ。</li>
 *   <li>`estimatedEffort`: 見積もり工数。</li>
 *   <li>`remainingEffort`: 残工数。</li>
 *   <li>`status`: ステータス（未着手、進行中、完了など）。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "sprint_backlogs")
@NoArgsConstructor
public class SprintBacklog extends PptEntity<SprintBacklog> implements Serializable {

  /**
   * ユニークなスプリントバックログ識別子を表す変数。
   * <p>
   * アプリケーションにおける各スプリントバックログを一意に識別するために使用されます。 データベース上の "sprint_backlog_id" カラムに対応し、null
   * 値は許可されていません。
   */
  @Id
  @Column(name = "sprint_backlog_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId sprintBacklogId;

  /**
   * このスプリントバックログが関連するスプリントを表す変数。
   * <p>
   * スプリントバックログが属するスプリントへの参照です。 データベース上の "sprint_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "sprint_id", nullable = false)
  private Sprint sprint;

  /**
   * このスプリントバックログが関連するプロダクトバックログを表す変数。
   * <p>
   * スプリントバックログが対応するプロダクトバックログへの参照です。 データベース上の "product_backlog_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "product_backlog_id", nullable = false)
  private ProductBacklog productBacklog;

  /**
   * 見積もり工数を表す変数。
   * <p>
   * スプリントバックログアイテムの完了に必要な見積もり工数です。 データベース上の "estimated_effort" カラムに対応します。
   */
  @Getter
  @Column(name = "estimated_effort")
  private Integer estimatedEffort;

  /**
   * 残工数を表す変数。
   * <p>
   * スプリントバックログアイテムの完了までに残っている工数です。 データベース上の "remaining_effort" カラムに対応します。
   */
  @Getter
  @Column(name = "remaining_effort")
  private Integer remainingEffort;

  /**
   * ステータスを表す変数。
   * <p>
   * スプリントバックログアイテムの現在の状態（未着手、進行中、完了など）を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /**
   * このメソッドはSprintBacklogクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return SprintBacklogオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "SprintBacklog{" +
        "sprintBacklogId=" + sprintBacklogId +
        ", sprint=" + sprint +
        ", productBacklog=" + productBacklog +
        ", estimatedEffort=" + estimatedEffort +
        ", remainingEffort=" + remainingEffort +
        ", status='" + status + '\'' +
        '}';
  }
}
