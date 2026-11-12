package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintBacklogItemのテスト")
class SprintBacklogItemTest {

  private static final UUID FIXED_ASSIGNEE_ID =
      UUID.fromString("55555555-5555-5555-5555-555555555555");

  private SprintBacklogItem createBacklogItem() {
    return SprintBacklogItem.of(
        SnowflakeId.of(1L),
        SnowflakeId.of(100L),
        SnowflakeId.of(200L),
        "Implement login feature",
        null,
        null,
        null,
        null);
  }

  @Nested
  @DisplayName("ファクトリーメソッドのテスト")
  class FactoryMethodTest {

    @Test
    @DisplayName("ファクトリーメソッドでインスタンスが作成される")
    void shouldCreateInstanceWithFactoryMethod() {
      SprintBacklogItem item = createBacklogItem();

      assertThat(item).isNotNull();
      assertThat(item.getWorkDescription()).isEqualTo("Implement login feature");
      assertThat(item.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    @DisplayName("デフォルトステータスはTODOである")
    void shouldHaveDefaultStatusTodo() {
      SprintBacklogItem item = createBacklogItem();

      assertThat(item.getStatus()).isEqualTo(TaskStatus.TODO);
    }
  }

  @Nested
  @DisplayName("updateStatusメソッドのテスト")
  class UpdateStatusMethodTest {

    @Test
    @DisplayName("ステータスが更新される")
    void shouldUpdateStatus() {
      SprintBacklogItem item = createBacklogItem();

      item.updateStatus(TaskStatus.IN_PROGRESS);

      assertThat(item.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("全ステータスへ遷移できる")
    void shouldTransitionToAllStatuses() {
      SprintBacklogItem item = createBacklogItem();

      item.updateStatus(TaskStatus.IN_PROGRESS);
      assertThat(item.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

      item.updateStatus(TaskStatus.IN_REVIEW);
      assertThat(item.getStatus()).isEqualTo(TaskStatus.IN_REVIEW);

      item.updateStatus(TaskStatus.DONE);
      assertThat(item.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    @DisplayName("statusがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStatusIsNull() {
      SprintBacklogItem item = createBacklogItem();

      assertThatThrownBy(() -> item.updateStatus(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("status must not be null");
    }
  }

  @Nested
  @DisplayName("assignToメソッドのテスト")
  class AssignToMethodTest {

    @Test
    @DisplayName("担当者設定が更新される")
    void shouldUpdateAssignee() {
      SprintBacklogItem item = createBacklogItem();

      item.assignTo(FIXED_ASSIGNEE_ID);

      assertThat(item.getAssigneeId()).isEqualTo(FIXED_ASSIGNEE_ID);
    }

    @Test
    @DisplayName("assigneeIdがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenAssigneeIdIsNull() {
      SprintBacklogItem item = createBacklogItem();

      assertThatThrownBy(() -> item.assignTo(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("assigneeId must not be null");
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("全プロパティが更新される")
    void shouldUpdateAllProperties() {
      SprintBacklogItem item = createBacklogItem();

      item.update("New description", TaskStatus.IN_PROGRESS, FIXED_ASSIGNEE_ID);

      assertThat(item.getWorkDescription()).isEqualTo("New description");
      assertThat(item.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
      assertThat(item.getAssigneeId()).isEqualTo(FIXED_ASSIGNEE_ID);
    }

    @Test
    @DisplayName("workDescriptionがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenWorkDescriptionIsNull() {
      SprintBacklogItem item = createBacklogItem();

      assertThatThrownBy(() -> item.update(null, TaskStatus.IN_PROGRESS, FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("workDescription must not be null");
    }

    @Test
    @DisplayName("statusがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStatusIsNull() {
      SprintBacklogItem item = createBacklogItem();

      assertThatThrownBy(() -> item.update("Description", null, FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("status must not be null");
    }

    @Test
    @DisplayName("workDescriptionが2000文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenWorkDescriptionExceedsLength() {
      SprintBacklogItem item = createBacklogItem();
      String longDescription = "A".repeat(2001);

      assertThatThrownBy(
              () -> item.update(longDescription, TaskStatus.IN_PROGRESS, FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("workDescription must not exceed 2000 characters");
    }

    @Test
    @DisplayName("assigneeIdがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenAssigneeIdIsNull() {
      SprintBacklogItem item = createBacklogItem();

      assertThatThrownBy(() -> item.update("Description", TaskStatus.IN_PROGRESS, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("assigneeId must not be null");
    }
  }

  @Nested
  @DisplayName("論理削除後の変更禁止テスト")
  class DeletedEntityChangeProhibitionTest {

    @Test
    @DisplayName("論理削除後にupdateStatusすると例外がスローされる")
    void shouldThrowExceptionWhenUpdateStatusAfterDeleted() {
      SprintBacklogItem item = createBacklogItem();
      item.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> item.updateStatus(TaskStatus.IN_PROGRESS))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にassignToすると例外がスローされる")
    void shouldThrowExceptionWhenAssignToAfterDeleted() {
      SprintBacklogItem item = createBacklogItem();
      item.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> item.assignTo(FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にupdateすると例外がスローされる")
    void shouldThrowExceptionWhenUpdateAfterDeleted() {
      SprintBacklogItem item = createBacklogItem();
      item.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(
              () -> item.update("New description", TaskStatus.IN_PROGRESS, FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }
  }
}
