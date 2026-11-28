package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemの分割専用DONE状態遷移テスト")
class BacklogItemSplitTransitionTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final LocalDateTime BEFORE = LocalDateTime.of(2025, 1, 1, 0, 0);

  private BacklogItem itemWithStatus(BacklogItemStatus status) {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(status)
        .storyPoint(13)
        .createdAt(BEFORE)
        .updatedAt(BEFORE)
        .build();
  }

  @Nested
  @DisplayName("分割専用のDONE遷移")
  class CompleteForSplitTest {

    @ParameterizedTest
    @EnumSource(value = BacklogItemStatus.class, names = "DONE", mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("DONE以外の状態から分割専用操作でDONEへ遷移し更新日時が更新されること")
    void shouldCompleteForSplitFromNonDoneStatus(BacklogItemStatus status) {
      BacklogItem item = itemWithStatus(status);

      item.completeForSplit(LocalDateTime.now());

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.DONE);
      assertThat(item.getStoryPoint()).isEqualTo(13);
      assertThat(item.getUpdatedAt()).isAfter(BEFORE);
    }

    @Test
    @DisplayName("DONE状態のアイテムは分割専用操作を再実行できないこと")
    void shouldRejectCompletingForSplitWhenAlreadyDone() {
      BacklogItem item = itemWithStatus(BacklogItemStatus.DONE);

      assertThatThrownBy(() -> item.completeForSplit(LocalDateTime.now())).isInstanceOf(IllegalStateException.class);

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.DONE);
      assertThat(item.getUpdatedAt()).isEqualTo(BEFORE);
    }
  }

  @Nested
  @DisplayName("通常の状態遷移")
  class ChangeStatusTest {

    @ParameterizedTest
    @EnumSource(BacklogItemStatus.class)
    @DisplayName("通常の状態変更ではどの状態からもDONEへ遷移できないこと")
    void shouldRejectNormalTransitionToDone(BacklogItemStatus status) {
      BacklogItem item = itemWithStatus(status);

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.DONE, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class);

      assertThat(item.getStatus()).isEqualTo(status);
      assertThat(item.getUpdatedAt()).isEqualTo(BEFORE);
    }

    @ParameterizedTest
    @EnumSource(BacklogItemStatus.class)
    @DisplayName("DONE状態から通常の状態変更はできないこと")
    void shouldRejectNormalTransitionFromDone(BacklogItemStatus target) {
      BacklogItem item = itemWithStatus(BacklogItemStatus.DONE);

      assertThatThrownBy(() -> item.changeStatus(target, LocalDateTime.now())).isInstanceOf(IllegalStateException.class);

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.DONE);
      assertThat(item.getUpdatedAt()).isEqualTo(BEFORE);
    }
  }
}