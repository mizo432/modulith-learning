package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Sprintのテスト")
class SprintTest {

  private Sprint createSprint() {
    return Sprint.builder()
        .id(SnowflakeId.of(1L))
        .productId(SnowflakeId.of(100L))
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Complete user authentication")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .teamId(SnowflakeId.of(200L))
        .teamName("Team A")
        .velocity(30)
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
      assertThat(sprint.getUpdatedAt()).isNotNull();
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
      assertThat(sprint.getUpdatedAt()).isNotNull();
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
      assertThat(sprint.getUpdatedAt()).isNotNull();
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
  }

  @Nested
  @DisplayName("スプリント0のテスト")
  class SprintZeroTest {

    @Test
    @DisplayName("createSprintZeroファクトリメソッドでスプリント0が作成される")
    void shouldCreateSprintZero() {
      SnowflakeId productId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(200L);
      String teamName = "Team A";

      Sprint sprintZero = Sprint.createSprintZero(productId, teamId, teamName);

      assertThat(sprintZero.isSprintZero()).isTrue();
      assertThat(sprintZero.getSprintNumber()).isEqualTo(0);
      assertThat(sprintZero.getTitle()).isEqualTo("Sprint 0");
      assertThat(sprintZero.getGoal()).isEqualTo("チームの初期準備期間");
      assertThat(sprintZero.getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(sprintZero.getStartDate()).isNull();
      assertThat(sprintZero.getEndDate()).isNull();
      assertThat(sprintZero.getTeamId()).isEqualTo(teamId);
      assertThat(sprintZero.getTeamName()).isEqualTo(teamName);
      assertThat(sprintZero.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("スプリント0は削除できない")
    void shouldNotDeleteSprintZero() {
      Sprint sprintZero = Sprint.createSprintZero(SnowflakeId.of(100L), SnowflakeId.of(200L), "Team A");

      org.junit.jupiter.api.Assertions.assertThrows(
          IllegalStateException.class,
          sprintZero::delete,
          "Sprint 0 cannot be deleted"
      );
    }

    @Test
    @DisplayName("スプリント0のisDeletableはfalseを返す")
    void shouldReturnFalseForIsDeletable() {
      Sprint sprintZero = Sprint.createSprintZero(SnowflakeId.of(100L), SnowflakeId.of(200L), "Team A");

      assertThat(sprintZero.isDeletable()).isFalse();
    }

    @Test
    @DisplayName("通常のスプリントは削除できる")
    void shouldDeleteNormalSprint() {
      Sprint sprint = createSprint();

      sprint.delete();

      assertThat(sprint.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("通常のスプリントのisDeletableはtrueを返す")
    void shouldReturnTrueForIsDeletable() {
      Sprint sprint = createSprint();

      assertThat(sprint.isDeletable()).isTrue();
    }

    @Test
    @DisplayName("スプリント0の名前変更は禁止される")
    void shouldNotChangeSprintZeroTitle() {
      Sprint sprintZero = Sprint.createSprintZero(SnowflakeId.of(100L), SnowflakeId.of(200L), "Team A");
      String originalTitle = sprintZero.getTitle();

      sprintZero.update("New Title", "New Goal", null, "Notes");

      assertThat(sprintZero.getTitle()).isEqualTo(originalTitle);
    }

    @Test
    @DisplayName("スプリント0の日付更新は許可される")
    void shouldUpdateSprintZeroDates() {
      Sprint sprintZero = Sprint.createSprintZero(SnowflakeId.of(100L), SnowflakeId.of(200L), "Team A");
      LocalDate startDate = LocalDate.now();
      LocalDate endDate = LocalDate.now().plusWeeks(2);

      sprintZero.updateDates(startDate, endDate);

      assertThat(sprintZero.getStartDate()).isEqualTo(startDate);
      assertThat(sprintZero.getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("markAsSprintZeroでスプリント0としてマークされる")
    void shouldMarkAsSprintZero() {
      Sprint sprint = createSprint();

      sprint.markAsSprintZero();

      assertThat(sprint.isSprintZero()).isTrue();
      assertThat(sprint.getSprintNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("createSprintZeroのproductIdがnullの場合、例外がスローされる")
    void shouldThrowWhenProductIdIsNull() {
      org.junit.jupiter.api.Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> Sprint.createSprintZero(null, SnowflakeId.of(200L), "Team A")
      );
    }

    @Test
    @DisplayName("createSprintZeroのteamIdがnullの場合、例外がスローされる")
    void shouldThrowWhenTeamIdIsNull() {
      org.junit.jupiter.api.Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> Sprint.createSprintZero(SnowflakeId.of(100L), null, "Team A")
      );
    }

    @Test
    @DisplayName("createSprintZeroのteamNameがnullの場合、例外がスローされる")
    void shouldThrowWhenTeamNameIsNull() {
      org.junit.jupiter.api.Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> Sprint.createSprintZero(SnowflakeId.of(100L), SnowflakeId.of(200L), null)
      );
    }
  }
}
