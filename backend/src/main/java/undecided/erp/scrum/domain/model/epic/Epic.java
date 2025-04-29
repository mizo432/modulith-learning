package undecided.erp.scrum.domain.model.epic;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.task.UserStory;

/**
 * Epicクラスは、エンティティとしてスクラム開発における複数のスプリントをまたがるエピックを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`epicId`: エピック固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`product`: このエピックが属するプロダクト。</li>
 *   <li>`title`: エピックのタイトル。</li>
 *   <li>`description`: エピックの詳細説明。</li>
 *   <li>`status`: エピックの状態。</li>
 *   <li>`priority`: エピックの優先度。</li>
 *   <li>`startDate`: 開始予定日。</li>
 *   <li>`endDate`: 終了予定日。</li>
 *   <li>`userStories`: このエピックに関連するユーザーストーリーのセット。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "epics")
@NoArgsConstructor
public class Epic extends PptEntity<Epic> implements Serializable {

  /**
   * ユニークなエピック識別子を表す変数。
   * <p>
   * アプリケーションにおける各エピックを一意に識別するために使用されます。 データベース上の "epic_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "epic_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId epicId;
  /**
   * このエピックが属するプロダクトを表す変数。
   * <p>
   * プロダクトとエピックの関連付けを表します。 データベース上の "product_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @Getter
  private Product product;
  /**
   * エピックのタイトルを表す変数。
   * <p>
   * エピックの識別に使用されるタイトルです。 データベース上の "title" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "title", nullable = false, length = 200)
  private String title;
  /**
   * エピックの詳細説明を表す変数。
   * <p>
   * エピックの詳細な説明を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;
  /**
   * エピックの状態を表す変数。
   * <p>
   * エピックの現在の状態を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;
  /**
   * エピックの優先度を表す変数。
   * <p>
   * エピックの優先度を示します。 データベース上の "priority" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false)
  private Priority priority;
  /**
   * 開始予定日を表す変数。
   * <p>
   * エピックの開始予定日を示します。 データベース上の "start_date" カラムに対応します。
   */
  @Getter
  @Column(name = "start_date")
  private LocalDate startDate;
  /**
   * 終了予定日を表す変数。
   * <p>
   * エピックの終了予定日を示します。 データベース上の "end_date" カラムに対応します。
   */
  @Getter
  @Column(name = "end_date")
  private LocalDate endDate;
  /**
   * このエピックに関連するユーザーストーリーのセットを表す変数。
   * <p>
   * エピックとユーザーストーリーの関連付けを表します。
   */
  @OneToMany(mappedBy = "epic")
  @Getter
  private Set<UserStory> userStories = new HashSet<>();

  /**
   * エピックIDを取得します。
   *
   * @return エピックID
   */
  public SnowflakeId getEpicId() {
    return epicId;
  }

  /**
   * エピックIDを設定します。
   *
   * @param epicId 設定するエピックID
   */
  public void setEpicId(SnowflakeId epicId) {
    this.epicId = epicId;
  }

  /**
   * エピックが属するプロダクトを設定します。
   *
   * @param product 設定するプロダクト
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * エピックのタイトルを設定します。
   *
   * @param title 設定するタイトル
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * エピックの詳細説明を設定します。
   *
   * @param description 設定する詳細説明
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * エピックの状態を設定します。
   *
   * @param status 設定する状態
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * エピックの優先度を設定します。
   *
   * @param priority 設定する優先度
   */
  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  /**
   * エピックの開始予定日を設定します。
   *
   * @param startDate 設定する開始予定日
   */
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * エピックの終了予定日を設定します。
   *
   * @param endDate 設定する終了予定日
   */
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   * このメソッドはEpicクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Epicオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Epic{" +
        "epicId=" + epicId +
        ", product=" + (product != null ? product.toString() : "null") +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", status=" + status +
        ", priority=" + priority +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }

  /**
   * エピックの状態を表す列挙型。
   */
  public enum Status {
    /**
     * 未着手の状態。
     */
    TODO,

    /**
     * 進行中の状態。
     */
    IN_PROGRESS,

    /**
     * 完了した状態。
     */
    DONE
  }

  /**
   * エピックの優先度を表す列挙型。
   */
  public enum Priority {
    /**
     * 最高優先度。
     */
    HIGHEST,

    /**
     * 高優先度。
     */
    HIGH,

    /**
     * 中優先度。
     */
    MEDIUM,

    /**
     * 低優先度。
     */
    LOW,

    /**
     * 最低優先度。
     */
    LOWEST
  }
}
