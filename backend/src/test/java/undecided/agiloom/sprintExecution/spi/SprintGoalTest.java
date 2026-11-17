package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

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
  }
}
