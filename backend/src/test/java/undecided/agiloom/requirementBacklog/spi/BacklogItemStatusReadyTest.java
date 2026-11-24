package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("BacklogItemStatusのREADY状態テスト")
class BacklogItemStatusReadyTest {

  @Nested
  @DisplayName("READY定数のテスト")
  class ReadyConstantTest {

    @Test
    @DisplayName("READY定数が存在し、ラベルが「準備完了」であること")
    void shouldReadyExistWithLabel() {
      assertThat(BacklogItemStatus.READY).isNotNull();
      assertThat(BacklogItemStatus.READY.label()).isEqualTo("準備完了");
    }

    @Test
    @DisplayName("READYのlabel()が「準備完了」を返すこと")
    void shouldReadyLabelReturnJapaneseText() {
      assertThat(BacklogItemStatus.READY.label()).isEqualTo("準備完了");
    }
  }

  @Nested
  @DisplayName("READY→ACCEPTED逆遷移のテスト")
  class ReadyToAcceptedTest {

    private BacklogItem readyItem() {
      BacklogItem item =
          BacklogItem.builder().title("Test Item").creatorName("Creator").value(50).build();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      return item;
    }

    @Test
    @DisplayName("Readyでストーリーポイントが設定されているアイテムをAcceptedへ遷移させたとき、状態がAcceptedとなりストーリーポイントがnullとなる")
    void shouldClearStoryPointWhenReadyToAccepted() {
      BacklogItem item = readyItem();
      item.setStoryPoint(5);

      item.changeStatus(BacklogItemStatus.ACCEPTED);

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      assertThat(item.getStoryPoint()).isNull();
    }

    @Test
    @DisplayName("Readyでストーリーポイントが未設定のアイテムをAcceptedへ遷移させたとき、状態がAcceptedとなりストーリーポイントはnullのままとなる")
    void shouldKeepStoryPointNullWhenReadyToAcceptedWithoutStoryPoint() {
      BacklogItem item = readyItem();

      item.changeStatus(BacklogItemStatus.ACCEPTED);

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      assertThat(item.getStoryPoint()).isNull();
    }

    @Test
    @DisplayName("ReadyのアイテムをAcceptedへ遷移させたとき、Valueは変更されない")
    void shouldKeepValueWhenReadyToAccepted() {
      BacklogItem item = readyItem();

      item.changeStatus(BacklogItemStatus.ACCEPTED);

      assertThat(item.getValue()).isEqualTo(50);
    }

    @Test
    @DisplayName("ESTIMATEDのアイテムをAcceptedへ遷移させたとき、IllegalStateExceptionとなる")
    void shouldThrowWhenEstimatedToAccepted() {
      BacklogItem item = readyItem();
      item.setStoryPoint(5);
      item.changeStatus(BacklogItemStatus.ESTIMATED);

      assertThatThrownBy(() -> item.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalStateException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
    }
  }
}
