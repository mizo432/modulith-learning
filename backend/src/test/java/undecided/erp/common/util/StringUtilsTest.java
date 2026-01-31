package undecided.erp.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StringUtils Test")
public class StringUtilsTest {

  @Test
  @DisplayName("Test string reversal")
  void testReverseString() {
    // Given
    String input = "hello";

    // When
    String result = reverseString(input);

    // Then
    assertThat(result).isEqualTo("olleh");
  }

  /**
   * 指定された文字列を逆順にした新しい文字列を返します。 入力がnullの場合はnullを返します。
   *
   * @param input 逆順にする対象の文字列
   * @return 逆順にした文字列。入力がnullの場合はnull
   */
  private String reverseString(String input) {
    if (input == null) {
      return null;
    }
    return new StringBuilder(input).reverse().toString();
  }
}
