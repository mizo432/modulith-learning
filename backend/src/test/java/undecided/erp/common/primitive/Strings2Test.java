package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Strings2Test {

  /**
   * Tests for the `getHalfWidthLength` method in Strings2 class.
   */
  @Nested
  class GetHalfWidthLengthTest {

    @Test
    void withNarrowWidthCharacter() {
      String str = "a"; // ASCII character 'a' has a narrow width
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 1 for narrow width character").isEqualTo(1);
    }

    @Test
    void withWideWidthCharacter() {
      String str = String.valueOf(
          'あ'); // Japanese character 'あ' has a wide width
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 2 for wide width character").isEqualTo(2);
    }

    @Test
    void withNullCharacter() {
      String str = null;
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 0 as input string is null").isEqualTo(0);
    }

    @Test
    void withEmptyString() {
      String str = "";
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 0 as input string is empty").isEqualTo(0);
    }

  }


  @Nested
  class isAllCharacterHalfWidthTest {

    @Test
    void withFullWidthCharacter() {
      String str = String.valueOf('あ');
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is false for full width character").isFalse();
    }

    @Test
    void withHalfWidthCharacter() {
      String str = String.valueOf('a');
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for half width character").isTrue();
    }

    @Test
    void withEmptyString() {
      String str = "";
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for empty string").isTrue();
    }

    @Test
    void withNullString() {
      String str = null;
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for null string").isTrue();
    }

  }

  @Nested
  class IsAllCharacterFullWidthTest {

    @Test
    void withFullWidthCharacter() {
      String str = String.valueOf('あ');
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is true for full width character").isTrue();
    }

    @Test
    void withHalfWidthCharacter() {
      String str = String.valueOf('a');
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is false for half width character").isFalse();
    }

    @Test
    void withEmptyString() {
      String str = "";
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is true for empty string").isTrue();
    }

    @Test
    void withNullString() {
      String str = null;
      assertThat(Strings2.isAllCharacterFullWidth(str)).isTrue();
    }
  }
}
