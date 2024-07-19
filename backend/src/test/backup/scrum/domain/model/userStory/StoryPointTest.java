package undecided.erp.scrum.domain.model.userStory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import undecided.erp.scrum.domain.model.userStory.StoryPoint.StoriesPoint;


/**
 * StoryPointTest クラスは、StoryPoint および StoriesPoint クラスの機能をテストするために使用されます。
 * <p>
 * これには、testOfMethodという単一のテストメソッドが含まれており、StoryPointと
 * StoriesPointのインスタンスを作成、いくつかの操作を行い、期待される結果をアサートします。
 */
class StoryPointTest {

  @Test
  void testOfMethod() {

    StoryPoint storyPoint1 = StoryPoint.of(5);
    StoryPoint storyPoint2 = StoryPoint.of(10);
    StoryPoint storyPoint3 = StoryPoint.of(15);

    StoryPoint expectedStoryPoint = StoryPoint.of(30);
    StoriesPoint actualStoriesPoint = StoriesPoint.of(
        Arrays.asList(storyPoint1, storyPoint2, storyPoint3));

    assertThat(actualStoriesPoint.toStoryPoint())
        .isEqualTo(expectedStoryPoint);

  }
}