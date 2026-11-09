package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintGoalのテスト")
class SprintGoalTest {

  private SprintGoal createSprintGoal() {
    return SprintGoal.builder()
        .id(SnowflakeId.of(1L))
        .sprintId(SnowflakeId.of(100L))
        .goalDescription("Complete user authentication feature")
        .relatedItems("Login, Register, Forgot Password")
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      SprintGoal goal = createSprintGoal();

      assertThat(goal).isNotNull();
      assertThat(goal.getGoalDescription()).isEqualTo("Complete user authentication feature");
      assertThat(goal.getRelatedItems()).isEqualTo("Login, Register, Forgot Password");
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("ゴール説明と関連アイテムが更新される")
    void shouldUpdateGoalDescriptionAndRelatedItems() {
      SprintGoal goal = createSprintGoal();

      goal.update("New goal description", "New related items");

      assertThat(goal.getGoalDescription()).isEqualTo("New goal description");
      assertThat(goal.getRelatedItems()).isEqualTo("New related items");
      assertThat(goal.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("goalDescriptionがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalDescriptionIsNull() {
      SprintGoal goal = createSprintGoal();

      assertThatThrownBy(() -> goal.update(null, "Related items"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goalDescription must not be null");
    }

    @Test
    @DisplayName("goalDescriptionが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalDescriptionIsEmpty() {
      SprintGoal goal = createSprintGoal();

      assertThatThrownBy(() -> goal.update("", "Related items"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goalDescription must not be empty");
    }

    @Test
    @DisplayName("goalDescriptionが空白文字みの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalDescriptionIsBlank() {
      SprintGoal goal = createSprintGoal();

      assertThatThrownBy(() -> goal.update("   ", "Related items"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goalDescription must not be empty");
    }

    @Test
    @DisplayName("goalDescriptionが1000文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalDescriptionExceedsLength() {
      SprintGoal goal = createSprintGoal();
      String longDescription = "A".repeat(1001);

      assertThatThrownBy(() -> goal.update(longDescription, "Related items"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goalDescription must not exceed 1000 characters");
    }
  }
}
