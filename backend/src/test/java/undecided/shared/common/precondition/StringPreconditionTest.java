package undecided.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StringPreconditionTest - 文字列の事前条件を検証するテスト")
class StringPreconditionTest {

  @Nested
  @DisplayName("checkNonEmptyメソッドのテスト")
  class CheckNonEmpty {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列が非空の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenNonEmpty() {
      String input = "test";
      String result = StringPrecondition.checkNonEmpty(input, exceptionSupplier);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列が空の場合、例外がスローされる")
    void shouldThrowExceptionWhenEmpty() {
      String input = "";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkNonEmpty(input, exceptionSupplier));
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNull() {
      String input = null;
      String result = StringPrecondition.checkNonEmpty(input, exceptionSupplier);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "test";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkNonEmpty(input, nullSupplier));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthLengthOpenメソッドのテスト")
  class CheckHalfWidthLengthOpen {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが範囲内の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthWithinOpenRange() {
      String input = "abcd"; // 半角文字列。
      String result = StringPrecondition.checkHalfWidthLengthOpen(input, exceptionSupplier, 2, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値未満の場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthBelowMin() {
      String input = "a"; // 1文字。
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthOpen(input, exceptionSupplier, 2, 5));
    }

    @Test
    @DisplayName("文字列の半角長さが最大値を超える場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthExceedsMax() {
      String input = "abcdef"; // 6文字。
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthOpen(input, exceptionSupplier, 2, 5));
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthLengthOpen(input, exceptionSupplier, 2, 5);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abcd"; // 半角文字列。
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkHalfWidthLengthOpen(input, nullSupplier, 2, 5));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthFixedLengthメソッドのテスト")
  class CheckHalfWidthFixedLength {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが指定された長さと一致する場合、同じ文字列を返す")
    void shouldReturnSameStringWhenFixedLengthMatches() {
      String input = "abcd"; // 4半角文字。
      String result = StringPrecondition.checkHalfWidthFixedLength(input, exceptionSupplier, 4);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが指定された長さと一致しない場合、例外がスローされる")
    void shouldThrowExceptionWhenFixedLengthDoesNotMatch() {
      String input = "abc"; // 半角長さが3。
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthFixedLength(input, exceptionSupplier, 4));
    }

    @Test
    @DisplayName("文字列に非半角文字が含まれる場合、例外がスローされる")
    void shouldThrowExceptionWhenValueContainsNonHalfWidthCharacters() {
      String input = "abあ"; // 非半角文字を含む。
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthFixedLength(input, exceptionSupplier, 3));
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenValueIsNull() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthFixedLength(input, exceptionSupplier, 4);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abcd"; // 半角文字列。
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkHalfWidthFixedLength(input, nullSupplier, 4));
    }
  }
}
