package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemのREADY状態遷移テスト")
class BacklogItemReadyTransitionTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);

  private BacklogItem acceptedItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .build();
  }

  private BacklogItem acceptedItemWithTimestamp() {
    LocalDateTime before = LocalDateTime.now().minusMinutes(1);
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .createdAt(before)
        .updatedAt(before)
        .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
    return item;
  }

  @Nested
  @DisplayName("ACCEPTED→READY遷移のテスト")
  class AcceptedToReadyTest {

    @Test
    @DisplayName("ACCEPTED状態からREADYへ遷移できること")
    void shouldTransitionFromAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());

      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
    }
  }

  @Nested
  @DisplayName("READY→SUGGESTED遷移のテスト")
  class ReadyToSuggestedTest {

    @Test
    @DisplayName("READY状態からSUGGESTEDへ遷移できること")
    void shouldTransitionFromReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());

      item.changeStatus(BacklogItemStatus.SUGGESTED, LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }
  }

  @Nested
  @DisplayName("無効な遷移のテスト")
  class InvalidTransitionTest {

    @Test
    @DisplayName("SUGGESTED状態からREADYへ直接遷移できないこと")
    void shouldNotTransitionFromSuggestedToReady() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("READY状態からFROZENへ遷移できないこと")
    void shouldNotTransitionFromReadyToFrozen() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("FROZEN状態からREADYへ遷移できないこと")
    void shouldNotTransitionFromFrozenToReady() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("ACCEPTED状態からSUGGESTEDへ遷移できないこと")
    void shouldNotTransitionFromAcceptedToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.SUGGESTED, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("READY状態からREADYへ遷移できないこと")
    void shouldNotTransitionFromReadyToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  @DisplayName("既存遷移の回帰テスト")
  class RegressionTest {

    @Test
    @DisplayName("既存遷移SUGGESTED→ACCEPTED（Valueあり）が引き続き機能すること")
    void shouldSuggestedToAcceptedStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();

      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }

    @Test
    @DisplayName("既存遷移SUGGESTED→FROZENが引き続き機能すること")
    void shouldSuggestedToFrozenStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();

      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.FROZEN);
    }

    @Test
    @DisplayName("既存遷移FROZEN→SUGGESTEDが引き続き機能すること")
    void shouldFrozenToSuggestedStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());

      item.changeStatus(BacklogItemStatus.SUGGESTED, LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }
  }

  @Nested
  @DisplayName("updatedAt更新のテスト")
  class UpdatedAtTest {

    @Test
    @DisplayName("changeStatus()後にupdatedAtが更新されること（READY遷移時）")
    void shouldUpdateUpdatedAtOnReadyTransition() {
      LocalDateTime before = LocalDateTime.now().minusMinutes(5);
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .createdAt(before)
          .updatedAt(before)
          .build();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());

      try {
        Thread.sleep(10); // updatedAtの更新が検出できるように待機
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());

      assertThat(item.getUpdatedAt()).isAfter(before);
    }
  }
}
