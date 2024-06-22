package undecided.erp.scrum.domain.model.userStory;

import jakarta.persistence.Embeddable;
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
}
