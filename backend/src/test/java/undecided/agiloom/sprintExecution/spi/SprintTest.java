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
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Sprintのテスト")
class SprintTest {

  private static final UUID FIXED_PRODUCT_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  private Sprint createSprint() {
    return Sprint.of(
        SnowflakeId.of(1L),
        FIXED_PRODUCT_ID,
        1,
        "Sprint 1",
        "Complete user authentication",
        LocalDate.now(),
        LocalDate.now().plusWeeks(2),
        FIXED_TEAM_ID,
        "Team A",
        30,
        null,
        LocalDateTime.now(),
        LocalDateTime.now());
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
    @DisplayName("ACTIVE状態からCOMPLETEDに更新される")
    void shouldUpdateStatusToCompleted() {
      Sprint sprint = createSprint();
      sprint.start();

      sprint.complete();

      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.COMPLETED);
    }

    @Test
    @DisplayName("PLANNED状態からcompleteするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenCompleteFromPlanned() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(BusinessException.class)
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
    @DisplayName("COMPLETED状態からcancelするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenCancelFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.cancel())
          .isInstanceOf(BusinessException.class)
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
    @DisplayName("タイトルがnullの場合、既存値が使用される")
    void shouldUseExistingTitleWhenNull() {
      Sprint sprint = createSprint();
      String originalTitle = sprint.getTitle();

      sprint.update(null, "New Goal", null, null);

      assertThat(sprint.getTitle()).isEqualTo(originalTitle);
      assertThat(sprint.getGoal()).isEqualTo("New Goal");
    }

    @Test
    @DisplayName("ゴールがnullの場合、既存値が使用される")
    void shouldUseExistingGoalWhenNull() {
      Sprint sprint = createSprint();
      String originalGoal = sprint.getGoal();

      sprint.update("New Title", null, null, null);

      assertThat(sprint.getTitle()).isEqualTo("New Title");
      assertThat(sprint.getGoal()).isEqualTo(originalGoal);
    }

    @Test
    @DisplayName("タイトルが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsEmpty() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("  ", "Goal", null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be empty");
    }

    @Test
    @DisplayName("ゴールが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalIsEmpty() {
      Sprint sprint = createSprint();

      assertThatThrownBy(() -> sprint.update("Title", "  ", null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goal must not be empty");
    }

    @Test
    @DisplayName("タイトルが100文字の場合、更新される")
    void shouldUpdateTitleWhen100Characters() {
      Sprint sprint = createSprint();
      String title100 = "a".repeat(100);

      sprint.update(title100, "Goal", null, null);

      assertThat(sprint.getTitle()).isEqualTo(title100);
    }

    @Test
    @DisplayName("タイトルが101文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleExceeds100Characters() {
      Sprint sprint = createSprint();
      String title101 = "a".repeat(101);

      assertThatThrownBy(() -> sprint.update(title101, "Goal", null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not exceed 100 characters");
    }

    @Test
    @DisplayName("ゴールが1000文字の場合、更新される")
    void shouldUpdateGoalWhen1000Characters() {
      Sprint sprint = createSprint();
      String goal1000 = "b".repeat(1000);

      sprint.update("Title", goal1000, null, null);

      assertThat(sprint.getGoal()).isEqualTo(goal1000);
    }

    @Test
    @DisplayName("ゴールが1001文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenGoalExceeds1000Characters() {
      Sprint sprint = createSprint();
      String goal1001 = "b".repeat(1001);

      assertThatThrownBy(() -> sprint.update("Title", goal1001, null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("goal must not exceed 1000 characters");
    }

    @Test
    @DisplayName("endDateがstartDateより前の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
      Sprint sprint = createSprint();
      LocalDate pastEndDate = LocalDate.now().minusDays(1);

      assertThatThrownBy(() -> sprint.update("Title", "Goal", pastEndDate, "Notes"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("endDate must be after startDate");
    }

    @Test
    @DisplayName("endDateがstartDateと等しい場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenEndDateEqualsStartDate() {
      Sprint sprint = createSprint();
      LocalDate startDate = sprint.getStartDate();

      assertThatThrownBy(() -> sprint.update("Title", "Goal", startDate, "Notes"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("endDate must be after startDate");
    }
  }

  @Nested
  @DisplayName("soft-delete関連のテスト")
  class SoftDeleteTest {

    @Test
    @DisplayName("setDeletedAtでdeletedAtが設定される")
    void shouldSetDeletedAt() {
      Sprint sprint = createSprint();
      LocalDateTime deletedAt = LocalDateTime.now();

      sprint.setDeletedAt(deletedAt);

      assertThat(sprint.getDeletedAt()).isEqualTo(deletedAt);
    }

    @Test
    @DisplayName("soft-deleteされたSprintでstartするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStartAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.start())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint is already deleted");
    }

    @Test
    @DisplayName("soft-deleteされたSprintでcompleteするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenCompleteAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint is already deleted");
    }

    @Test
    @DisplayName("soft-deleteされたSprintでcancelするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenCancelAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.cancel())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint is already deleted");
    }

    @Test
    @DisplayName("soft-deleteされたSprintでupdateするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenUpdateAfterDeleted() {
      Sprint sprint = createSprint();
      sprint.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> sprint.update("New Title", "New Goal", null, null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint is already deleted");
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
    @DisplayName("COMPLETED状態からstartするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStartFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.start())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("COMPLETED");
    }

    @Test
    @DisplayName("COMPLETED状態からcompleteするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenCompleteFromCompleted() {
      Sprint sprint = createSprint();
      sprint.start();
      sprint.complete();

      assertThatThrownBy(() -> sprint.complete())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("COMPLETED");
    }
  }
}
