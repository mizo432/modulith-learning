package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("StoryPoint値オブジェクトのテスト")
class StoryPointTest {

  @Nested
  @DisplayName("有効なStoryPoint値のテスト")
  class ValidValuesTest {

    @Test
    @DisplayName("StoryPoint 0を設定したとき、有効な値として受け入れられること")
    void shouldAcceptZero() {
      StoryPoint sp = StoryPoint.of(0);
      assertThat(sp.getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("StoryPoint 1を設定したとき、有効な値として受け入れられること")
    void shouldAcceptOne() {
      StoryPoint sp = StoryPoint.of(1);
      assertThat(sp.getValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("StoryPoint 89を設定したとき、有効な値として受け入れられること")
    void shouldAccept89() {
      StoryPoint sp = StoryPoint.of(89);
      assertThat(sp.getValue()).isEqualTo(89);
    }
  }

  @Nested
  @DisplayName("無効なStoryPoint値のテスト")
  class InvalidValuesTest {

    @Test
    @DisplayName("StoryPoint 4を設定したとき（フィボナッチ数列にない）、IllegalArgumentExceptionがスローされること")
    void shouldReject4() {
      assertThatThrownBy(() -> StoryPoint.of(4))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("StoryPoint -1を設定したとき、IllegalArgumentExceptionがスローされること")
    void shouldRejectNegative() {
      assertThatThrownBy(() -> StoryPoint.of(-1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("StoryPoint 90を設定したとき、IllegalArgumentExceptionがスローされること")
    void shouldReject90() {
      assertThatThrownBy(() -> StoryPoint.of(90))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("許容値一覧のテスト")
  class AllowedValuesTest {

    @Test
    @DisplayName("StoryPointの許容値一覧を取得したとき、フィボナッチ数列の値のみが返ること")
    void shouldReturnAllowedValues() {
      List<Integer> allowed = StoryPoint.allowedValues();
      assertThat(allowed).containsExactly(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
  }
}
