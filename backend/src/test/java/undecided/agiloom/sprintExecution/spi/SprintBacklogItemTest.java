package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

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

  private SprintBacklogItem createBacklogItem() {
    return SprintBacklogItem.builder()
        .id(SnowflakeId.of(1L))
        .sprintId(SnowflakeId.of(100L))
        .backlogItemId(SnowflakeId.of(200L))
        .workDescription("Implement login feature")
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
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
      assertThat(item.getUpdatedAt()).isNotNull();
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
  }

  @Nested
  @DisplayName("assignToメソッドのテスト")
  class AssignToMethodTest {

    @Test
    @DisplayName("担当者設定が更新される")
    void shouldUpdateAssignee() {
      SprintBacklogItem item = createBacklogItem();
      UUID assigneeId = UUID.randomUUID();

      item.assignTo(assigneeId);

      assertThat(item.getAssigneeId()).isEqualTo(assigneeId);
      assertThat(item.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("全プロパティが更新される")
    void shouldUpdateAllProperties() {
      SprintBacklogItem item = createBacklogItem();
      UUID assigneeId = UUID.randomUUID();

      item.update("New description", TaskStatus.IN_PROGRESS, assigneeId);

      assertThat(item.getWorkDescription()).isEqualTo("New description");
      assertThat(item.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
      assertThat(item.getAssigneeId()).isEqualTo(assigneeId);
      assertThat(item.getUpdatedAt()).isNotNull();
    }
  }
}
