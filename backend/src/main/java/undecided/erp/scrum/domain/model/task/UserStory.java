package undecided.erp.scrum.domain.model.task;

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
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.product.ProductBacklog;

/**
 * UserStoryクラスは、エンティティとしてスクラム開発におけるユーザーストーリーを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`storyId`: ユーザーストーリー固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`productBacklog`: このユーザーストーリーが属するプロダクトバックログ。
 *   <li>`epic`: このユーザーストーリーが属するエピック。
 *   <li>`title`: ユーザーストーリーのタイトル。
 *   <li>`description`: ユーザーストーリーの詳細説明。
 *   <li>`acceptanceCriteria`: 受け入れ基準。
 *   <li>`priority`: 優先度。
 *   <li>`storyPoints`: ストーリーポイント。
 *   <li>`status`: 状態。
 *   <li>`tasks`: このユーザーストーリーに関連するタスクのセット。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "user_stories")
@NoArgsConstructor
@Setter
public class UserStory extends PptEntity<UserStory> implements Serializable {

  /**
   * ユニークなユーザーストーリー識別子を表す変数。
   *
   * <p>アプリケーションにおける各ユーザーストーリーを一意に識別するために使用されます。 データベース上の "story_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "story_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId storyId;

  /**
   * このユーザーストーリーが属するプロダクトバックログを表す変数。
   *
   * <p>プロダクトバックログとユーザーストーリーの関連付けを表します。 データベース上の "backlog_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "backlog_id", nullable = false)
  @Getter
  private ProductBacklog productBacklog;

  /**
   * このユーザーストーリーが属するエピックを表す変数。
   *
   * <p>エピックとユーザーストーリーの関連付けを表します。 データベース上の "epic_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "epic_id")
  @Getter
  private Epic epic;

  /**
   * ユーザーストーリーのタイトルを表す変数。
   *
   * <p>ユーザーストーリーの識別に使用されるタイトルです。 データベース上の "title" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "title", nullable = false, length = 200)
  private String title;

  /**
   * ユーザーストーリーの詳細説明を表す変数。
   *
   * <p>ユーザーストーリーの詳細な説明を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * 受け入れ基準を表す変数。
   *
   * <p>ユーザーストーリーが完了したと見なされるための基準を示します。 データベース上の "acceptance_criteria" カラムに対応します。
   */
  @Getter
  @Column(name = "acceptance_criteria", length = 2000)
  private String acceptanceCriteria;

  /**
   * 優先度を表す変数。
   *
   * <p>ユーザーストーリーの優先度を示します。 データベース上の "priority" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false)
  private Priority priority;

  /**
   * ストーリーポイントを表す変数。
   *
   * <p>ユーザーストーリーの複雑さや作業量を示します。 データベース上の "story_points" カラムに対応します。
   */
  @Getter
  @Column(name = "story_points")
  private Integer storyPoints;

  /**
   * 価値を表す変数。
   *
   * <p>ユーザーストーリーのビジネス価値を示します。 データベース上の "value" カラムに対応します。
   */
  @Getter
  @Column(name = "value")
  private Integer value;

  /**
   * 状態を表す変数。
   *
   * <p>ユーザーストーリーの現在の状態を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  /**
   * バックログ内での順序を表す変数。
   *
   * <p>プロダクトバックログ内でのユーザーストーリーの順序を示します。 データベース上の "backlog_order" カラムに対応します。
   */
  @Getter
  @Column(name = "backlog_order")
  private Integer backlogOrder;

  /**
   * このユーザーストーリーに関連するタスクのセットを表す変数。
   *
   * <p>ユーザーストーリーとタスクの関連付けを表します。
   */
  @OneToMany(mappedBy = "userStory")
  @Getter
  private Set<Task> tasks = new HashSet<>();

  /**
   * ユーザーストーリーIDを取得します。
   *
   * @return ユーザーストーリーID
   */
  public SnowflakeId getStoryId() {
    return storyId;
  }

  /**
   * ユーザーストーリーIDを設定します。
   *
   * @param storyId 設定するユーザーストーリーID
   */
  public void setStoryId(SnowflakeId storyId) {
    this.storyId = storyId;
  }

  /**
   * このユーザーストーリーが属するプロダクトバックログを設定します。
   *
   * @param productBacklog 設定するプロダクトバックログ
   */
  public void setProductBacklog(ProductBacklog productBacklog) {
    this.productBacklog = productBacklog;
  }

  /**
   * このユーザーストーリーが属するエピックを設定します。
   *
   * @param epic 設定するエピック
   */
  public void setEpic(Epic epic) {
    this.epic = epic;
  }

  /**
   * バックログ内での順序を取得します。
   *
   * @return バックログ内での順序
   */
  public Integer getBacklogOrder() {
    return backlogOrder;
  }

  /**
   * バックログ内での順序を設定します。
   *
   * @param backlogOrder 設定するバックログ内での順序
   */
  public void setBacklogOrder(Integer backlogOrder) {
    this.backlogOrder = backlogOrder;
  }

  /**
   * このユーザーストーリーを分割して新しいユーザーストーリーを作成します。
   *
   * <p>元のユーザーストーリーの一部を新しいユーザーストーリーに移動します。
   *
   * @param newTitle 新しいユーザーストーリーのタイトル
   * @param newDescription 新しいユーザーストーリーの説明
   * @param newStoryPoints 新しいユーザーストーリーのストーリーポイント
   * @param newValue 新しいユーザーストーリーの価値
   * @return 分割された新しいユーザーストーリー
   */
  public UserStory split(
      String newTitle, String newDescription, Integer newStoryPoints, Integer newValue) {
    UserStory newStory = new UserStory();
    newStory.setProductBacklog(this.productBacklog);
    newStory.setEpic(this.epic);
    newStory.title = newTitle;
    newStory.description = newDescription;
    newStory.storyPoints = newStoryPoints;
    newStory.value = newValue;
    newStory.priority = this.priority;
    newStory.status = Status.TODO;

    // 元のストーリーのポイントを調整（分割後のポイントは元のポイントから新しいポイントを引いた値）
    if (this.storyPoints != null && newStoryPoints != null) {
      this.storyPoints = Math.max(0, this.storyPoints - newStoryPoints);
    }

    // 元のストーリーの価値を調整（分割後の価値は元の価値から新しい価値を引いた値）
    if (this.value != null && newValue != null) {
      this.value = Math.max(0, this.value - newValue);
    }

    return newStory;
  }

  /**
   * このユーザーストーリーを分割して新しいユーザーストーリーを作成します。
   *
   * <p>元のユーザーストーリーの一部を新しいユーザーストーリーに移動します。
   * 価値を指定しない場合は、元のユーザーストーリーの価値はそのままで、新しいユーザーストーリーの価値はnullになります。
   *
   * @param newTitle 新しいユーザーストーリーのタイトル
   * @param newDescription 新しいユーザーストーリーの説明
   * @param newStoryPoints 新しいユーザーストーリーのストーリーポイント
   * @return 分割された新しいユーザーストーリー
   */
  public UserStory split(String newTitle, String newDescription, Integer newStoryPoints) {
    return split(newTitle, newDescription, newStoryPoints, null);
  }

  /**
   * このメソッドはUserStoryクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return UserStoryオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "UserStory{"
        + "storyId="
        + storyId
        + ", productBacklog="
        + (productBacklog != null ? productBacklog.toString() : "null")
        + ", epic="
        + (epic != null ? epic.toString() : "null")
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + ", acceptanceCriteria='"
        + acceptanceCriteria
        + '\''
        + ", priority="
        + priority
        + ", storyPoints="
        + storyPoints
        + ", value="
        + value
        + ", status="
        + status
        + ", backlogOrder="
        + backlogOrder
        + '}';
  }

  /** ユーザーストーリーの優先度を表す列挙型。 */
  public enum Priority {
    /** 最高優先度。 */
    HIGHEST,

    /** 高優先度。 */
    HIGH,

    /** 中優先度。 */
    MEDIUM,

    /** 低優先度。 */
    LOW,

    /** 最低優先度。 */
    LOWEST
  }

  /** ユーザーストーリーの状態を表す列挙型。 */
  public enum Status {
    /** 未着手の状態。 */
    TODO,

    /** 進行中の状態。 */
    IN_PROGRESS,

    /** レビュー中の状態。 */
    REVIEW,

    /** 完了した状態。 */
    DONE
  }
}
