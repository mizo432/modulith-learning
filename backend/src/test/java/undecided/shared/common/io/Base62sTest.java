package undecided.shared.common.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Base62sのテスト")
class Base62sTest {

  @Nested
  @DisplayName("DECODE_FROM_BASE62#applyのテスト")
  class DecodeFromBase62ApplyTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInputIsNull() {
      assertThatThrownBy(() -> Base62s.DECODE_FROM_BASE62.apply(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("\"0\"が渡された場合、0Lを返すこと")
    void shouldReturnZeroWhenInputIsZeroString() {
      Long result = Base62s.DECODE_FROM_BASE62.apply("0");
      assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("\"1\"が渡された場合、1Lを返すこと")
    void shouldReturnOneWhenInputIsOneString() {
      Long result = Base62s.DECODE_FROM_BASE62.apply("1");
      assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("\"z\"が渡された場合、61Lを返すこと")
    void shouldReturn61WhenInputIsZ() {
      Long result = Base62s.DECODE_FROM_BASE62.apply("z");
      assertThat(result).isEqualTo(61L);
    }

    @Test
    @DisplayName("\"10\"が渡された場合、62Lを返すこと")
    void shouldReturn62WhenInputIs10() {
      Long result = Base62s.DECODE_FROM_BASE62.apply("10");
      assertThat(result).isEqualTo(62L);
    }

    @Test
    @DisplayName("負数を表す文字列が渡された場合、負のLong値を返すこと")
    void shouldReturnNegativeValueForNegativeString() {
      Long result = Base62s.DECODE_FROM_BASE62.apply("-10");
      assertThat(result).isEqualTo(-62L);
    }

    @Test
    @DisplayName("無効なBase62文字が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionForInvalidCharacter() {
      assertThatThrownBy(() -> Base62s.DECODE_FROM_BASE62.apply("!invalid"))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("エンコードしてデコードした場合、元の値を返すこと")
    void shouldReturnOriginalValueAfterEncodeAndDecode() {
      long original = 123456789L;
      String encoded = Base62s.ENCODE_TO_BASE62.apply(original);
      Long decoded = Base62s.DECODE_FROM_BASE62.apply(encoded);
      assertThat(decoded).isEqualTo(original);
    }
  }

  @Nested
  @DisplayName("ENCODE_TO_BASE62#applyのテスト")
  class EncodeToBase62ApplyTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInputIsNull() {
      assertThatThrownBy(() -> Base62s.ENCODE_TO_BASE62.apply(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("0が渡された場合、\"0\"を返すこと")
    void shouldReturnZeroStringWhenInputIsZero() {
      String result = Base62s.ENCODE_TO_BASE62.apply(0L);
      assertThat(result).isEqualTo("0");
    }

    @Test
    @DisplayName("正の値が渡された場合、Base62エンコードされた文字列を返すこと")
    void shouldReturnBase62StringForPositiveValue() {
      String result = Base62s.ENCODE_TO_BASE62.apply(62L);
      assertThat(result).isEqualTo("10");
    }

    @Test
    @DisplayName("1が渡された場合、\"1\"を返すこと")
    void shouldReturnOneStringWhenInputIsOne() {
      String result = Base62s.ENCODE_TO_BASE62.apply(1L);
      assertThat(result).isEqualTo("1");
    }

    @Test
    @DisplayName("61が渡された場合、\"z\"を返すこと")
    void shouldReturnZWhenInputIs61() {
      String result = Base62s.ENCODE_TO_BASE62.apply(61L);
      assertThat(result).isEqualTo("z");
    }

    @Test
    @DisplayName("負の値が渡された場合、\"-\"プレフィックス付きのBase62エンコードされた文字列を返すこと")
    void shouldReturnBase62StringWithMinusPrefixForNegativeValue() {
      String result = Base62s.ENCODE_TO_BASE62.apply(-62L);
      assertThat(result).isEqualTo("-10");
    }

    @Test
    @DisplayName("Long.MAX_VALUEが渡された場合、Base62エンコードされた文字列を返すこと")
    void shouldReturnBase62StringForLongMaxValue() {
      String result = Base62s.ENCODE_TO_BASE62.apply(Long.MAX_VALUE);
      assertThat(result).isNotEmpty();
    }
  }
}
