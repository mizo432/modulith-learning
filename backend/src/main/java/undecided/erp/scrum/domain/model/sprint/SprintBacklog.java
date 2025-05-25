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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.task.UserStory;

/**
 * SprintBacklogクラスは、エンティティとしてスクラム開発におけるスプリントバックログを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`backlogId`: スプリントバックログ固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`sprint`: このバックログが属するスプリント。
 *   <li>`name`: スプリントバックログの名前。
 *   <li>`description`: スプリントバックログの詳細説明。
 *   <li>`estimatedEffort`: 見積もり工数。
 *   <li>`remainingEffort`: 残り工数。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "sprint_backlogs")
@NoArgsConstructor
@Builder
public class SprintBacklog extends PptEntity<SprintBacklog> implements Serializable {

  /**
   * ユニークなスプリントバックログ識別子を表す変数。
   *
   * <p>アプリケーションにおける各スプリントバックログを一意に識別するために使用されます。 データベース上の "backlog_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "backlog_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId backlogId;

  /**
   * このバックログが属するスプリントを表す変数。
   *
   * <p>スプリントとスプリントバックログの関連付けを表します。 データベース上の "sprint_id" カラムに対応し、null 値は許可されていません。
   */
  @ManyToOne
  @JoinColumn(name = "sprint_id", nullable = false)
  @Getter
  private Sprint sprint;

  /**
   * このバックログアイテムに関連するユーザーストーリーを表す変数。
   *
   * <p>ユーザーストーリーとスプリントバックログの関連付けを表します。 データベース上の "story_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "story_id")
  @Getter
  private UserStory userStory;

  /**
   * スプリントバックログの名前を表す変数。
   *
   * <p>スプリントバックログの識別に使用される名前です。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * スプリントバックログの詳細説明を表す変数。
   *
   * <p>スプリントバックログの詳細な説明や背景情報を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * 見積もり工数を表す変数。
   *
   * <p>スプリントバックログの完了に必要な見積もり工数を示します。 データベース上の "estimated_effort" カラムに対応します。
   */
  @Getter
  @Column(name = "estimated_effort")
  private Integer estimatedEffort;

  /**
   * 残り工数を表す変数。
   *
   * <p>スプリントバックログの完了までの残り工数を示します。 データベース上の "remaining_effort" カラムに対応します。
   */
  @Getter
  @Column(name = "remaining_effort")
  private Integer remainingEffort;

  /**
   * 見積もり工数を更新します。
   *
   * @param estimatedEffort 新しい見積もり工数
   * @return 更新されたSprintBacklogオブジェクト
   */
  public SprintBacklog updateEstimatedEffort(Integer estimatedEffort) {
    this.estimatedEffort = estimatedEffort;
    return this;
  }

  /**
   * 残り工数を更新します。
   *
   * @param remainingEffort 新しい残り工数
   * @return 更新されたSprintBacklogオブジェクト
   */
  public SprintBacklog updateRemainingEffort(Integer remainingEffort) {
    this.remainingEffort = remainingEffort;
    return this;
  }

  /**
   * 進捗率を計算します。
   *
   * @return 進捗率（0〜100の整数）。見積もり工数がnullまたは0の場合は0を返します。
   */
  public int calculateProgress() {
    if (estimatedEffort == null || estimatedEffort == 0) {
      return 0;
    }

    int remaining = remainingEffort != null ? remainingEffort : estimatedEffort;
    int completed = estimatedEffort - remaining;
    return (int) Math.round((double) completed / estimatedEffort * 100);
  }

  /**
   * ユーザーストーリーを設定します。
   *
   * @param userStory 設定するユーザーストーリー
   * @return 更新されたSprintBacklogオブジェクト
   */
  public SprintBacklog setUserStory(UserStory userStory) {
    this.userStory = userStory;
    return this;
  }

  /**
   * このメソッドはSprintBacklogクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return SprintBacklogオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "SprintBacklog{"
        + "backlogId="
        + backlogId
        + ", sprint="
        + (sprint != null ? sprint.toString() : "null")
        + ", userStory="
        + (userStory != null ? userStory.getStoryId() : "null")
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", estimatedEffort="
        + estimatedEffort
        + ", remainingEffort="
        + remainingEffort
        + '}';
  }
}
