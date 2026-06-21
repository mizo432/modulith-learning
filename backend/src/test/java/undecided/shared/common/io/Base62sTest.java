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
