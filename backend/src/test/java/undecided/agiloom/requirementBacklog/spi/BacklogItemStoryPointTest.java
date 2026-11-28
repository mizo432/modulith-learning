package undecided.agiloom.requirementBacklog.spi;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemのStoryPoint設定テスト")
class BacklogItemStoryPointTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);

  private BacklogItem item() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("StoryPoint値の保持テスト")
  class StoryPointValueTest {

    @Test
    @DisplayName("BacklogItemにStoryPoint 0を設定したとき、値が保持されること")
    void shouldKeepStoryPointZero() {
      BacklogItem backlogItem = item();
      backlogItem.setStoryPoint(0, LocalDateTime.now());
      assertThat(backlogItem.getStoryPoint()).isEqualTo(0);
    }

    @Test
    @DisplayName("BacklogItemにStoryPoint 89を設定したとき、値が保持されること")
    void shouldKeepStoryPoint89() {
      BacklogItem backlogItem = item();
      backlogItem.setStoryPoint(89, LocalDateTime.now());
      assertThat(backlogItem.getStoryPoint()).isEqualTo(89);
    }

    @Test
    @DisplayName("BacklogItemに無効なStoryPoint値を設定しようとすると、IllegalArgumentExceptionがスローされること")
    void shouldRejectInvalidStoryPoint() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.setStoryPoint(4, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(backlogItem.getStoryPoint()).isNull();
    }

    @Test
    @DisplayName("BacklogItemのStoryPointをnullから有効な値に変更したとき、値が更新されること")
    void shouldUpdateStoryPointFromNull() {
      BacklogItem backlogItem = item();
      assertThat(backlogItem.getStoryPoint()).isNull();
      backlogItem.setStoryPoint(13, LocalDateTime.now());
      assertThat(backlogItem.getStoryPoint()).isEqualTo(13);
    }
  }
}
