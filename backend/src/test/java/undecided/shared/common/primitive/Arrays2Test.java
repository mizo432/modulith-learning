package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Arrays2 - 配列ユーティリティのテスト")
class Arrays2Test {

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("配列が空の場合にtrueを返すこと")
    void shouldReturnTrueWhenArrayIsEmpty() {
      String[] emptyArray = new String[0];
      boolean result = Arrays2.isEmpty().test(emptyArray);
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("配列に要素がある場合にfalseを返すこと")
    void shouldReturnFalseWhenArrayIsNotEmpty() {
      String[] nonEmptyArray = {"element"};
      boolean result = Arrays2.isEmpty().test(nonEmptyArray);
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("引数がnullの場合はNullPointerExceptionが発生すること")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      assertThatThrownBy(() -> Arrays2.isEmpty().test(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("IS_EMPTY定数のテスト")
  class IsEmptyConstantTest {

    @Test
    @DisplayName("空配列に対してtrueを返すこと")
    void shouldReturnTrueForEmptyArray() {
      Object[] emptyArray = new Object[0];
      assertThat(Arrays2.IS_EMPTY.test(emptyArray)).isTrue();
    }

    @Test
    @DisplayName("要素あり配列に対してfalseを返すこと")
    void shouldReturnFalseForNonEmptyArray() {
      Object[] nonEmptyArray = {"test"};
      assertThat(Arrays2.IS_EMPTY.test(nonEmptyArray)).isFalse();
    }
  }

  @Nested
  @DisplayName("IS_NOT_EMPTY定数のテスト")
  class IsNotEmptyConstantTest {

    @Test
    @DisplayName("空配列に対してfalseを返すこと")
    void shouldReturnFalseForEmptyArray() {
      Object[] emptyArray = new Object[0];
      assertThat(Arrays2.IS_NOT_EMPTY.test(emptyArray)).isFalse();
    }

    @Test
    @DisplayName("要素あり配列に対してtrueを返すこと")
    void shouldReturnTrueForNonEmptyArray() {
      Object[] nonEmptyArray = {"test"};
      assertThat(Arrays2.IS_NOT_EMPTY.test(nonEmptyArray)).isTrue();
    }
  }

  @Nested
  @DisplayName("toStringsメソッドのテスト")
  class ToStringsTest {

    @Test
    @DisplayName("通常の配列を文字列に変換する場合、正しい結果を返すこと")
    void shouldReturnStringRepresentationOfArray() {
      String[] array = {"A", "B", "C"};
      String result = Arrays2.toStrings().apply(array);
      assertThat(result).isEqualTo("[A, B, C]");
    }

    @Test
    @DisplayName("空配列を文字列に変換する場合、正しい結果を返すこと")
    void shouldReturnStringRepresentationOfEmptyArray() {
      String[] array = {};
      String result = Arrays2.toStrings().apply(array);
      assertThat(result).isEqualTo("[]");
    }

    @Test
    @DisplayName("null引数の場合、NullPointerExceptionが発生すること")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      assertThatThrownBy(() -> Arrays2.toStrings().apply(null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
