package undecided.erp.scrum.model;

import java.util.Date;
import lombok.Getter;

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
@Getter
public class Sprint {

  public enum Status {
    PLANNING, IN_PROGRESS, COMPLETED
  }

  private Date startDate;
  private Date endDate;
  private SprintBacklog sprintBacklog;
  private SprintReview sprintReview;
  private SprintRetrospective retrospective;
  private Status status;
  private int completedStoryPoints;

  public Sprint() {
    // The initial status of the sprint could be set to PLANNING
    this.status = Status.PLANNING;
  }

  public void startSprint() {
    // When the sprint is started, set the status to IN_PROGRESS
    this.status = Status.IN_PROGRESS;
  }

  public void completeSprint() {
    calculateCompletedStoryPoints();
    this.status = Status.COMPLETED;
  }

  // This method moves a UserStory from this sprint's backlog to the backlog of the next sprint
  public void moveToNextSprint(UserStory userStory, Sprint nextSprint) {
    this.sprintBacklog.removeUserStory(userStory);
    nextSprint.getSprintBacklog().addUserStory(userStory);

  }

  // other sprint methods...
  public void calculateCompletedStoryPoints() {
    for (UserStory userStory : sprintBacklog.getUserStories()) {
      if (userStory.isCompleted()) {
        completedStoryPoints += userStory.getStoryPoints();
      }
    }
  }

  public int getCompletedStoryPoints() {
    if (status != Status.COMPLETED) {
      throw new UnsupportedOperationException(
          "Cannot get completed story points for an incomplete sprint");
    }
    return completedStoryPoints;
  }
}
