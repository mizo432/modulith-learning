package com.github.mizo432.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
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

  @Nested
  @DisplayName("checkHalfWidthLengthClosedメソッドのテスト")
  class CheckHalfWidthLengthClosed {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが範囲内（境界値を含む）の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthWithinClosedRange() {
      String input = "abc"; // 半角3文字
      String result = StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値（境界値）の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthEqualsMin() {
      String input = "ab"; // 半角2文字
      String result = StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最大値（境界値）の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthEqualsMax() {
      String input = "abcde"; // 半角5文字
      String result = StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値未満の場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthBelowMin() {
      String input = "a"; // 半角1文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5));
    }

    @Test
    @DisplayName("文字列の半角長さが最大値を超える場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthExceedsMax() {
      String input = "abcdef"; // 半角6文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5));
    }

    @Test
    @DisplayName("文字列に全角文字が含まれる場合、半角換算で長さを検証する")
    void shouldCalculateLengthWithFullWidthCharacters() {
      String input = "あ"; // 全角1文字 = 半角2文字
      String result = StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 1, 3);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthLengthClosed(input, exceptionSupplier, 2, 5);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthClosed(input, nullSupplier, 2, 5));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthLengthAtLestメソッドのテスト")
  class CheckHalfWidthLengthAtLest {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが最小値以上の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthAtLeastMin() {
      String input = "abcd"; // 半角4文字
      String result = StringPrecondition.checkHalfWidthLengthAtLest(input, exceptionSupplier, 3);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値と等しい場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthEqualsMin() {
      String input = "abc"; // 半角3文字
      String result = StringPrecondition.checkHalfWidthLengthAtLest(input, exceptionSupplier, 3);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値未満の場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthBelowMin() {
      String input = "ab"; // 半角2文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthAtLest(input, exceptionSupplier, 3));
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthLengthAtLest(input, exceptionSupplier, 3);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkHalfWidthLengthAtLest(input, nullSupplier, 3));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthLengthGraterThanメソッドのテスト")
  class CheckHalfWidthLengthGraterThan {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが最小値より大きい場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthGreaterThanMin() {
      String input = "abcd"; // 半角4文字
      String result =
          StringPrecondition.checkHalfWidthLengthGraterThan(input, exceptionSupplier, 3);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最小値と等しい場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthEqualsMin() {
      String input = "abc"; // 半角3文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthGraterThan(input, exceptionSupplier, 3));
    }

    @Test
    @DisplayName("文字列の半角長さが最小値未満の場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthBelowMin() {
      String input = "ab"; // 半角2文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthGraterThan(input, exceptionSupplier, 3));
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result =
          StringPrecondition.checkHalfWidthLengthGraterThan(input, exceptionSupplier, 3);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthGraterThan(input, nullSupplier, 3));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthLengthAtMostメソッドのテスト")
  class CheckHalfWidthLengthAtMost {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが最大値以下の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthAtMostMax() {
      String input = "abc"; // 半角3文字
      String result = StringPrecondition.checkHalfWidthLengthAtMost(input, exceptionSupplier, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最大値と等しい場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthEqualsMax() {
      String input = "abcde"; // 半角5文字
      String result = StringPrecondition.checkHalfWidthLengthAtMost(input, exceptionSupplier, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最大値を超える場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthExceedsMax() {
      String input = "abcdef"; // 半角6文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthAtMost(input, exceptionSupplier, 5));
    }

    @Test
    @DisplayName("空文字列の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenEmpty() {
      String input = "";
      String result = StringPrecondition.checkHalfWidthLengthAtMost(input, exceptionSupplier, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthLengthAtMost(input, exceptionSupplier, 5);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkHalfWidthLengthAtMost(input, nullSupplier, 5));
    }
  }

  @Nested
  @DisplayName("checkHalfWidthLengthLessThanメソッドのテスト")
  class CheckHalfWidthLengthLessThan {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の半角長さが最大値より小さい場合、同じ文字列を返す")
    void shouldReturnSameStringWhenLengthLessThanMax() {
      String input = "abc"; // 半角3文字
      String result = StringPrecondition.checkHalfWidthLengthLessThan(input, exceptionSupplier, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列の半角長さが最大値と等しい場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthEqualsMax() {
      String input = "abcde"; // 半角5文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthLessThan(input, exceptionSupplier, 5));
    }

    @Test
    @DisplayName("文字列の半角長さが最大値を超える場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthExceedsMax() {
      String input = "abcdef"; // 半角6文字
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthLessThan(input, exceptionSupplier, 5));
    }

    @Test
    @DisplayName("空文字列の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenEmpty() {
      String input = "";
      String result = StringPrecondition.checkHalfWidthLengthLessThan(input, exceptionSupplier, 5);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkHalfWidthLengthLessThan(input, exceptionSupplier, 5);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(
              () -> StringPrecondition.checkHalfWidthLengthLessThan(input, nullSupplier, 5));
    }
  }

  @Nested
  @DisplayName("checkAllDecimalメソッドのテスト")
  class CheckAllDecimal {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("数字のみで構成される文字列の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenAllDigits() {
      String input = "12345";
      String result = StringPrecondition.checkAllDecimal(input, exceptionSupplier);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("空文字列の場合、同じ文字列を返す")
    void shouldReturnSameStringWhenEmpty() {
      String input = "";
      String result = StringPrecondition.checkAllDecimal(input, exceptionSupplier);
      assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("文字列がnullの場合、nullを返す")
    void shouldReturnNullWhenNullInput() {
      String input = null;
      String result = StringPrecondition.checkAllDecimal(input, exceptionSupplier);
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("英字を含む文字列の場合、例外がスローされる")
    void shouldThrowExceptionWhenContainsLetters() {
      String input = "12a45";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkAllDecimal(input, exceptionSupplier));
    }

    @Test
    @DisplayName("記号を含む文字列の場合、例外がスローされる")
    void shouldThrowExceptionWhenContainsSymbols() {
      String input = "12-45";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkAllDecimal(input, exceptionSupplier));
    }

    @Test
    @DisplayName("小数点を含む文字列の場合、例外がスローされる")
    void shouldThrowExceptionWhenContainsDecimalPoint() {
      String input = "12.34";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkAllDecimal(input, exceptionSupplier));
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "12345";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkAllDecimal(input, nullSupplier));
    }
  }

  @Nested
  @DisplayName("checkMaxLengthメソッドのテスト")
  class CheckMaxLength {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列の長さが最大値以下の場合、例外をスローしない")
    void shouldNotThrowExceptionWhenLengthWithinMax() {
      String input = "abc"; // 長さ3
      assertThatCode(() -> StringPrecondition.checkMaxLength(input, 5, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("文字列の長さが最大値と等しい場合、例外をスローしない")
    void shouldNotThrowExceptionWhenLengthEqualsMax() {
      String input = "abcde"; // 長さ5
      assertThatCode(() -> StringPrecondition.checkMaxLength(input, 5, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("文字列の長さが最大値を超える場合、例外がスローされる")
    void shouldThrowExceptionWhenLengthExceedsMax() {
      String input = "abcdef"; // 長さ6
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkMaxLength(input, 5, exceptionSupplier));
    }

    @Test
    @DisplayName("空文字列の場合、例外をスローしない")
    void shouldNotThrowExceptionWhenEmpty() {
      String input = "";
      assertThatCode(() -> StringPrecondition.checkMaxLength(input, 5, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("文字列がnullの場合、例外をスローしない")
    void shouldNotThrowExceptionWhenNullInput() {
      String input = null;
      assertThatCode(() -> StringPrecondition.checkMaxLength(input, 5, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkMaxLength(input, 5, nullSupplier));
    }
  }

  @Nested
  @DisplayName("checkRegexメソッドのテスト")
  class CheckRegex {

    private final Supplier<RuntimeException> exceptionSupplier = IllegalArgumentException::new;

    @Test
    @DisplayName("文字列が正規表現にマッチする場合、例外をスローしない")
    void shouldNotThrowExceptionWhenMatch() {
      String input = "abc123";
      String regex = "[a-z]+[0-9]+";
      assertThatCode(() -> StringPrecondition.checkRegex(input, regex, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("文字列が正規表現にマッチしない場合、例外がスローされる")
    void shouldThrowExceptionWhenNotMatch() {
      String input = "abc";
      String regex = "[0-9]+";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkRegex(input, regex, exceptionSupplier));
    }

    @Test
    @DisplayName("文字列がnullの場合、例外をスローしない")
    void shouldNotThrowExceptionWhenNullInput() {
      String input = null;
      String regex = ".*";
      assertThatCode(() -> StringPrecondition.checkRegex(input, regex, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("空文字列が正規表現にマッチする場合、例外をスローしない")
    void shouldNotThrowExceptionWhenEmptyAndMatch() {
      String input = "";
      String regex = ".*";
      assertThatCode(() -> StringPrecondition.checkRegex(input, regex, exceptionSupplier))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("空文字列が正規表現にマッチしない場合、例外がスローされる")
    void shouldThrowExceptionWhenEmptyAndNotMatch() {
      String input = "";
      String regex = ".+";
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> StringPrecondition.checkRegex(input, regex, exceptionSupplier));
    }

    @Test
    @DisplayName("例外サプライヤがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenExceptionSupplierIsNull() {
      String input = "abc";
      String regex = ".*";
      Supplier<RuntimeException> nullSupplier = null;
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> StringPrecondition.checkRegex(input, regex, nullSupplier));
    }
  }
}
