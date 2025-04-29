package undecided.erp.scrum.domain.model.sprint;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import undecided.erp.scrum.domain.model.product.Product;

/**
 * Sprintクラスは、エンティティとしてスクラム開発におけるスプリントを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`sprintId`: スプリント固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: このスプリントが属するプロダクト。</li>
 *   <li>`name`: スプリントの名前。</li>
 *   <li>`goal`: スプリントの目標。</li>
 *   <li>`startDate`: スプリントの開始日。</li>
 *   <li>`endDate`: スプリントの終了日。</li>
 *   <li>`status`: スプリントの状態。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "sprints")
@NoArgsConstructor
public class Sprint extends PptEntity<Sprint> implements Serializable {

  /**
   * ユニークなスプリント識別子を表す変数。
   * <p>
   * アプリケーションにおける各スプリントを一意に識別するために使用されます。
   * データベース上の "sprint_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "sprint_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId sprintId;

  /**
   * このスプリントが属するプロダクトを表す変数。
   * <p>
   * プロダクトとスプリントの関連付けを表します。
   * データベース上の "product_id" カラムに対応し、null 値は許可されていません。
   */
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @Getter
  private Product product;

  /**
   * スプリントの名前を表す変数。
   * <p>
   * スプリントの識別に使用される名前です。
   * データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * スプリントの目標を表す変数。
   * <p>
   * スプリントの目標や達成したい成果を示します。
   * データベース上の "goal" カラムに対応します。
   */
  @Getter
  @Column(name = "goal", length = 500)
  private String goal;

  /**
   * スプリントの開始日を表す変数。
   * <p>
   * スプリントの開始日を示します。
   * データベース上の "start_date" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /**
   * スプリントの終了日を表す変数。
   * <p>
   * スプリントの終了日を示します。
   * データベース上の "end_date" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  /**
   * スプリントの状態を表す変数。
   * <p>
   * スプリントの現在の状態を示します。
   * データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SprintStatus status;

  /**
   * このメソッドはSprintクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Sprintオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Sprint{" +
        "sprintId=" + sprintId +
        ", product=" + (product != null ? product.toString() : "null") +
        ", name='" + name + '\'' +
        ", goal='" + goal + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", status=" + status +
        '}';
  }

  /**
   * スプリントの状態を表す列挙型。
   */
  public enum SprintStatus {
    /**
     * 計画中のスプリント。
     */
    PLANNED,

    /**
     * 進行中のスプリント。
     */
    IN_PROGRESS,

    /**
     * 完了したスプリント。
     */
    COMPLETED,

    /**
     * キャンセルされたスプリント。
     */
    CANCELLED
  }
}
