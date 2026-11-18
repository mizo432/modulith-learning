package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemのESTIMATED状態遷移テスト")
class BacklogItemEstimatedTransitionTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);

  private BacklogItem readyItemWithStoryPoint(Integer storyPoint) {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .storyPoint(storyPoint)
        .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED);
    item.changeStatus(BacklogItemStatus.READY);
    return item;
  }

  private BacklogItem readyItemWithoutStoryPoint() {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED);
    item.changeStatus(BacklogItemStatus.READY);
    return item;
  }

  @Nested
  @DisplayName("READY → ESTIMATED 遷移のテスト")
  class ReadyToEstimatedTest {

    @Test
    @DisplayName("READY状態のBacklogItemに有効なStoryPointを設定しESTIMATEDへ遷移したとき、状態がESTIMATEDになること")
    void shouldTransitionReadyToEstimatedWithValidStoryPoint() {
      BacklogItem item = readyItemWithStoryPoint(8);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
    }

    @Test
    @DisplayName("READY状態のBacklogItemでStoryPointが未設定のときESTIMATEDへ遷移すると、IllegalArgumentExceptionがスローされること")
    void shouldRejectReadyToEstimatedWithoutStoryPoint() {
      BacklogItem item = readyItemWithoutStoryPoint();
      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.ESTIMATED))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("StoryPoint must be set");
    }

    @Test
    @DisplayName("READY状態のBacklogItemに無効なStoryPointを設定しESTIMATEDへ遷移したとき、IllegalArgumentExceptionがスローされること")
    void shouldRejectReadyToEstimatedWithInvalidStoryPoint() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .storyPoint(4) // 無効な値
          .build();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.ESTIMATED))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("ESTIMATED → READY 遷移のテスト")
  class EstimatedToReadyTest {

    @Test
    @DisplayName("ESTIMATED状態のBacklogItemをREADYへ戻したとき、状態がREADYになりStoryPointが保持されること")
    void shouldTransitionEstimatedToReadyAndKeepStoryPoint() {
      BacklogItem item = readyItemWithStoryPoint(13);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
      assertThat(item.getStoryPoint()).isEqualTo(13);
      item.changeStatus(BacklogItemStatus.READY);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      assertThat(item.getStoryPoint()).isEqualTo(13);
    }
  }

  @Nested
  @DisplayName("ESTIMATED状態での再評価テスト")
  class ReEstimateTest {

    @Test
    @DisplayName("ESTIMATED状態のBacklogItemのStoryPointを変更したとき、新しい値が設定されること（再評価許可）")
    void shouldUpdateStoryPointInEstimatedState() {
      BacklogItem item = readyItemWithStoryPoint(5);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
      item.setStoryPoint(21);
      assertThat(item.getStoryPoint()).isEqualTo(21);
    }
  }

  @Nested
  @DisplayName("ESTIMATED状態からの無効な遷移テスト")
  class InvalidTransitionFromEstimatedTest {

    @Test
    @DisplayName("ESTIMATED状態のBacklogItemをACCEPTEDへ遷移しようとすると、IllegalStateExceptionがスローされること")
    void shouldRejectEstimatedToAccepted() {
      BacklogItem item = readyItemWithStoryPoint(8);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("ESTIMATED状態のBacklogItemをSUGGESTEDへ遷移しようとすると、IllegalStateExceptionがスローされること")
    void shouldRejectEstimatedToSuggested() {
      BacklogItem item = readyItemWithStoryPoint(8);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.SUGGESTED))
          .isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  @DisplayName("READY状態からの無効な遷移テスト")
  class InvalidTransitionFromReadyTest {

    @Test
    @DisplayName("READY状態のBacklogItemをESTIMATED以外へ遷移しようとすると、IllegalStateExceptionがスローされること")
    void shouldRejectReadyToAccepted() {
      BacklogItem item = readyItemWithStoryPoint(8);
      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalStateException.class);
    }
  }
}
