package undecided.erp.scrum.domain.model.userStory.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.person.Person;
import undecided.erp.scrum.domain.model.userStory.UserStory;
import undecided.erp.shared.entity.SnowflakeId;

class TaskTest {

  @Nested
  class CreateTest {

    @Test
    @DisplayName("タスクを作成し、一致するフィールドと非nullのタスクを返す必要があります")
    void shouldCreateTaskAndReturnNonNullTaskWithMatchingFields() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test description";
      Task task = Task.create(userStoryId, title, description);
      assertThat(task).isNotNull();
      assertThat(task.getUserStoryId()).isEqualTo(userStoryId);
      assertThat(task.getTitle()).isEqualTo(title);
      assertThat(task.getDescription()).isEqualTo(description);
    }

  }

  @Nested
  @DisplayName("タスクへの割り当てテスト")
  class AssignToTaskTest {

    @Test
    @DisplayName("開発者をタスクに割り当て、一致するフィールドと新しいタスクを返す必要があります")
    void shouldAssignDeveloperToTaskAndReturnNewTaskWithMatchingFields() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      Task task = Task.create(userStoryId, title, description);

      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task assignedTask = task.assignToTask(developerId);

      assertThat(assignedTask).isNotNull();
      assertThat(assignedTask.getAssignedDeveloperId()).isEqualTo(developerId);
    }
  }

  @Nested
  @DisplayName("Task estimation tests")
  class TaskEstimationTest {

    @Test
    @DisplayName("新しいタスクオブジェクトを返し、見積もり時間を更新します")
    void shouldReturnNewTaskWithUpdatedEstimatedHours() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      Task task = Task.create(userStoryId, title, description);

      Double estimatedHours = 2.0;
      Task estimatedTask = task.estimateTask(estimatedHours);

      assertThat(estimatedTask).isNotNull();
      assertThat(estimatedTask.getEstimatedHours()).isEqualTo(estimatedHours);
    }

    @Test
    @DisplayName("見積もり時間が負のとき、IllegalArgumentExceptionをスローする必要があります")
    void shouldThrowExceptionWhenEstimatedHoursAreNegative() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      Task task = Task.create(userStoryId, title, description);

      Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
        task.estimateTask(-1.0);
      });
    }
  }

  @Nested
  @DisplayName("Task start tests")
  class TaskStartTest {

    @Test
    @DisplayName("Should start a task and return task with in progress status")
    void shouldStartTaskAndReturnTaskInProgressStatus() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);

      Task startedTask = task.startTask();

      assertThat(startedTask).isNotNull();
      assertThat(startedTask.getStatus()).isEqualTo(Task.TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Should throw exception when starting a task that is already in progress")
    void shouldThrowExceptionWhenStartingAlreadyInProgressTask() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);

      Task alreadyStarted = task.startTask();

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(alreadyStarted::startTask);
    }

    @Test
    @DisplayName("Should throw exception when starting a task that is already done")
    void shouldThrowExceptionWhenStartingAlreadyDoneTask() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);
      Task alreadyDone = task.startTask().turnToInReview().finishTask();

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(alreadyDone::startTask);
    }
  }

  @Nested
  @DisplayName("Task review tests")
  class TaskReviewTest {

    @Test
    @DisplayName("Should review a task and return task with in review status")
    void shouldTurnToReviewTaskAndReturnTaskInReviewStatus() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description);
      task = task.assignToTask(developerId);
      task = task.estimateTask(2.0);
      Task inProgressTask = task.startTask();

      Task taskInReview = inProgressTask.turnToInReview();

      assertThat(taskInReview).isNotNull();
      assertThat(taskInReview.getStatus()).isEqualTo(Task.TaskStatus.IN_REVIEW);
    }

    @Test
    @DisplayName("Should throw exception when reviewing a task that is not started")
    void shouldThrowExceptionWhenReviewingTaskThatIsNotStarted() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description);
      task = task.assignToTask(developerId);
      task = task.estimateTask(2.0);

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(task::turnToInReview)
          .withMessageContaining("Cannot turn to inReview a task that is yet started");
    }

    @Test
    @DisplayName("Should throw exception when reviewing a task that is already done")
    void shouldThrowExceptionWhenReviewingTaskThatIsAlreadyDone() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description);
      task = task.assignToTask(developerId);
      task = task.estimateTask(2.0);
      Task alreadyDone = task.startTask().turnToInReview().finishTask();

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(alreadyDone::turnToInReview)
          .withMessageContaining("Cannot turn to inReview a task that is already done");
    }

    @Test
    @DisplayName("Should throw exception when reviewing a task that is already in review")
    void shouldThrowExceptionWhenReviewingTaskThatIsAlreadyInReview() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description);
      task = task.assignToTask(developerId);
      task = task.estimateTask(2.0);
      Task alreadyInProgress = task.startTask();
      Task alreadyInReview = alreadyInProgress.turnToInReview();

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(alreadyInReview::turnToInReview)
          .withMessageContaining("Cannot turn to inReview a task that is already in a review");
    }
  }

  @Nested
  @DisplayName("Task finish tests")
  class TaskFinishTest {

    @Test
    @DisplayName("Should finish a task and return task with done status")
    void shouldFinishTaskAndReturnTaskWithDoneStatus() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);
      Task taskInProgress = task.startTask();
      Task taskInReview = taskInProgress.turnToInReview();

      Task doneTask = taskInReview.finishTask();

      assertThat(doneTask).isNotNull();
      assertThat(doneTask.getStatus()).isEqualTo(Task.TaskStatus.DONE);
    }

    @Test
    @DisplayName("Should throw exception when finishing a task that is already done")
    void shouldThrowExceptionWhenFinishingAlreadyDoneTask() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);
      Task taskInProgress = task.startTask();
      Task taskInReview = taskInProgress.turnToInReview();
      Task doneTask = taskInReview.finishTask();

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(doneTask::finishTask);
    }

    @Test
    @DisplayName("Should throw exception when finishing a task that has not yet started")
    void shouldThrowExceptionWhenFinishingYetToStartTask() {
      SnowflakeId<UserStory> userStoryId = SnowflakeId.newInstance();
      String title = "Test Title";
      String description = "Test Description";
      SnowflakeId<Person> developerId = SnowflakeId.newInstance();
      Task task = Task.create(userStoryId, title, description).assignToTask(developerId)
          .estimateTask(2.0);

      Assertions.assertThatExceptionOfType(IllegalStateException.class)
          .isThrownBy(task::finishTask);
    }

  }
}
