package undecided.erp.scrum.domain.model.sprint;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.EnumSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.common.verifier.EnumVerifiers;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.userStory.BusinessValue;
import undecided.erp.scrum.domain.model.userStory.StoryPoint;
import undecided.erp.scrum.domain.model.userStory.UserStory;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * SprintクラスはScrumプロジェクトのスプリントを表現します。
 * <p>
 * スプリントの開始と終了の日付、 スプリントバックログ、スプリントレビュー、レトロスペクティブなどの情報を含みます。また、スプリントの状態と 完了したストーリーポイントの数を追跡します。
 * <p>
 * スプリントの状態はPLANNING、IN_PROGRESS、またはCOMPLETEDに設定することができます。デフォルトでは、スプリントが作成されたときに状態はPLANNINGに設定されます。
 * <p>
 * スプリントバックログは、スプリント中に完了する予定のユーザーストーリーの集まりです。スプリントバックログは、`SprintBacklog`クラスの`addUserStory`と`removeUserStory`メソッドを使用して追加または削除することができます。
 * <p>
 * スプリントレビューは、スプリント間の完了した作業をデモンストレートするミーティングです。ミーティングの日付、参加者のリスト、議論された問題、特定された改善点が含まれます。スプリントレビューは、`setSprintReview`メソッドを使用して設定できます。
 * <p>
 * レトロスペクティブは、スプリントについて反省し、改善の余地を特定するミーティングです。うまくいったこと、うまくいかなかったこと、改善できることが含まれます。レトロスペクティブは、`setSprintRetrospective`メソッドを使用して設定できます。
 * <p>
 * 完成したストーリーポイントは、スプリントバックログにて完了とマークされたユーザーストーリーに基づいて計算されます。完成したストーリーポイントは、`getCompletedStoryPoints`メソッドを使用して取得できます。
 * <p>
 * スプリントは、`startSprint`メソッドを使って開始できます。これは、ステータスをIN_PROGRESSに設定します。
 * <p>
 * スプリントは、`completeSprint`メソッドを使って完了できます。これは、完了したストーリーポイントを計算し、ステータスをCOMPLETEDに設定します。
 * <p>
 * `moveToNextSprint`メソッドは、現在のスプリントのバックログから次のスプリントのバックログにユーザーストーリーを移動させます。これは、ユーザーストーリーが現在のスプリントでは完了できず、次のスプリントに持ち越す必要がある場合に使用できます。
 */
@Table(schema = "scrum", name = "sprints")
@Entity
@Getter
@AllArgsConstructor
public class Sprint {

  /**
   * SprintStatus列挙型は、スプリントの可能なステータスを表します。
   */
  public enum SprintStatus {
    WAITING,
    IN_PROGRESS,
    COMPLETE;

    public static EnumSet<SprintStatus> completableStatus() {
      return EnumSet.of(IN_PROGRESS);
    }

    public static EnumSet<SprintStatus> startbleStatus() {
      return EnumSet.of(WAITING);
    }
  }

  @EmbeddedId
  @Id
  @Column(name = "sprint_id")
  private SnowflakeId<Sprint> id;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "product_id", nullable = false))
  private SnowflakeId<Product> productId;
  private Date startDate;
  private Date endDate;
  @Transient
  private SprintBacklog sprintBacklog = SprintBacklog.EMPTY;
  @Transient
  private SprintReview sprintReview = SprintReview.EMPTY;
  @Transient
  private SprintRetrospective retrospective = SprintRetrospective.EMPTY;
  /**
   * スプリントの状態を表現します。
   */
  @Column(nullable = false, name = "sprint_status")
  @Enumerated(EnumType.STRING)
  private SprintStatus status;
  /**
   * スプリントの完了したストーリーポイントを表します。
   */
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "completed_story_point"))
  private StoryPoint completedStoryPoint;
  /**
   * スプリントの予定ストーリーポイントを表します。
   */
  @Embedded
  @AttributeOverride(column = @Column(name = "planned_story_point"), name = "value")
  private StoryPoint plannedStoryPoint;
  @Embedded
  @AttributeOverride(column = @Column(name = "completed_business_value"), name = "value")
  private BusinessValue completedBusinessValue;
  @Embedded
  @AttributeOverride(column = @Column(name = "planned_business_value"), name = "value")
  private BusinessValue plannedBusinessValue;

  public Sprint() {
    this.status = SprintStatus.WAITING;
  }

  public void startSprint() {
    EnumVerifiers.verifyContains(status, SprintStatus.startbleStatus(),
        () -> new IllegalStateException(
            String.format("sprint %s は開始できません", status.name())));
    this.status = SprintStatus.IN_PROGRESS;
  }

  public Sprint completeSprint() {
    EnumVerifiers.verifyContains(status, SprintStatus.completableStatus(),
        () -> new IllegalStateException(
            String.format("sprint %s は終了できません", status.name())));

    return new Sprint(id, productId, startDate, endDate, sprintBacklog, sprintReview, retrospective,
        this.status = SprintStatus.COMPLETE, completedStoryPoint, plannedStoryPoint,
        completedBusinessValue, plannedBusinessValue).calculateCompletedStoryPoint()
        .calculateCompletedBusinessValues();
  }

  // This method moves a UserStory from this sprint's backlog to the backlog of the next sprint
  public void moveToNextSprint(UserStory userStory, Sprint nextSprint) {
    this.sprintBacklog.removeUserStory(userStory);
    nextSprint.getSprintBacklog().addUserStory(userStory);

  }

  // other sprint methods...
  public Sprint calculateCompletedStoryPoint() {
    return new Sprint(id, productId, startDate, endDate, sprintBacklog, sprintReview, retrospective,
        status, sprintBacklog.sumCompletedStoryPoints(), plannedStoryPoint, completedBusinessValue,
        plannedBusinessValue);
  }

  public Sprint calculateCompletedBusinessValues() {
    return new Sprint(id, productId, startDate, endDate, sprintBacklog, sprintReview, retrospective,
        status, completedStoryPoint, plannedStoryPoint, sprintBacklog.sumCompletedBusinessValues(),
        plannedBusinessValue);
  }

  public StoryPoint getCompletedStoryPoints() {
    if (status != SprintStatus.COMPLETE) {
      throw new UnsupportedOperationException(
          "Cannot get completed story points for an incomplete sprint");
    }
    return completedStoryPoint;
  }
}
