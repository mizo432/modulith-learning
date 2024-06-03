package undecided.erp.scrum.model;

import java.util.List;
import lombok.Getter;
import undecided.erp.relMgmt.model.personRole.actor.Actor;

/**
 * UserStory クラスは、ソフトウェアプロジェクトにおけるユーザーストーリーを表します。
 * <p>
 * アクター、アクション、価値、承認基準、優先度、ストーリーポイント、ステータス、コメント、機能、タスク、ストーリータイプなどの情報を含みます。
 */
@Getter
public class UserStory {

  private Actor actor;
  private String title;
  private String description;
  private String value;
  private String acceptanceCriteria;
  private Integer priority;
  private Integer storyPoints;
  private final UserStoryStatus status;
  private String comments;
  private Feature feature;
  private List<Task> tasks;
  private StoryType storyType;

  public UserStory() {
    this.status = UserStoryStatus.VALUE;

  }

  public boolean isCompleted() {
    return status == UserStoryStatus.DONE;
  }
}
