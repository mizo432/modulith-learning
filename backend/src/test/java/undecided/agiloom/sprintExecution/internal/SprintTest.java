package undecided.agiloom.sprintExecution.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Sprintのテスト")
class SprintTest {

  private Sprint createSprint() {
    return Sprint.builder()
        .id(SnowflakeId.of(1L))
        .productId(UUID.randomUUID())
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Complete user authentication")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .teamId(UUID.randomUUID())
        .teamName("Team A")
        .velocity(30)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      Sprint sprint = createSprint();

      assertThat(sprint).isNotNull();
      assertThat(sprint.getTitle()).isEqualTo("Sprint 1");
      assertThat(sprint.getSprintNumber()).isEqualTo(1);
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(sprint.getVelocity()).isEqualTo(30);
    }

    @Test
    @DisplayName("デフォルトステータスはPLANNEDである")
    void shouldHaveDefaultStatusPlanned() {
      Sprint sprint = createSprint();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.PLANNED);
    }
  }

  @Nested
  @DisplayName("startメソッドのテスト")
  class StartMethodTest {

    @Test
    @DisplayName("ステータスがACTIVEに更新される")
    void shouldUpdateStatusToActive() {
      Sprint sprint = createSprint();

      sprint.start();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }
  }

  @Nested
  @DisplayName("completeメソッドのテスト")
  class CompleteMethodTest {

    @Test
    @DisplayName("ACTIVE状態からCOMPLETEDに更新される")
    void shouldUpdateStatusToCompleted() {
      Sprint sprint = createSprint();
      sprint.start();

      sprint.complete();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.COMPLETED);
    }

    @Test
    @DisplayName("PLANNED状態からcompleteするとIllegalStateExceptionがスローされる")
    void shouldThrowExceptionWhenCompleteFromPlanned() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("PLANNED");
    }
  }

  @Nested
  @DisplayName("cancelメソッドのテスト")
  class CancelMethodTest {

    @Test
    @DisplayName("PLANNED状態からCANCELLEDに更新される")
    void shouldUpdateStatusToCancelled() {
      Sprint sprint = createSprint();

      sprint.cancel();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
      assertThat(sprint.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("COMPLETED状態からcancelするとIllegalStateExceptionがスローされる")
    void shouldThrowExceptionWhenCancelFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.cancel())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("COMPLETED");
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("タイトルとゴールが更新される")
    void shouldUpdateTitleAndGoal() {
      Sprint sprint = createSprint();

      sprint.update("New Title", "New Goal", null, "New Notes");

      assertThat(sprint.getTitle()).isEqualTo("New Title");
      assertThat(sprint.getGoal()).isEqualTo("New Goal");
      assertThat(sprint.getNotes()).isEqualTo("New Notes");
      assertThat(sprint.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("終了日が更新される")
    void shouldUpdateEndDate() {
      Sprint sprint = createSprint();
      LocalDate newEndDate = LocalDate.now().plusWeeks(4);

      sprint.update("Title", "Goal", newEndDate, "Notes");

      assertThat(sprint.getEndDate()).isEqualTo(newEndDate);
    }

    @Test
    @DisplayName("終了日がnullの場合は更新されない")
    void shouldNotUpdateEndDateWhenNull() {
      Sprint sprint = createSprint();
      LocalDate originalEndDate = sprint.getEndDate();

      sprint.update("Title", "Goal", null, "Notes");

      assertThat(sprint.getEndDate()).isEqualTo(originalEndDate);
    }

    @Test
    @DisplayName("タイトルがnullの場合、IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsNull() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update(null, "Goal", null, "Notes"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("title must not be null");
    }

    @Test
    @DisplayName("ゴールがnullの場合、IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenGoalIsNull() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("Title", null, null, "Notes"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("goal must not be null");
    }
  }

  @Nested
  @DisplayName("ステータス遷移のテスト")
  class StatusTransitionTest {

    @Test
    @DisplayName("PLANNEDからACTIVEへ遷移できる")
    void shouldTransitionFromPlannedToActive() {
      Sprint sprint = createSprint();
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.PLANNED);

      sprint.start();
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    @DisplayName("ACTIVEからCOMPLETEDへ遷移できる")
    void shouldTransitionFromActiveToCompleted() {
      Sprint sprint = createSprint();
      sprint.start();

      sprint.complete();
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.COMPLETED);
    }

    @Test
    @DisplayName("ACTIVEからCANCELLEDへ遷移できる")
    void shouldTransitionFromActiveToCancelled() {
      Sprint sprint = createSprint();
      sprint.start();

      sprint.cancel();
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
    }

    @Test
    @DisplayName("PLANNEDからCANCELLEDへ遷移できる")
    void shouldTransitionFromPlannedToCancelled() {
      Sprint sprint = createSprint();

      sprint.cancel();
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
    }

    @Test
    @DisplayName("COMPLETED状態からstartするとIllegalStateExceptionがスローされる")
    void shouldThrowExceptionWhenStartFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.start())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("COMPLETED");
    }

    @Test
    @DisplayName("COMPLETED状態からcompleteするとIllegalStateExceptionがスローされる")
    void shouldThrowExceptionWhenCompleteFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("COMPLETED");
    }
  }
}
