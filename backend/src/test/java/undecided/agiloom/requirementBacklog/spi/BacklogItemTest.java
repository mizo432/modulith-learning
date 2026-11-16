package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemの単体テスト")
class BacklogItemTest {

  private BacklogItem item() {
    return BacklogItem.builder()
        .id(SnowflakeId.of(100L))
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
  }

  @Nested
  @DisplayName("初期状態のテスト")
  class InitialStatusTest {

    @Test
    @DisplayName("新規作成時の状態はSUGGESTEDであること")
    void shouldDefaultToSuggested() {
      assertThat(item().getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }
  }

  @Nested
  @DisplayName("changeStatus メソッドのテスト")
  class ChangeStatusTest {

    @Test
    @DisplayName("SUGGESTEDからACCEPTEDへ遷移できること")
    void shouldAcceptFromSuggested() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }

    @Test
    @DisplayName("SUGGESTEDからREJECTEDへ遷移できること")
    void shouldRejectFromSuggested() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.REJECTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.REJECTED);
    }

    @Test
    @DisplayName("ACCEPTEDからは遷移できないこと")
    void shouldNotTransitionFromAccepted() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.REJECTED))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Invalid status transition");
    }

    @Test
    @DisplayName("REJECTEDからは遷移できないこと")
    void shouldNotTransitionFromRejected() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.REJECTED);
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("SUGGESTEDからSUGGESTEDへの遷移はできないこと")
    void shouldNotTransitionToSuggested() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.SUGGESTED))
          .isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  @DisplayName("update メソッドのテスト")
  class UpdateTest {

    @Test
    @DisplayName("タイトル等を更新できること（nullは変更なし）")
    void shouldUpdateFields() {
      BacklogItem backlogItem = item();
      backlogItem.update("New Title", null, 5, null);
      assertThat(backlogItem.getTitle()).isEqualTo("New Title");
      assertThat(backlogItem.getPriority()).isEqualTo(5);
    }

    @Test
    @DisplayName("空タイトルへの更新は例外になること")
    void shouldThrowOnEmptyTitle() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.update("", null, null, null))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
