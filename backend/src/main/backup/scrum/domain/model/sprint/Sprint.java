package undecided.erp.scrum.domain.model.sprint;

import com.ibm.icu.util.LocaleData;
import java.util.EnumSet;
import undecided.erp.common.verifier.EnumVerifiers;
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
//@Table(schema = "scrum", name = "sprints")
public record Sprint(
    SnowflakeId id,
    SnowflakeId productId,
    LocaleData startDate,
    LocaleData endDate,
    SprintBacklog sprintBacklog,
    SprintReview sprintReview,
    SprintRetrospective retrospective,
    SprintStatus status,
    StoryPoint completedStoryPoint,
    StoryPoint plannedStoryPoint,
    BusinessValue completedBusinessValue,
    BusinessValue plannedBusinessValue) {

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

    public static EnumSet<SprintStatus> beginableStatus() {
      return EnumSet.of(WAITING);
    }
  }

  public Sprint startSprint() {
    EnumVerifiers.verifyContains(status, SprintStatus.beginableStatus(),
        () -> new IllegalStateException(
            String.format("sprint %s は開始できません", status.name())));
    return new Sprint(id, productId, startDate, endDate, sprintBacklog, sprintReview, retrospective,
        SprintStatus.IN_PROGRESS, completedStoryPoint, plannedStoryPoint,
        completedBusinessValue, plannedBusinessValue);
  }

  public Sprint completeSprint() {
    EnumVerifiers.verifyContains(status, SprintStatus.completableStatus(),
        () -> new IllegalStateException(
            String.format("sprint %s は終了できません", status.name())));

    return new Sprint(id, productId, startDate, endDate, sprintBacklog, sprintReview, retrospective,
        SprintStatus.COMPLETE, completedStoryPoint, plannedStoryPoint,
        completedBusinessValue, plannedBusinessValue).calculateCompletedStoryPoint()
        .calculateCompletedBusinessValues();
  }

  public void moveToNextSprint(UserStory userStory, Sprint nextSprint) {
    this.sprintBacklog.removeUserStory(userStory);
    nextSprint.sprintBacklog().addUserStory(userStory);

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
