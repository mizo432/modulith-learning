package com.github.mizo432.shared.common.value.datetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ValidIntervalの単体テスト")
class ValidIntervalTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("引数なしコンストラクタでnull値のインスタンスが作成されること")
    void shouldCreateInstanceWithNullValues() {
      // Act
      ValidInterval interval = new ValidInterval();

      // Assert
      assertThat(interval.validFrom()).isNull();
      assertThat(interval.validTo()).isNull();
    }
  }

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateMethodTest {

    @Test
    @DisplayName("有効開始日と有効終了日で正常に作成されること")
    void shouldCreateWithValidFromAndValidTo() {
      // Arrange
      LocalDate validFrom = LocalDate.of(2024, 1, 1);
      LocalDate validTo = LocalDate.of(2024, 12, 31);

      // Act
      ValidInterval interval = ValidInterval.create(validFrom, validTo);

      // Assert
      assertThat(interval.validFrom()).isEqualTo(validFrom);
      assertThat(interval.validTo()).isEqualTo(validTo);
    }

    @Test
    @DisplayName("有効開始日のみで正常に作成されること")
    void shouldCreateWithValidFromOnly() {
      // Arrange
      LocalDate validFrom = LocalDate.of(2024, 1, 1);

      // Act
      ValidInterval interval = ValidInterval.create(validFrom);

      // Assert
      assertThat(interval.validFrom()).isEqualTo(validFrom);
      assertThat(interval.validTo()).isNull();
    }

    @Test
    @DisplayName("有効開始日と有効終了日が同じ日で正常に作成されること")
    void shouldCreateWhenValidFromEqualsValidTo() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 6, 15);

      // Act
      ValidInterval interval = ValidInterval.create(date, date);

      // Assert
      assertThat(interval.validFrom()).isEqualTo(date);
      assertThat(interval.validTo()).isEqualTo(date);
    }

    @Test
    @DisplayName("有効開始日がnullの場合例外がスローされること")
    void shouldThrowExceptionWhenValidFromIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> ValidInterval.create(null, LocalDate.of(2024, 12, 31)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validFrom must not be null");
    }

    @Test
    @DisplayName("有効終了日が有効開始日より前の場合例外がスローされること")
    void shouldThrowExceptionWhenValidToIsBeforeValidFrom() {
      // Arrange
      LocalDate validFrom = LocalDate.of(2024, 12, 31);
      LocalDate validTo = LocalDate.of(2024, 1, 1);

      // Act & Assert
      assertThatThrownBy(() -> ValidInterval.create(validFrom, validTo))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validTo must not be before validFrom");
    }

    @Test
    @DisplayName("create(LocalDate)でvalidFromがnullの場合例外がスローされること")
    void shouldThrowExceptionWhenValidFromIsNullWithSingleArg() {
      // Act & Assert
      assertThatThrownBy(() -> ValidInterval.create((LocalDate) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("validFrom must not be null");
    }
  }

  @Nested
  @DisplayName("empty メソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが返されること")
    void shouldReturnEmptyInstance() {
      // Act
      ValidInterval interval = ValidInterval.empty();

      // Assert
      assertThat(interval.validFrom()).isNull();
      assertThat(interval.validTo()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmpty メソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスではtrueを返すこと")
    void shouldReturnTrueForEmptyInstance() {
      // Arrange
      ValidInterval interval = ValidInterval.empty();

      // Act & Assert
      assertThat(interval.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスではfalseを返すこと")
    void shouldReturnFalseForNonEmptyInstance() {
      // Arrange
      ValidInterval interval = ValidInterval.create(LocalDate.of(2024, 1, 1));

      // Act & Assert
      assertThat(interval.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("contains メソッドのテスト")
  class ContainsMethodTest {

    @Test
    @DisplayName("期間内の日付が渡された場合trueを返すこと")
    void shouldReturnTrueWhenDateIsWithinInterval() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2024, 6, 15))).isTrue();
    }

    @Test
    @DisplayName("開始日が渡された場合trueを返すこと")
    void shouldReturnTrueWhenDateIsStartDate() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2024, 1, 1))).isTrue();
    }

    @Test
    @DisplayName("終了日が渡された場合trueを返すこと")
    void shouldReturnTrueWhenDateIsEndDate() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2024, 12, 31))).isTrue();
    }

    @Test
    @DisplayName("開始日より前の日付が渡された場合falseを返すこと")
    void shouldReturnFalseWhenDateIsBeforeStart() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2023, 12, 31))).isFalse();
    }

    @Test
    @DisplayName("終了日より後の日付が渡された場合falseを返すこと")
    void shouldReturnFalseWhenDateIsAfterEnd() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2025, 1, 1))).isFalse();
    }

    @Test
    @DisplayName("有効終了日がない場合、開始日以降はtrueを返すこと")
    void shouldReturnTrueForDateAfterStartWhenNoEndDate() {
      // Arrange
      ValidInterval interval = ValidInterval.create(LocalDate.of(2024, 1, 1));

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2025, 6, 15))).isTrue();
    }

    @Test
    @DisplayName("null日付が渡された場合falseを返すこと")
    void shouldReturnFalseWhenDateIsNull() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.contains(null)).isFalse();
    }

    @Test
    @DisplayName("空のインスタンスではfalseを返すこと")
    void shouldReturnFalseForEmptyInstance() {
      // Arrange
      ValidInterval interval = ValidInterval.empty();

      // Act & Assert
      assertThat(interval.contains(LocalDate.of(2024, 6, 15))).isFalse();
    }
  }

  @Nested
  @DisplayName("isActive メソッドのテスト")
  class IsActiveMethodTest {

    @Test
    @DisplayName("現在が有効期間内の場合trueを返すこと")
    void shouldReturnTrueWhenCurrentlyActive() {
      // Arrange
      LocalDate today = LocalDate.now();
      ValidInterval interval = ValidInterval.create(today.minusDays(1), today.plusDays(1));

      // Act & Assert
      assertThat(interval.isActive()).isTrue();
    }

    @Test
    @DisplayName("現在が有効期間外の場合falseを返すこと")
    void shouldReturnFalseWhenNotActive() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(
              LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31));

      // Act & Assert
      assertThat(interval.isActive()).isFalse();
    }

    @Test
    @DisplayName("空のインスタンスではfalseを返すこと")
    void shouldReturnFalseForEmptyInstance() {
      // Arrange
      ValidInterval interval = ValidInterval.empty();

      // Act & Assert
      assertThat(interval.isActive()).isFalse();
    }
  }

  @Nested
  @DisplayName("toString メソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("有効開始日と有効終了日を持つ場合の文字列表現が正しく返されること")
    void shouldReturnCorrectStringRepresentationWithBothDates() {
      // Arrange
      ValidInterval interval =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval.toString()).isEqualTo("2024-01-01 ~ 2024-12-31");
    }

    @Test
    @DisplayName("有効終了日がない場合の文字列表現が正しく返されること")
    void shouldReturnCorrectStringRepresentationWithoutEndDate() {
      // Arrange
      ValidInterval interval = ValidInterval.create(LocalDate.of(2024, 1, 1));

      // Act & Assert
      assertThat(interval.toString()).isEqualTo("2024-01-01 ~ ");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返すこと")
    void shouldReturnNullForEmptyInstance() {
      // Arrange
      ValidInterval interval = ValidInterval.empty();

      // Act & Assert
      assertThat(interval.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equals と hashCode のテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      ValidInterval interval1 =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
      ValidInterval interval2 =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

      // Act & Assert
      assertThat(interval1).isEqualTo(interval2);
      assertThat(interval1.hashCode()).isEqualTo(interval2.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくないこと")
    void shouldBeNotEqualWhenValuesDiffer() {
      // Arrange
      ValidInterval interval1 =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
      ValidInterval interval2 =
          ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 12, 31));

      // Act & Assert
      assertThat(interval1).isNotEqualTo(interval2);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しいこと")
    void shouldBeEqualWhenBothEmpty() {
      // Arrange
      ValidInterval interval1 = ValidInterval.empty();
      ValidInterval interval2 = ValidInterval.empty();

      // Act & Assert
      assertThat(interval1).isEqualTo(interval2);
    }
  }

  @Nested
  @DisplayName("ValidIntervalConverter のテスト")
  class ConverterTest {

    private final ValidInterval.ValidIntervalConverter converter =
        new ValidInterval.ValidIntervalConverter();

    @Nested
    @DisplayName("convertToDatabaseColumn メソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("正常に文字列に変換されること")
      void shouldConvertToDatabaseColumnSuccessfully() {
        // Arrange
        ValidInterval interval =
            ValidInterval.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        // Act
        String actual = converter.convertToDatabaseColumn(interval);

        // Assert
        assertThat(actual).isEqualTo("2024-01-01 ~ 2024-12-31");
      }

      @Test
      @DisplayName("有効終了日がない場合の文字列変換が正しく行われること")
      void shouldConvertWithoutEndDate() {
        // Arrange
        ValidInterval interval = ValidInterval.create(LocalDate.of(2024, 1, 1));

        // Act
        String actual = converter.convertToDatabaseColumn(interval);

        // Assert
        assertThat(actual).isEqualTo("2024-01-01 ~ ");
      }

      @Test
      @DisplayName("null属性が渡された場合nullを返すこと")
      void shouldReturnNullWhenAttributeIsNull() {
        // Act & Assert
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }

      @Test
      @DisplayName("空のインスタンスが渡された場合nullを返すこと")
      void shouldReturnNullWhenAttributeIsEmpty() {
        // Arrange
        ValidInterval interval = ValidInterval.empty();

        // Act & Assert
        assertThat(converter.convertToDatabaseColumn(interval)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttribute メソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("正常にValidIntervalに変換されること")
      void shouldConvertToEntityAttributeSuccessfully() {
        // Arrange
        String dbData = "2024-01-01 ~ 2024-12-31";

        // Act
        ValidInterval actual = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(actual.validFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(actual.validTo()).isEqualTo(LocalDate.of(2024, 12, 31));
      }

      @Test
      @DisplayName("有効終了日がない文字列が正常に変換されること")
      void shouldConvertWithoutEndDate() {
        // Arrange
        String dbData = "2024-01-01 ~ ";

        // Act
        ValidInterval actual = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(actual.validFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(actual.validTo()).isNull();
      }

      @Test
      @DisplayName("nullデータが渡された場合空のインスタンスを返すこと")
      void shouldReturnEmptyWhenDataIsNull() {
        // Act & Assert
        ValidInterval actual = converter.convertToEntityAttribute(null);
        assertThat(actual.isEmpty()).isTrue();
      }

      @Test
      @DisplayName("空文字列が渡された場合空のインスタンスを返すこと")
      void shouldReturnEmptyWhenDataIsEmpty() {
        // Act & Assert
        ValidInterval actual = converter.convertToEntityAttribute("");
        assertThat(actual.isEmpty()).isTrue();
      }
    }
  }
}
