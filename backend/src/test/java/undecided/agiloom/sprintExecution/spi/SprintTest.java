package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Sprintのテスト")
class SprintTest {

  private Sprint createSprint() {
    return Sprint.of(
        SnowflakeId.of(1L),
        UUID.randomUUID(),
        1,
        "Sprint 1",
        "Complete user authentication",
        LocalDate.now(),
        LocalDate.now().plusWeeks(2),
        UUID.randomUUID(),
        "Team A",
        30,
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
    @DisplayName("ステータスがCOMPLETEDに更新される")
    void shouldUpdateStatusToCompleted() {
      Sprint sprint = createSprint();
      sprint.start();

      sprint.complete();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.COMPLETED);
    }
  }

  @Nested
  @DisplayName("cancelメソッドのテスト")
  class CancelMethodTest {

    @Test
    @DisplayName("ステータスがCANCELLEDに更新される")
    void shouldUpdateStatusToCancelled() {
      Sprint sprint = createSprint();

      sprint.cancel();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
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
    @DisplayName("PLANNED状態でcompleteすると例外がスローされる")
    void shouldThrowExceptionWhenCompleteFromPlanned() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.complete()).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("COMPLETED状態でstartすると例外がスローされる")
    void shouldThrowExceptionWhenStartFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.start()).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("COMPLETED状態でcancelすると例外がスローされる")
    void shouldThrowExceptionWhenCancelFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.cancel()).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("CANCELLED状態でstartすると例外がスローされる")
    void shouldThrowExceptionWhenStartFromCancelled() {
      Sprint sprint = createSprint();
      sprint.cancel();

      assertThatThrownBy(() -> sprint.start()).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("ACTIVE状態でstartすると例外がスローされる")
    void shouldThrowExceptionWhenStartFromActive() {
      Sprint sprint = createSprint();
      sprint.start();

      assertThatThrownBy(() -> sprint.start()).isInstanceOf(BusinessException.class);
    }
  }

  @Nested
  @DisplayName("updateメソッドの異常系テスト")
  class UpdateMethodNegativeTest {

    @Test
    @DisplayName("titleがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenTitleIsNull() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update(null, "Goal", null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("goalがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenGoalIsNull() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("Title", null, null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("titleが空文字の場合例外がスローされる")
    void shouldThrowExceptionWhenTitleIsEmpty() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("  ", "Goal", null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("goalが空文字の場合例外がスローされる")
    void shouldThrowExceptionWhenGoalIsEmpty() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("Title", "  ", null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("titleが100文字を超える場合例外がスローされる")
    void shouldThrowExceptionWhenTitleExceeds100Chars() {
      Sprint sprint = createSprint();
      String longTitle = "A".repeat(101);

      assertThatThrownBy(() -> sprint.update(longTitle, "Goal", null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("goalが1000文字を超える場合例外がスローされる")
    void shouldThrowExceptionWhenGoalExceeds1000Chars() {
      Sprint sprint = createSprint();
      String longGoal = "G".repeat(1001);

      assertThatThrownBy(() -> sprint.update("Title", longGoal, null, null))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("endDateがstartDateより前の場合例外がスローされる")
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {
      Sprint sprint = createSprint();
      LocalDate pastDate = LocalDate.now().minusDays(1);

      assertThatThrownBy(() -> sprint.update("Title", "Goal", pastDate, null))
          .isInstanceOf(BusinessException.class);
    }
  }

  @Nested
  @DisplayName("論理削除後の変更禁止テスト")
  class DeletedEntityChangeProhibitionTest {

    @Test
    @DisplayName("論理削除後にstartすると例外がスローされる")
    void shouldThrowExceptionWhenStartAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.start())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にcompleteすると例外がスローされる")
    void shouldThrowExceptionWhenCompleteAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にcancelすると例外がスローされる")
    void shouldThrowExceptionWhenCancelAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.cancel())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にupdateすると例外がスローされる")
    void shouldThrowExceptionWhenUpdateAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.update("New Title", "New Goal", null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }
  }
}
