package undecided.erp.scrum.domain.model.userStory;

import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@Setter
public class StoryPoint {

  private Integer value;

  public static final StoryPoint EMPTY = new StoryPoint();

  public static StoryPoint of(Integer value) {
    return new StoryPoint(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class StoriesPoint {

    private Integer value;

    public StoryPoint toStoryPoint() {
      return StoryPoint.of(value);

    }


    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StoriesPoint of(List<StoryPoint> value) {
      return new StoriesPoint(value.stream().mapToInt(StoryPoint::getValue).sum());

    }
  }

}
