package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** LongsTest クラスのテストケースを定義したクラスです。 Long 型の値に対するエンコードや変換に関連するユーティリティメソッドの動作を検証します。 */
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

  @Nested
  @DisplayName("decodeFromBase64メソッドのテスト")
  class DecodeFromBase64Test {

    @Test
    @DisplayName("有効な正のBase64形式文字列から対応するLong値を返す")
    void shouldReturnCorrectPositiveLongForBase64() {
      String input = "AAAAAAdbzRU=";
      Long result = Longs.decodeFromBase64(input);
      assertThat(result).isEqualTo(123456789L);
    }

    @Test
    @DisplayName("有効な負のBase64形式文字列から対応するLong値を返す")
    void shouldReturnCorrectNegativeLongForBase64() {
      String input = "______ikMus=";
      Long result = Longs.decodeFromBase64(input);
      assertThat(result).isEqualTo(-123456789L);
    }

    @Test
    @DisplayName("0に対応するBase64形式文字列からLong値0を返す")
    void shouldReturnZeroForBase64String() {
      String input = "AAAAAAAAAAA=";
      Long result = Longs.decodeFromBase64(input);
      assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("Base64形式文字列がnullの場合、例外をスローする")
    void shouldThrowExceptionWhenBase64StringIsNull() {
      String input = null;
      assertThatThrownBy(() -> Longs.decodeFromBase64(input))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("無効なBase64形式文字列の場合、例外をスローする")
    void shouldThrowExceptionForInvalidBase64String() {
      String input = "InvalidBase64";
      assertThatThrownBy(() -> Longs.decodeFromBase64(input))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Illegal base64 character");
    }

    @Test
    @DisplayName("空のBase64文字列の場合、例外をスローする")
    void shouldThrowExceptionForEmptyBase64String() {
      String input = "";
      assertThatThrownBy(() -> Longs.decodeFromBase64(input))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("デコードされたバイト配列の長さが不正です");
    }

    @Test
    @DisplayName("Base64文字列の長さが不正の場合、例外をスローする")
    void shouldThrowExceptionForIncorrectLengthBase64String() {
      String input = "AAA="; // Incorrect length
      assertThatThrownBy(() -> Longs.decodeFromBase64(input))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("デコードされたバイト配列の長さが不正です");
    }
  }
}
