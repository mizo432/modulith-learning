package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;

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
}
