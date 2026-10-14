package undecided.shared.value.datetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ValidIntervalTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("開始日と終了日を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidDates() {
      LocalDate start = LocalDate.of(2024, 1, 1);
      LocalDate end = LocalDate.of(2024, 12, 31);
      ValidInterval interval = ValidInterval.of(start, end);

      assertThat(interval).isNotNull();
      assertThat(interval.validFrom()).isEqualTo(start);
      assertThat(interval.validTo()).isEqualTo(end);
    }

    @Test
    @DisplayName("開始日のみでインスタンスが作成される")
    void shouldCreateInstanceWithStartDateOnly() {
      LocalDate start = LocalDate.of(2024, 1, 1);
      ValidInterval interval = ValidInterval.of(start);

      assertThat(interval).isNotNull();
      assertThat(interval.validFrom()).isEqualTo(start);
      assertThat(interval.validTo()).isNull();
    }

    @Test
    @DisplayName("終了日が開始日と同じ場合許容される")
    void shouldAllowEndDateEqualToStartDate() {
      LocalDate date = LocalDate.of(2024, 6, 1);
      ValidInterval interval = ValidInterval.of(date, date);

      assertThat(interval).isNotNull();
      assertThat(interval.validFrom()).isEqualTo(date);
      assertThat(interval.validTo()).isEqualTo(date);
    }

    @Test
    @DisplayName("開始日がnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenStartDateIsNull() {
      assertThatThrownBy(() -> ValidInterval.of(null, LocalDate.of(2024, 12, 31)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validFrom must not be null");
    }

    @Test
    @DisplayName("終了日が開始日より前の場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
      LocalDate start = LocalDate.of(2024, 12, 31);
      LocalDate end = LocalDate.of(2024, 1, 1);

      assertThatThrownBy(() -> ValidInterval.of(start, end))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validTo must not be before validFrom");
    }

    @Test
    @DisplayName("開始日のみでnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenStartDateIsNullSingleArg() {
      assertThatThrownBy(() -> ValidInterval.of((LocalDate) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validFrom must not be null");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      ValidInterval interval = ValidInterval.empty();

      assertThat(interval).isNotNull();
      assertThat(interval.validFrom()).isNull();
      assertThat(interval.validTo()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValues() {
      ValidInterval interval = new ValidInterval();

      assertThat(interval).isNotNull();
      assertThat(interval.validFrom()).isNull();
      assertThat(interval.validTo()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスはtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      ValidInterval interval = ValidInterval.empty();

      assertThat(interval.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスはfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      ValidInterval interval = ValidInterval.of(LocalDate.of(2024, 1, 1));

      assertThat(interval.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("containsメソッドのテスト")
  class ContainsTest {

    @Test
    @DisplayName("期間内の日付はtrueを返す")
    void shouldReturnTrueForDateWithinInterval() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(LocalDate.of(2024, 6, 15))).isTrue();
    }

    @Test
    @DisplayName("開始日はtrueを返す")
    void shouldReturnTrueForStartDate() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(LocalDate.of(2024, 1, 1))).isTrue();
    }

    @Test
    @DisplayName("終了日はtrueを返す")
    void shouldReturnTrueForEndDate() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(LocalDate.of(2024, 12, 31))).isTrue();
    }

    @Test
    @DisplayName("開始日より前の日付はfalseを返す")
    void shouldReturnFalseForDateBeforeStart() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(LocalDate.of(2023, 12, 31))).isFalse();
    }

    @Test
    @DisplayName("終了日より後の日付はfalseを返す")
    void shouldReturnFalseForDateAfterEnd() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(LocalDate.of(2025, 1, 1))).isFalse();
    }

    @Test
    @DisplayName("終了日なしで期間内の日付はtrueを返す")
    void shouldReturnTrueForDateWithNoEndDate() {
      ValidInterval interval = ValidInterval.of(LocalDate.of(2024, 1, 1));

      assertThat(interval.contains(LocalDate.of(2025, 6, 15))).isTrue();
    }

    @Test
    @DisplayName("null日付はfalseを返す")
    void shouldReturnFalseForNullDate() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.contains(null)).isFalse();
    }

    @Test
    @DisplayName("空のインスタンスはfalseを返す")
    void shouldReturnFalseForEmptyInterval() {
      ValidInterval interval = ValidInterval.empty();

      assertThat(interval.contains(LocalDate.of(2024, 6, 15))).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("開始日と終了日を文字列として返す")
    void shouldReturnStartAndEndAsString() {
      ValidInterval interval =
          ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(interval.toString()).isEqualTo("2024-01-01 ~ 2024-12-31");
    }

    @Test
    @DisplayName("終了日なしの場合は開始日のみを返す")
    void shouldReturnOnlyStartDateWhenNoEndDate() {
      ValidInterval interval = ValidInterval.of(LocalDate.of(2024, 1, 1));

      assertThat(interval.toString()).isEqualTo("2024-01-01 ~ ");
    }

    @Test
    @DisplayName("空のインスタンスはnullを返す")
    void shouldReturnNullForEmptyInstance() {
      ValidInterval interval = ValidInterval.empty();

      assertThat(interval.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      ValidInterval first = ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
      ValidInterval second = ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      ValidInterval first = ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
      ValidInterval second = ValidInterval.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      ValidInterval first = ValidInterval.empty();
      ValidInterval second = ValidInterval.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("ValidIntervalConverterのテスト")
  class ConverterTest {

    private final ValidInterval.ValidIntervalConverter converter =
        new ValidInterval.ValidIntervalConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からStringに変換する")
      void shouldConvertToDatabaseColumn() {
        ValidInterval interval =
            ValidInterval.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        assertThat(converter.convertToDatabaseColumn(interval))
            .isEqualTo("2024-01-01 ~ 2024-12-31");
      }

      @Test
      @DisplayName("null属性はnullを返す")
      void shouldReturnNullForNullAttribute() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }

      @Test
      @DisplayName("空の属性はnullを返す")
      void shouldReturnNullForEmptyAttribute() {
        assertThat(converter.convertToDatabaseColumn(ValidInterval.empty())).isNull();
      }

      @Test
      @DisplayName("終了日なしの属性を変換する")
      void shouldConvertWithoutEndDate() {
        ValidInterval interval = ValidInterval.of(LocalDate.of(2024, 1, 1));

        assertThat(converter.convertToDatabaseColumn(interval)).isEqualTo("2024-01-01 ~ ");
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("Stringから属性に変換する")
      void shouldConvertToEntityAttribute() {
        ValidInterval result = converter.convertToEntityAttribute("2024-01-01 ~ 2024-12-31");

        assertThat(result).isNotNull();
        assertThat(result.validFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.validTo()).isEqualTo(LocalDate.of(2024, 12, 31));
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        ValidInterval result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
      }

      @Test
      @DisplayName("空文字は空のインスタンスを返す")
      void shouldReturnEmptyForEmptyString() {
        ValidInterval result = converter.convertToEntityAttribute("");

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
      }

      @Test
      @DisplayName("終了日なしの文字列を変換する")
      void shouldConvertWithoutEndDate() {
        ValidInterval result = converter.convertToEntityAttribute("2024-01-01 ~ ");

        assertThat(result).isNotNull();
        assertThat(result.validFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.validTo()).isNull();
      }
    }
  }
}
