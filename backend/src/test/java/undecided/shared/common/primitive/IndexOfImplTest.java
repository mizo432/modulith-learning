package undecided.shared.common.primitive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Strings2.IndexOfImpl}.
 *
 * <p>This class specifically tests the behavior of the {@code apply()} method in the {@code
 * IndexOfImpl} class, verifying its correctness under various input conditions.
 */
@DisplayName("Strings2.IndexOfImplのテスト")
class IndexOfImplTest {

  @Nested
  @DisplayName("applyメソッドのテスト")
  class ApplyTests {

    @Test
    @DisplayName("入力文字列内に部分文字列が存在する場合、そのインデックスを返すこと")
    void shouldReturnIndexWhenSubstringExists() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = "hello world";
      String subString = "world";

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(6, result, "Expected index of substring to be 6");
    }

    @Test
    @DisplayName("入力文字列内に部分文字列が存在しない場合、-1を返すこと")
    void shouldReturnMinusOneWhenSubstringNotExists() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = "hello world";
      String subString = "universe";

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(-1, result, "Expected -1 when substring does not exist");
    }

    @Test
    @DisplayName("部分文字列が空の場合、0を返すこと")
    void shouldReturnZeroWhenSubstringIsEmpty() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = "hello world";
      String subString = "";

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(0, result, "Expected index to be 0 for empty substring");
    }

    @Test
    @DisplayName("入力文字列がnullの場合、-1を返すこと")
    void shouldReturnMinusOneWhenInputStringIsNull() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = null;
      String subString = "world";

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(-1, result, "Expected -1 when input string is null");
    }

    @Test
    @DisplayName("入力文字列と部分文字列の両方が空の場合、0を返すこと")
    void shouldReturnZeroWhenBothInputAndSubstringAreEmpty() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = "";
      String subString = "";

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(0, result, "Expected index to be 0 when both input and substring are empty");
    }

    @Test
    @DisplayName("部分文字列がnullの場合、-1を返すこと")
    void shouldReturnMinusOneWhenSubstringIsNull() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = "hello world";
      String subString = null;

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(-1, result, "Expected -1 when substring is null");
    }

    @Test
    @DisplayName("入力文字列と部分文字列の両方がnullの場合、-1を返すこと")
    void shouldReturnMinusOneWhenBothInputAndSubstringAreNull() {
      // Given
      Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
      String str = null;
      String subString = null;

      // When
      int result = indexOf.apply(str, subString);

      // Then
      assertEquals(-1, result, "Expected -1 when both input string and substring are null");
    }
  }
}
