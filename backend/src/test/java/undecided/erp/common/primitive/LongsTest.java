package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Test class for the {@link Longs#encodeToBase64(Long)} method. */
class LongsTest {

  @Nested
  @DisplayName("encodeToBase64メソッドのテスト")
  class EncodeToBase64Test {

    @Test
    @DisplayName("正のLong値をエンコードしたBase64形式の文字列を返す")
    void shouldReturnBase64EncodedStringForPositiveLong() {
      Long input = 123456789L;
      String result = Longs.encodeToBase64(input);
      assertThat(result).isEqualTo("AAAAAAdbzRU=");
    }

    @Test
    @DisplayName("負のLong値をエンコードしたBase64形式の文字列を返す")
    void shouldReturnBase64EncodedStringForNegativeLong() {
      Long input = -123456789L;
      String result = Longs.encodeToBase64(input);
      assertThat(result).isEqualTo("______ikMus=");
    }

    @Test
    @DisplayName("0をエンコードしたBase64形式の文字列を返す")
    void shouldReturnBase64EncodedStringForZero() {
      Long input = 0L;
      String result = Longs.encodeToBase64(input);
      assertThat(result).isEqualTo("AAAAAAAAAAA=");
    }

    @Test
    @DisplayName("Long値がnullの場合に例外をスローする")
    void shouldThrowExceptionWhenLongValueIsNull() {
      Long input = null;
      assertThatThrownBy(() -> Longs.encodeToBase64(input))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("toByteArrayメソッドのテスト")
  class ToByteArrayTest {

    @Test
    @DisplayName("正のLong値をbyte配列に変換する")
    void shouldConvertPositiveLongToByteArray() {
      Long input = 123456789L;
      byte[] result = Longs.toByteArray(input);
      assertThat(result).containsExactly(0, 0, 0, 0, 7, 91, -51, 21);
    }

    @Test
    @DisplayName("負のLong値をbyte配列に変換する")
    void shouldConvertNegativeLongToByteArray() {
      Long input = -123456789L;
      byte[] result = Longs.toByteArray(input);
      assertThat(result).containsExactly(-1, -1, -1, -1, -8, -92, 50, -21);
    }

    @Test
    @DisplayName("0をbyte配列に変換する")
    void shouldConvertZeroToByteArray() {
      Long input = 0L;
      byte[] result = Longs.toByteArray(input);
      assertThat(result).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    @DisplayName("Long値がnullの場合に例外をスローする")
    void shouldThrowExceptionWhenLongValueIsNull() {
      Long input = null;
      assertThatThrownBy(() -> Longs.toByteArray(input)).isInstanceOf(NullPointerException.class);
    }
  }
}
