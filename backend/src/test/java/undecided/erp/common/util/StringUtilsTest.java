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
   * Simple utility method to reverse a string. This is just for demonstration purposes.
   */
  private String reverseString(String input) {
    if (input == null) {
      return null;
    }
    return new StringBuilder(input).reverse().toString();
  }
}
