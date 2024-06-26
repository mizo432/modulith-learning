package undecided.erp.scrum.domain.model.userStory;

import java.util.EnumSet;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.erp.common.verifier.EnumVerifiers;
import undecided.erp.relMgmt.domain.model.personRole.actor.Actor;
import undecided.erp.scrum.domain.model.feature.Feature;
import undecided.erp.scrum.domain.model.userStory.task.Task;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * UserStory クラスは、ソフトウェアプロジェクトにおけるユーザーストーリーを表します。
 * <p>
 * アクター、アクション、価値、承認基準、優先度、ストーリーポイント、ステータス、コメント、機能、タスク、ストーリータイプなどの情報を含みます。
 */
@Getter
@RequiredArgsConstructor
public class UserStory {

  public enum UserStoryStatus {
    ICEBOX,
    VALUE,
    READY,
    ESTIMATED,
    TO_DO,
    IN_PROGRESS,
    REVIEW,
    DONE,
    DROP;

    public static EnumSet<UserStoryStatus> turnableReady() {
      return EnumSet.of(VALUE);
    }
  }

  @Getter
  public enum StoryType {
    FEATURE("機能追加"),
    CHORE("軽微な"),
    BUG("バグ対応"),
    NON_FUNCTIONAL("非機能"),
    TECHNICAL("技術的");

    private final String description;

    StoryType(String description) {
      this.description = description;
    }
  }

  private final SnowflakeId<UserStory> id;
  private final SnowflakeId<Actor> actorId;
  private Actor actor;
  private final String title;
  private String description;
  private final BusinessValue businessValue = BusinessValue.EMPTY;
  private final String acceptanceCriteria;
  private final Integer priority;
  private final StoryPoint storyPoint = StoryPoint.EMPTY;
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
    this.id = null;
    this.actorId = null;
    this.title = null;
    this.acceptanceCriteria = null;
    this.priority = null;

  }

  public boolean isCompleted() {
    return status == UserStoryStatus.DONE;
  }

  public UserStory turnToReady() {
    EnumVerifiers.verifyContains(status, UserStoryStatus.turnableReady(),
        () -> new IllegalStateException(""));
    return new UserStory(id, actorId, title, acceptanceCriteria, priority, UserStoryStatus.READY);

  }


}
