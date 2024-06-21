package undecided.erp.scrum.model.product;

import java.util.List;
import undecided.erp.scrum.model.userStory.UserStory;

public class ProductBacklog {

  private List<UserStory> userStories;

  // Constructor, getter and setter methods...

  public void addUserStory(UserStory userStory) {
    this.userStories.add(userStory);
    // You might want to sort userStories based on their priority after adding a new one
  }

  public void removeUserStory(UserStory userStory) {
    this.userStories.remove(userStory);
  }

  // Implement your own method to sort userStories, perhaps based on their priority
}
