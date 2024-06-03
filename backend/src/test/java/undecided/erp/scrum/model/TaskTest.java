package undecided.erp.scrum.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import undecided.erp.relMgmt.model.personRole.developer.Developer;
import undecided.erp.shared.entity.SnowflakeId;

public class TaskTest {

  @Test
  void createTaskSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "Test title", "Test description");

    assertNotNull(task);
    assertEquals(mockUserStoryId, task.getUserStoryId());
    assertEquals("Test title", task.getTitle());
    assertEquals("Test description", task.getDescription());
    assertEquals(TaskStatus.TO_DO, task.getStatus());
  }

  @Test
  void assignDeveloperToTaskSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestAssignDevTitle", "TestAssignDevDescription");
    Developer developer = new Developer();

    Task assignedTask = task.assignToTask(developer);

    assertNotNull(assignedTask);
    assertEquals(mockUserStoryId, assignedTask.getUserStoryId());
    assertEquals("TestAssignDevTitle", assignedTask.getTitle());
    assertEquals("TestAssignDevDescription", assignedTask.getDescription());
    assertEquals(TaskStatus.TO_DO, assignedTask.getStatus());
    assertEquals(developer, assignedTask.getAssignedDeveloper());
  }

  @Test
  void estimateTaskSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestEstimateHoursTitle",
        "TestEstimateHoursDescription");
    Double estimatedHours = 5.0;

    Task estimatedTask = task.estimateTask(estimatedHours);

    assertNotNull(estimatedTask);
    assertEquals(mockUserStoryId, estimatedTask.getUserStoryId());
    assertEquals("TestEstimateHoursTitle", estimatedTask.getTitle());
    assertEquals("TestEstimateHoursDescription", estimatedTask.getDescription());
    assertEquals(TaskStatus.TO_DO, estimatedTask.getStatus());
    assertEquals(estimatedHours, estimatedTask.getEstimatedHours());
  }

  @Test
  void estimateTaskWithNegativeHoursFailed() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestEstimateHoursTitle",
        "TestEstimateHoursDescription");
    Double negativeHours = -5.0;

    assertThrows(IllegalArgumentException.class, () -> task.estimateTask(negativeHours),
        "Estimated hours cannot be negative");
  }

  @Test
  void startTaskSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestStartTaskTitle", "TestStartTaskDescription");

    Task startedTask = task.startTask();

    assertNotNull(startedTask);
    assertEquals(mockUserStoryId, startedTask.getUserStoryId());
    assertEquals("TestStartTaskTitle", startedTask.getTitle());
    assertEquals("TestStartTaskDescription", startedTask.getDescription());
    assertEquals(TaskStatus.IN_PROGRESS, startedTask.getStatus());
  }

  @Test
  void startTaskStatusInprogressUnsuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestStartTaskInprogressTitle",
        "TestStartTaskInprogressDescription");
    task = task.startTask();

    assertThrows(IllegalStateException.class, task::startTask,
        "Cannot start a task that is already in progress");
  }

  @Test
  void startTaskStatusInReviewUnsuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestStartTaskInReviewTitle",
        "TestStartTaskInReviewDescription");
    task = task.turnToInReview(2.0);

    assertThrows(IllegalStateException.class, task::startTask,
        "Cannot start a task that is already in a review");
  }

  @Test
  void startTaskStatusDoneUnsuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestStartTaskDoneTitle",
        "TestStartTaskDoneDescription");
    task = task.finishTask();

    assertThrows(IllegalStateException.class, task::startTask,
        "Cannot start a task that is already done");
  }

  @Test
  void turnTaskToInReviewSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestTurnTaskToInReviewTitle",
        "TestTurnTaskToInReviewDescription");
    Double reviewHours = 2.0;

    Task reviewedTask = task.turnToInReview(reviewHours);

    assertNotNull(reviewedTask);
    assertEquals(mockUserStoryId, reviewedTask.getUserStoryId());
    assertEquals("TestTurnTaskToInReviewTitle", reviewedTask.getTitle());
    assertEquals("TestTurnTaskToInReviewDescription", reviewedTask.getDescription());
    assertEquals(TaskStatus.IN_REVIEW, reviewedTask.getStatus());
    assertEquals(reviewHours, reviewedTask.getEstimatedHours());
  }

  @Test
  void finishTaskSuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestFinishTaskTitle", "TestFinishTaskDescription");
    task = task.startTask();

    Task finishedTask = task.finishTask();

    assertNotNull(finishedTask);
    assertEquals(mockUserStoryId, finishedTask.getUserStoryId());
    assertEquals("TestFinishTaskTitle", finishedTask.getTitle());
    assertEquals("TestFinishTaskDescription", finishedTask.getDescription());
    assertEquals(TaskStatus.DONE, finishedTask.getStatus());
  }

  @Test
  void finishTaskStatusDoneUnsuccessfully() {
    SnowflakeId<UserStory> mockUserStoryId = SnowflakeId.newInstance();
    Task task = Task.create(mockUserStoryId, "TestFinishDoneTaskTitle",
        "TestFinishDoneTaskDescription");
    task = task.finishTask();

    assertThrows(IllegalStateException.class, task::finishTask,
        "Cannot finish a task that is already done");
  }
}
