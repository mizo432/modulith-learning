package undecided.erp.scrum.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
