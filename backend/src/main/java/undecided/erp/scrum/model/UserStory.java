package undecided.erp.scrum.model;

import java.util.List;
import lombok.Getter;
import undecided.erp.relMgmt.model.personRole.actor.Actor;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * UserStory クラスは、ソフトウェアプロジェクトにおけるユーザーストーリーを表します。
 * <p>
 * アクター、アクション、価値、承認基準、優先度、ストーリーポイント、ステータス、コメント、機能、タスク、ストーリータイプなどの情報を含みます。
 */
@Getter
public class UserStory {

  public enum UserStoryStatus {
    ICEBOX,
    VALUE,
    ESTIMATING,
    READY,
    TO_DO,
    IN_PROGRESS,
    TESTING,
    DONE,
    DROP
  }

  public enum StoryType {
    FEATURE,
    CHORE,
    BUG,
    NON_FUNCTIONAL

  }

  private SnowflakeId<UserStory> id;
  private SnowflakeId<Actor> actorId;
  private Actor actor;
  private String title;
  private String description;
  private final BusinessValue businessValue = BusinessValue.EMPTY;
  private String acceptanceCriteria;
  private Integer priority;
  private StoryPoint storyPoint;
  private final UserStoryStatus status;
  private String comments;
  private Feature feature;
  private List<Task> tasks;
  private StoryType storyType;
  /** スプリント開始時点のビジネス価値 */
  private final BusinessValue plannedBusinessValue = BusinessValue.EMPTY;
  /** ユーザーストーリー終了時点のビジネス価値 */
  private final BusinessValue completedBusinessValue = BusinessValue.EMPTY;
  /** スプリント開始時点のストーリーポイント */
  private final StoryPoint plannedStoryPoint = StoryPoint.EMPTY;
  /** ユーザーストーリー終了時点のストーリーポイント */
  private final StoryPoint completedStoryPoint = StoryPoint.EMPTY;

  public UserStory() {
    this.status = UserStoryStatus.VALUE;

  }

  public boolean isCompleted() {
    return status == UserStoryStatus.DONE;
  }
}
