package undecided.erp.scrum.model;

import java.util.List;
import lombok.Getter;

@Getter
public class SprintBacklog {

  private List<UserStory> userStories;

  public void addUserStory(UserStory userStory) {
    // add userStory to the backlog...
  }

  public void removeUserStory(UserStory userStory) {
    // remove userStory from the backlog...
  }

  // other backlog methods...

}
