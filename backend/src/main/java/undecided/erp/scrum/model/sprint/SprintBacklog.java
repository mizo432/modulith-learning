package undecided.erp.scrum.model.sprint;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import undecided.erp.scrum.model.userStory.BusinessValue;
import undecided.erp.scrum.model.userStory.StoryPoint;
import undecided.erp.scrum.model.userStory.UserStory;

@Getter
public class SprintBacklog {

  public static final SprintBacklog EMPTY = new SprintBacklog();
  private final List<UserStory> userStories = new ArrayList<>();

  public SprintBacklog addUserStory(UserStory userStory) {
    // add userStory to the backlog...
    return this;
  }

  public SprintBacklog removeUserStory(UserStory userStory) {
    // remove userStory from the backlog...
    return this;
  }

  /**
   * スプリントバックログの完了したストーリーポイントの合計を計算します。
   *
   * @return 完了したストーリーポイントの合計値をStoryPointオブジェクトとして返します。
   */
  public StoryPoint sumCompletedStoryPoints() {
    int completedStoryPoint = userStories.stream()
        .filter(UserStory::isCompleted)
        .mapToInt(userStory -> userStory.getStoryPoint().getValue())
        .sum();

    return StoryPoint.of(completedStoryPoint);
  }

  /**
   * スプリントバックログの完了したビジネスバリューの合計を計算します。
   *
   * @return 完了したビジネスバリューの合計をビジネスバリューオブジェクトとして返します。
   */
  public BusinessValue sumCompletedBusinessValues() {
    Integer completedBusinessValue = userStories.stream()
        .filter(UserStory::isCompleted)
        .mapToInt(userStory -> userStory.getBusinessValue().getValue())
        .sum();

    return BusinessValue.of(completedBusinessValue);
  }
}
