package undecided.agiloom.sprintExecution.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintFacadeImplのテスト")
class SprintFacadeImplTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(100L);

  private final SprintRepository sprintRepository = mock(SprintRepository.class);
  private final SprintFacadeImpl facade = new SprintFacadeImpl(sprintRepository);

  private Sprint sprint(int number, SprintStatus status) {
    return Sprint.builder()
        .id(SnowflakeId.of(1000L + number))
        .productId(PRODUCT_ID)
        .sprintNumber(number)
        .title("Sprint " + number)
        .status(status)
        .teamId(TEAM_ID)
        .build();
  }

  private Sprint sprintZero() {
    return Sprint.builder()
        .id(SnowflakeId.of(999L))
        .productId(PRODUCT_ID)
        .sprintNumber(0)
        .title("Sprint 0")
        .status(SprintStatus.PLANNED)
        .teamId(TEAM_ID)
        .isSprintZero(true)
        .build();
  }

  private void givenSprints(Sprint... sprints) {
    when(sprintRepository.findByTeamIdOrderBySprintNumberAsc(TEAM_ID))
        .thenReturn(List.of(sprints));
  }

  @Nested
  @DisplayName("findAssignableSprintsメソッドのテスト")
  class FindAssignableSprintsTest {

    @Test
    @DisplayName("進行中スプリントがあるとき、進行中と次の3つの計画中スプリントが返る")
    void returnsActiveAndNextThreePlanned() {
      givenSprints(
          sprint(1, SprintStatus.COMPLETED),
          sprint(2, SprintStatus.ACTIVE),
          sprint(3, SprintStatus.PLANNED),
          sprint(4, SprintStatus.PLANNED),
          sprint(5, SprintStatus.PLANNED),
          sprint(6, SprintStatus.PLANNED));

      List<Sprint> result = facade.findAssignableSprints(TEAM_ID);

      assertThat(result).extracting(Sprint::getSprintNumber).containsExactly(2, 3, 4, 5);
    }

    @Test
    @DisplayName("進行中スプリントがないとき、最も早い計画中スプリントから4つが返る")
    void returnsFirstFourPlannedWhenNoActive() {
      givenSprints(
          sprint(1, SprintStatus.PLANNED),
          sprint(2, SprintStatus.PLANNED),
          sprint(3, SprintStatus.PLANNED),
          sprint(4, SprintStatus.PLANNED),
          sprint(5, SprintStatus.PLANNED));

      List<Sprint> result = facade.findAssignableSprints(TEAM_ID);

      assertThat(result).extracting(Sprint::getSprintNumber).containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("スプリント0があるとき、割り当て可能一覧に含まれない")
    void excludesSprintZero() {
      givenSprints(
          sprintZero(), sprint(1, SprintStatus.PLANNED), sprint(2, SprintStatus.PLANNED));

      List<Sprint> result = facade.findAssignableSprints(TEAM_ID);

      assertThat(result).extracting(Sprint::getSprintNumber).containsExactly(1, 2);
    }

    @Test
    @DisplayName("完了・キャンセル済みスプリントがあるとき、割り当て可能一覧に含まれない")
    void excludesCompletedAndCancelled() {
      givenSprints(
          sprint(1, SprintStatus.COMPLETED),
          sprint(2, SprintStatus.CANCELLED),
          sprint(3, SprintStatus.PLANNED),
          sprint(4, SprintStatus.PLANNED));

      List<Sprint> result = facade.findAssignableSprints(TEAM_ID);

      assertThat(result).extracting(Sprint::getSprintNumber).containsExactly(3, 4);
    }

    @Test
    @DisplayName("割り当て可能なスプリントが4つ未満のとき、存在する分だけ返る")
    void returnsFewerWhenLessThanFourAvailable() {
      givenSprints(sprint(1, SprintStatus.ACTIVE), sprint(2, SprintStatus.PLANNED));

      List<Sprint> result = facade.findAssignableSprints(TEAM_ID);

      assertThat(result).extracting(Sprint::getSprintNumber).containsExactly(1, 2);
    }
  }

  @Nested
  @DisplayName("findNextSprintメソッドのテスト")
  class FindNextSprintTest {

    @Test
    @DisplayName("次スプリントを取得したとき、指定番号より大きい最小番号のスプリントが返る")
    void returnsSmallestNumberGreaterThanGiven() {
      givenSprints(
          sprint(1, SprintStatus.ACTIVE),
          sprint(2, SprintStatus.PLANNED),
          sprint(3, SprintStatus.PLANNED));

      assertThat(facade.findNextSprint(TEAM_ID, 1))
          .get()
          .extracting(Sprint::getSprintNumber)
          .isEqualTo(2);
    }

    @Test
    @DisplayName("次スプリントがないとき、空が返る")
    void returnsEmptyWhenNoNextSprint() {
      givenSprints(sprint(1, SprintStatus.ACTIVE));

      assertThat(facade.findNextSprint(TEAM_ID, 1)).isEmpty();
    }

    @Test
    @DisplayName("スプリント0の次を取得したとき、スプリント0自体は返らない")
    void doesNotReturnSprintZero() {
      givenSprints(sprintZero(), sprint(1, SprintStatus.PLANNED));

      assertThat(facade.findNextSprint(TEAM_ID, 0))
          .get()
          .extracting(Sprint::getSprintNumber)
          .isEqualTo(1);
    }
  }
}
