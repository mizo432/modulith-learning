package undecided.shared.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Strings2Test {

  /** Method under test: {@link Strings2#toString(String)} */
  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("文字列をそのまま返す")
    void shouldReturnOriginalString() {
      String input = "Hello";
      assertThat(Strings2.toString(input)).isEqualTo("Hello");
    }

    @Test
    @DisplayName("nullをそのまま返す")
    void shouldReturnNull() {
      assertThat(Strings2.toString(null)).isNull();
    }

    @Test
    @DisplayName("空文字をそのまま返す")
    void shouldReturnEmptyString() {
      assertThat(Strings2.toString("")).isEmpty();
    }
  }

  /** Method under test: {@link Strings2#isEmpty(String)} */
  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyStaticTest {

    @Test
    @DisplayName("nullの場合trueを返す")
    void shouldReturnTrueForNull() {
      assertThat(Strings2.isEmpty(null)).isTrue();
    }

    @Test
    @DisplayName("空文字の場合trueを返す")
    void shouldReturnTrueForEmptyString() {
      assertThat(Strings2.isEmpty("")).isTrue();
    }

    @Test
    @DisplayName("空でない文字列の場合falseを返す")
    void shouldReturnFalseForNonEmptyString() {
      assertThat(Strings2.isEmpty("Hello")).isFalse();
    }

    @Test
    @DisplayName("空白文字の場合falseを返す")
    void shouldReturnFalseForBlankString() {
      assertThat(Strings2.isEmpty("   ")).isFalse();
    }
  }

  /** Method under test: {@link Strings2#formatDateTime(LocalDateTime)} */
  @Nested
  @DisplayName("formatDateTimeメソッドのテスト")
  class FormatDateTimeTest {

    @Test
    @DisplayName("nullの場合nullを返す")
    void shouldReturnNullForNullDateTime() {
      assertThat(Strings2.formatDateTime(null)).isNull();
    }

    @Test
    @DisplayName("LocalDateTimeをyyyy-MM-dd HH:mm:ss形式でフォーマットする")
    void shouldFormatDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 14, 30, 45);
      assertThat(Strings2.formatDateTime(dateTime)).isEqualTo("2023-10-15 14:30:45");
    }
  }

  /** Method under test: {@link Strings2.IsEmpty} */
  @Nested
  @DisplayName("IsEmpty内部クラスのテスト")
  class IsEmptyClassTest {

    @Test
    @DisplayName("nullの場合trueを返す")
    void shouldReturnTrueForNull() {
      assertThat(Strings2.IS_EMPTY.test(null)).isTrue();
    }

    @Test
    @DisplayName("空文字の場合trueを返す")
    void shouldReturnTrueForEmptyString() {
      assertThat(Strings2.IS_EMPTY.test("")).isTrue();
    }

    @Test
    @DisplayName("空でない文字列の場合falseを返す")
    void shouldReturnFalseForNonEmptyString() {
      assertThat(Strings2.IS_EMPTY.test("Hello")).isFalse();
    }

    @Test
    @DisplayName("空白文字の場合falseを返す")
    void shouldReturnFalseForBlankString() {
      assertThat(Strings2.IS_EMPTY.test("   ")).isFalse();
    }
  }

  /** Method under test: {@link Strings2.Surround} */
  @Nested
  @DisplayName("Surround内部クラスのテスト")
  class SurroundClassTest {

    @Test
    @DisplayName("文字列を囲み文字で囲む")
    void shouldSurroundString() {
      assertThat(Strings2.SURROUND.apply("Hello", "\"")).isEqualTo("\"Hello\"");
    }

    @Test
    @DisplayName("囲み文字がnullの場合空文字として扱う")
    void shouldTreatNullSurroundAsEmpty() {
      assertThat(Strings2.SURROUND.apply("Hello", null)).isEqualTo("Hello");
    }

    @Test
    @DisplayName("対象文字列がnullの場合空文字として扱う")
    void shouldTreatNullStringAsEmpty() {
      assertThat(Strings2.SURROUND.apply(null, "\"")).isEqualTo("\"\"");
    }

    @Test
    @DisplayName("両方がnullの場合空文字を返す")
    void shouldReturnEmptyWhenBothNull() {
      assertThat(Strings2.SURROUND.apply(null, null)).isEqualTo("");
    }

    @Test
    @DisplayName("複数の囲み文字で囲む")
    void shouldSurroundWithMultipleChars() {
      assertThat(Strings2.SURROUND.apply("test", "[]")).isEqualTo("[]test[]");
    }
  }

  /** Method under test: {@link Strings2.IndexOfImpl} */
  @Nested
  @DisplayName("IndexOfImpl内部クラスのテスト")
  class IndexOfImplTest {

    private final Strings2.IndexOf indexOf = new Strings2.IndexOfImpl();

    @Test
    @DisplayName("部分文字列が見つかった場合インデックスを返す")
    void shouldReturnIndexWhenFound() {
      assertThat(indexOf.apply("Hello World", "World")).isEqualTo(6);
    }

    @Test
    @DisplayName("部分文字列が見つからない場合-1を返す")
    void shouldReturnMinusOneWhenNotFound() {
      assertThat(indexOf.apply("Hello World", "xyz")).isEqualTo(-1);
    }

    @Test
    @DisplayName("対象文字列がnullの場合-1を返す")
    void shouldReturnMinusOneWhenStringIsNull() {
      assertThat(indexOf.apply(null, "World")).isEqualTo(-1);
    }

    @Test
    @DisplayName("部分文字列がnullの場合-1を返す")
    void shouldReturnMinusOneWhenSubStringIsNull() {
      assertThat(indexOf.apply("Hello World", null)).isEqualTo(-1);
    }

    @Test
    @DisplayName("両方がnullの場合-1を返す")
    void shouldReturnMinusOneWhenBothNull() {
      assertThat(indexOf.apply(null, null)).isEqualTo(-1);
    }

    @Test
    @DisplayName("先頭にある場合0を返す")
    void shouldReturnZeroWhenAtBeginning() {
      assertThat(indexOf.apply("Hello", "He")).isEqualTo(0);
    }
  }

  /** Method under test: {@link Strings2.LowerCamelImpl} */
  @Nested
  @DisplayName("LowerCamelImpl内部クラスのテスト")
  class LowerCamelImplTest {

    private final Strings2.LowerCamel lowerCamel = new Strings2.LowerCamelImpl();

    @Test
    @DisplayName("nullの場合空文字を返す")
    void shouldReturnEmptyForNull() {
      assertThat(lowerCamel.convertToString(null)).isEmpty();
    }

    @Test
    @DisplayName("空文字の場合空文字を返す")
    void shouldReturnEmptyForEmptyString() {
      assertThat(lowerCamel.convertToString("")).isEmpty();
    }

    @Test
    @DisplayName("アンダースコア区切りをローワーキャメルに変換する")
    void shouldConvertUnderscoreToLowerCase() {
      assertThat(lowerCamel.convertToString("hello_world")).isEqualTo("helloWorld");
    }

    @Test
    @DisplayName("大文字アンダースコア区切りをローワーキャメルに変換する")
    void shouldConvertUpperUnderscoreToLowerCase() {
      assertThat(lowerCamel.convertToString("HELLO_WORLD")).isEqualTo("helloWorld");
    }

    @Test
    @DisplayName("大文字のみの文字列を小文字に変換する")
    void shouldConvertUpperCaseToLowerCase() {
      assertThat(lowerCamel.convertToString("HELLO")).isEqualTo("hello");
    }

    @Test
    @DisplayName("先頭を小文字に変換する")
    void shouldConvertFirstCharToLower() {
      assertThat(lowerCamel.convertToString("HelloWorld")).isEqualTo("helloWorld");
    }

    @Test
    @DisplayName("既にローワーキャメルの場合は先頭のみ小文字にする")
    void shouldKeepLowerCamelCase() {
      assertThat(lowerCamel.convertToString("helloWorld")).isEqualTo("helloWorld");
    }

    @Test
    @DisplayName("複数のアンダースコア区切りを処理する")
    void shouldHandleMultipleUnderscores() {
      assertThat(lowerCamel.convertToString("hello_world_test")).isEqualTo("helloWorldTest");
    }
  }

  /** Method under test: {@link Strings2.Repeat} */
  @Nested
  @DisplayName("Repeat内部クラスのテスト")
  class RepeatClassTest {

    private final Strings2 strings2 = new Strings2();

    @Test
    @DisplayName("指定回数繰り返す")
    void shouldRepeatString() {
      Strings2.Repeat repeat = strings2.new Repeat(3);
      assertThat(repeat.apply("ab")).isEqualTo("ababab");
    }

    @Test
    @DisplayName("1回繰り返す")
    void shouldRepeatOnce() {
      Strings2.Repeat repeat = strings2.new Repeat(1);
      assertThat(repeat.apply("Hello")).isEqualTo("Hello");
    }

    @Test
    @DisplayName("文字列がnullの場合例外をスローする")
    void shouldThrowExceptionForNullString() {
      Strings2.Repeat repeat = strings2.new Repeat(3);
      assertThatThrownBy(() -> repeat.apply(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("回数が正の整数でない場合例外をスローする")
    void shouldThrowExceptionForNonPositiveTimes() {
      assertThatThrownBy(() -> strings2.new Repeat(0).apply("test"))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  /** Method under test: {@link Strings2#COLLECTION_TO_COMMA_DELIMITED_STRING} */
  @Nested
  @DisplayName("COLLECTION_TO_COMMA_DELIMITED_STRING定数のテスト")
  class CollectionToCommaDelimitedStringTest {

    @Test
    @DisplayName("コレクションをカンマ区切り文字列に変換する")
    void shouldConvertCollectionToCommaDelimitedString() {
      Collection<String> items = List.of("a", "b", "c");
      assertThat(Strings2.COLLECTION_TO_COMMA_DELIMITED_STRING.apply(items)).isEqualTo("a,b,c");
    }

    @Test
    @DisplayName("コレクションの要素が文字列に変換される")
    void shouldConvertElementsToString() {
      Collection<Integer> items = List.of(1, 2, 3);
      assertThat(Strings2.COLLECTION_TO_COMMA_DELIMITED_STRING.apply(items)).isEqualTo("1,2,3");
    }

    @Test
    @DisplayName("空のコレクションの場合空文字を返す")
    void shouldReturnEmptyForEmptyCollection() {
      Collection<String> items = List.of();
      assertThat(Strings2.COLLECTION_TO_COMMA_DELIMITED_STRING.apply(items)).isEmpty();
    }

    @Test
    @DisplayName("1つの要素の場合その要素を返す")
    void shouldReturnSingleElement() {
      Collection<String> items = List.of("only");
      assertThat(Strings2.COLLECTION_TO_COMMA_DELIMITED_STRING.apply(items)).isEqualTo("only");
    }
  }
}
