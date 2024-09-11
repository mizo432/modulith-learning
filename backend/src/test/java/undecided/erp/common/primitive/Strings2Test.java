package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Strings2Test {

  /**
   * Method under test: {@link Strings2#ltrim(String)}
   * <p>
   * Method under test: {@link Strings2#ltrim(String, String)}
   */
  @Nested
  class lTrimTest {

    @Test
    void shouldReturnSameStringWhenTrimValueIsNotFound() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim("Str", "Trim"))
          .isEqualTo("Str");
    }

    @Test
    void shouldReturnTrimmedStringWhenTrimValueIsFound() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim("TrimTrimStr", "Trim"))
          .isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyStringWhenOnlyTrimValueIsPresent() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim("TrimTrim", "Trim"))
          .isEmpty();
    }

    @Test
    void shouldReturnNullWhenBothArgumentsAreNull() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim(null, null))
          .isNull();
    }

    @Test
    void shouldReturnSameStringWhenTrimValueIsNull() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim("Str", null))
          .isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyStringForEmptyInput() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim(Strings2.EMPTY, "Trim"))
          .isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnEmptyStringWhenRemovingBlanks() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim(Strings2.BLANK, null))
          .isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnSameStringForNonEmptyInput() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim("Str")).isEqualTo("Str");
    }

    @Test
    void shouldReturnNullForNullInput() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim(null)).isNull();
    }

    @Test
    void shouldReturnEmptyStringForBlankInput() {
      // Arrange, Act and Assert
      assertThat(Strings2.ltrim(Strings2.BLANK))
          .isEqualTo(Strings2.EMPTY);
    }
  }

  /**
   * Method under test: {@link Strings2#trimPrefix(String, String)}
   */
  @Nested
  class TrimPrefixTest {

    @Test
    void trimPrefix() {
      // Arrange, Act and Assert
      assertThat(Strings2.trimPrefix("Text", "Prefix")).isEqualTo("Text");
      assertThat(Strings2.trimPrefix(null, null)).isNull();
      assertThat(Strings2.trimPrefix("Text", null)).isEqualTo("Text");
      assertThat(Strings2.trimPrefix("Text", Strings2.EMPTY)).isEqualTo("Text");
    }

  }

  /**
   * Method under test: {@link Strings2#trim(String)}
   */
  @Nested
  class TrimTest {

    @Test
    void shouldReturnOriginalStringWhenNoExtraSpaces() {
      // Arrange, Act and Assert
      assertThat(Strings2.trim("Str")).isEqualTo("Str");
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
      // Arrange, Act and Assert
      assertThat(Strings2.trim(null)).isNull();
    }

    @Test
    void shouldReturnEmptyStringWhenInputIsBlank() {
      // Arrange, Act and Assert
      assertThat(Strings2.trim(Strings2.BLANK)).isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnEmptyStringWhenInputIsSpaces() {
      // Arrange, Act and Assert
      assertThat(Strings2.trim("  ")).isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldRemoveLeadingSpaces() {
      // Arrange, Act and Assert
      assertThat(Strings2.trim(" Str")).isEqualTo("Str");
    }
  }

  /**
   * Method under test: {@link Strings2#repeat(String, int)}
   */
  @Nested
  class RepeatTest {

    @Test
    void shouldRepeatStringOnce() {
      // Arrange, Act and Assert
      assertThat(Strings2.repeat("Str", 1)).isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyWhenRepeatZeroTimes() {
      // Arrange, Act and Assert
      assertThat(Strings2.repeat("Str", 0)).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenRepeatNegativeTimes() {
      // Arrange, Act and Assert
      assertThatThrownBy(() -> Strings2.repeat("Str", -1)).isInstanceOf(
              IllegalArgumentException.class)
          .hasMessage("times must not negative times=-1");
    }

    @Test
    void shouldRepeatStringOnceWithSeparator() {
      // Arrange, Act and Assert
      assertThat(Strings2.repeat("Str", 1, "Separator")).isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyWhenRepeatZeroTimesWithSeparator() {
      // Arrange, Act and Assert
      assertThat(Strings2.repeat("Str", 0, "Separator")).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenRepeatNegativeTimesWithSeparator() {
      // Arrange, Act and Assert
      assertThatThrownBy(() -> Strings2.repeat("Str", -1, "Separator")).isInstanceOf(
              IllegalArgumentException.class)
          .hasMessage("times must not negative times=-1");
    }
  }

  /**
   * Method under test: {@link Strings2#surround(String, String)}
   */
  @Nested
  class SurroundTest {

    @Test
    void surround() {
      // Arrange, Act and Assert
      assertThat(Strings2.surround("Str", "Surround String")).isEqualTo(
          "Surround StringStrSurround String");
    }

  }

  /**
   * Method under test: {@link Strings2#rtrim(String, String)}
   * <p>
   * Method under test: {@link Strings2#rtrim(String)}
   */
  @Nested
  class RtrimTest {

    @Test
    void shouldTrimGivenStringEndingWithTrim() {
      assertThat(Strings2.rtrim("Str", "Trim")).isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyWhenTrimmingStringEqualToTrimString() {
      assertThat(Strings2.rtrim("AbcAbc", "Abc")).isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnStringWithoutTrimStringAtTheEnd() {
      assertThat(Strings2.rtrim("ZAbcAbc", "Abc")).isEqualTo("Z");
    }

    @Test
    void shouldReturnNullWhenBothStringsAreNull() {
      assertThat(Strings2.rtrim(null, null)).isNull();
    }

    @Test
    void shouldReturnOriginalStringWhenTrimStringIsNull() {
      assertThat(Strings2.rtrim("Str", null)).isEqualTo("Str");
    }

    @Test
    void shouldReturnEmptyStringWhenOriginalStringIsEmpty() {
      assertThat(Strings2.rtrim(Strings2.EMPTY, "Trim")).isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnOriginalStringWhenTrimmingWithoutTrimString() {
      assertThat(Strings2.rtrim("Str")).isEqualTo("Str");
    }

    @Test
    void shouldReturnNullWhenOriginalStringIsNull() {
      assertThat(Strings2.rtrim(null)).isNull();
    }

    @Test
    void shouldReturnEmptyStringWhenTrimmingWhiteSpace() {
      assertThat(Strings2.rtrim(Strings2.BLANK)).isEqualTo(Strings2.EMPTY);
    }

    @Test
    void shouldReturnEmptyStringWhenTrimmingMultipleWhiteSpaces() {
      assertThat(Strings2.rtrim("  ")).isEqualTo(Strings2.EMPTY);
    }
  }

  /**
   * Tests for the `getHalfWidthLength` method in Strings2 class.
   */
  @Nested
  class GetHalfWidthLengthTest {

    @Test
    void shouldReturnLengthOneForNarrowWidthCharacter() {
      String str = "a"; // ASCII character 'a' has a narrow width
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 1 for narrow width character").isEqualTo(1);
    }

    @Test
    void shouldReturnLengthTwoForWideWidthCharacter() {
      String str = String.valueOf('あ'); // Japanese character 'あ' has a wide width
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 2 for wide width character").isEqualTo(2);
    }

    @Test
    void shouldReturnLengthZeroForNullCharacter() {
      String str = null;
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 0 as input string is null").isEqualTo(0);
    }

    @Test
    void shouldReturnLengthZeroForEmptyString() {
      String str = "";
      int result = Strings2.getHalfWidthLength(str);
      assertThat(result).as("Expected length is 0 as input string is empty").isEqualTo(0);
    }

  }

  @Nested
  class IsAllCharacterHalfWidthTest {

    @Test
    void shouldReturnFalseForFullWidthCharacter() {
      String str = String.valueOf('あ');
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is false for full width character").isFalse();
    }

    @Test
    void shouldReturnTrueForHalfWidthCharacter() {
      String str = String.valueOf('a');
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for half width character").isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyString() {
      String str = "";
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for empty string").isTrue();
    }

    @Test
    void shouldReturnTrueForNullString() {
      String str = null;
      boolean result = Strings2.isAllCharacterHalfWidth(str);
      assertThat(result).as("Expected result is true for null string").isTrue();
    }

  }

  @Nested
  class IsAllCharacterFullWidthTest {

    @Test
    void shouldReturnTrueForFullWidthCharacter() {
      String str = String.valueOf('あ');
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is true for full width character").isTrue();
    }

    @Test
    void shouldReturnFalseForHalfWidthCharacter() {
      String str = String.valueOf('a');
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is false for half width character").isFalse();
    }

    @Test
    void shouldReturnTrueForEmptyString() {
      String str = "";
      boolean result = Strings2.isAllCharacterFullWidth(str);
      assertThat(result).as("Expected result is true for empty string").isTrue();
    }

    @Test
    void shouldReturnTrueForNullString() {
      String str = null;
      assertThat(Strings2.isAllCharacterFullWidth(str)).isTrue();
    }
  }

  @Nested
  class DefaultIfEmptyTest {

    @Test
    void shouldReturnSameStringIfNotEmptyForEmptyStringAsDefaultValue() {
      String str = "Not Empty";
      String defaultValue = "";
      String result = Strings2.defaultIfEmpty(str, defaultValue);
      assertThat(result).as("Expected original string, because it is not empty").isEqualTo(str);
    }

    @Test
    void shouldReturnNullForNullStringAndNullDefaultValue() {
      String str = null;
      String defaultValue = null;
      String result = Strings2.defaultIfEmpty(str, defaultValue);
      assertThat(result).as(
          "Expected null string, because input string and default string are null").isNull();
    }
  }

  @Nested
  class StartWithTest {

    @Test
    void shouldReturnTrueWhenStringStartsWithPrefix() {
      String str = "Hello World";
      String prefix = "Hello";
      assertThat(Strings2.startWith(str, prefix)).as(
          "Expected true as the string starts with the prefix.").isTrue();
    }

    @Test
    void shouldReturnFalseWhenStringStartsWithPrefix2() {
      String str = "Hello";
      String prefix = "Hello World";
      assertThat(Strings2.startWith(str, prefix)).as(
          "Expected true as the string starts with the prefix.").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotStartsWithPrefix() {
      String str = "Hello World";
      String prefix = "World";
      assertThat(Strings2.startWith(str, prefix)).as(
              "Expected false as the string does not start with the prefix.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringAndPrefixAreNull() {
      String str = null;
      String prefix = null;
      assertThat(Strings2.startWith(str, prefix)).as(
          "Expected false as both string and prefix are null.").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
      String str = null;
      String prefix = "Hello";
      assertThat(Strings2.startWith(str, prefix)).as("Expected false as the string is null.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenPrefixIsNull() {
      String str = "Hello World";
      String prefix = null;
      assertThat(Strings2.startWith(str, prefix)).as("Expected false as the prefix is null.")
          .isFalse();
    }
  }

  @Nested
  class StartsWithIgnoreCaseTest {

    @Test
    void shouldReturnTrueWhenStringStartsWithPrefixIgnoringCase() {
      String str = "Hello World";
      String prefix = "hello";
      assertThat(Strings2.startsWithIgnoreCase(str, prefix))
          .as("Expected true as the string starts with the prefix, ignoring case.")
          .isTrue();
    }

    @Test
    void shouldReturnTrueWhenStringStartsWithPrefixIgnoringCase2() {
      String str = "hello";
      String prefix = "Hello World";
      assertThat(Strings2.startsWithIgnoreCase(str, prefix))
          .as("Expected true as the string starts with the prefix, ignoring case.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotStartsWithPrefixIgnoringCase() {
      String str = "Hello World";
      String prefix = "WORLD";
      assertThat(Strings2.startsWithIgnoreCase(str, prefix))
          .as("Expected false as the string does not start with the prefix, ignoring case.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringAndPrefixAreNull() {
      String str = null;
      String prefix = null;
      assertThat(Strings2.startsWithIgnoreCase(str, prefix)).as(
              "Expected false as both string and prefix are null.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
      String str = null;
      String prefix = "Hello";
      assertThat(Strings2.startsWithIgnoreCase(str, prefix)).as(
          "Expected false as the string is null.").isFalse();
    }

    @Test
    void shouldReturnFalseWhenPrefixIsNull() {
      String str = "Hello World";
      String prefix = null;
      assertThat(Strings2.startsWithIgnoreCase(str, prefix)).as(
          "Expected false as the prefix is null.").isFalse();
    }
  }

  @Nested
  class EndsWithTest {

    @Test
    void shouldReturnTrueWhenStringEndsWithSuffix() {
      String str = "Hello World";
      String suffix = "World";
      assertThat(Strings2.endsWith(str, suffix)).as(
          "Expected true as the string ends with the suffix.").isTrue();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotEndsWithSuffix() {
      String str = "Hello World";
      String suffix = "Hello";
      assertThat(Strings2.endsWith(str, suffix)).as(
              "Expected false as the string does not end with the suffix.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotEndsWithSuffix2() {
      String str = "Hello";
      String suffix = "Hello World";
      assertThat(Strings2.endsWith(str, suffix)).as(
              "Expected false as the string does not end with the suffix.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringAndSuffixAreNull() {
      String str = null;
      String suffix = null;
      assertThat(Strings2.endsWith(str, suffix)).as(
          "Expected false as both string and suffix are null.").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
      String str = null;
      String suffix = "World";
      assertThat(Strings2.endsWith(str, suffix)).as("Expected false as the string is null.")
          .isFalse();
    }

    @Test
    void shouldReturnFalseWhenSuffixIsNull() {
      String str = "Hello World";
      String suffix = null;
      assertThat(Strings2.endsWith(str, suffix)).as("Expected false as the suffix is null.")
          .isFalse();
    }
  }

  @Nested
  class EndsWithIgnoreCaseTest {

    @Test
    void shouldReturnTrueWhenStringEndsWithSuffixIgnoringCase() {
      String str = "Hello World";
      String suffix = "WORLD";
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "文字列が大文字小文字を無視して接尾辞で終わっているため、trueを期待します。").isTrue();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotEndsWithSuffixIgnoringCase() {
      String str = "Hello World";
      String suffix = "HELLO";
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "文字列が大文字小文字を無視して接尾辞で終わっていないため、falseを期待します。").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringDoesNotEndsWithSuffixIgnoringCase2() {
      String str = "hello";
      String suffix = "Hello World";
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "文字列が大文字小文字を無視して接尾辞で終わっていないため、falseを期待します。").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringAndSuffixAreNull() {
      String str = null;
      String suffix = null;
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "文字列と接尾辞の両方がnullであるため、falseを期待します。").isFalse();
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
      String str = null;
      String suffix = "World";
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "文字列がnullであるため、falseを期待します。").isFalse();
    }

    @Test
    void shouldReturnFalseWhenSuffixIsNull() {
      String str = "Hello World";
      String suffix = null;
      assertThat(Strings2.endsWithIgnoreCase(str, suffix)).as(
          "接尾辞がnullであるため、falseを期待します。").isFalse();
    }
  }

  @Nested
  class TrimSuffixTest {

    @Test
    void shouldReturnOriginalStringIfSuffixIsNull() {
      String str = "Hello World";
      String suffix = null;
      String result = Strings2.trimSuffix(str, suffix);
      assertThat(result).as("Expected original string as the suffix is null").isEqualTo(str);
    }

    @Test
    void shouldReturnOriginalStringIfStringDoesNotEndWithSuffix() {
      String str = "Hello World";
      String suffix = "Hello";
      String result = Strings2.trimSuffix(str, suffix);
      assertThat(result).as("Expected original string as the string does not end with the suffix")
          .isEqualTo(str);
    }

    @Test
    void shouldReturnTrimmedStringIfStringEndsWithSuffix() {
      String str = "Hello World";
      String suffix = "World";
      String result = Strings2.trimSuffix(str, suffix);
      assertThat(result).as("Expected trimmed string as the string ends with the suffix")
          .isEqualTo("Hello ");
    }

    @Test
    void shouldReturnNullIfStringIsNull() {
      String str = null;
      String suffix = "World";
      String result = Strings2.trimSuffix(str, suffix);
      assertThat(result).as("Expected null as the input string is null").isNull();
    }
  }
}
