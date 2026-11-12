package com.github.mizo432.shared.common.dateProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.YearMonth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("DateProviderの単体テスト")
class DateProviderTest {

  @AfterEach
  void tearDown() {
    DateProvider.clear();
  }

  @Nested
  @DisplayName("currentLocalDateTime メソッドのテスト")
  class CurrentLocalDateTimeTest {

    @Test
    @DisplayName("現在の日時が返されること")
    void shouldReturnCurrentLocalDateTime() {
      // Act
      LocalDateTime dateTime = DateProvider.currentLocalDateTime();

      // Assert
      assertThat(dateTime).isNotNull();
    }

    @Test
    @DisplayName("固定日時プロバイダー設定後、固定された日時が返されること")
    void shouldReturnFixedDateTimeWhenProviderIsSet() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      LocalDateTime dateTime = DateProvider.currentLocalDateTime();

      // Assert
      assertThat(dateTime).isEqualTo(fixedDateTime);
    }
  }

  @Nested
  @DisplayName("currentLocalDate メソッドのテスト")
  class CurrentLocalDateTest {

    @Test
    @DisplayName("現在の日付が返されること")
    void shouldReturnCurrentLocalDate() {
      // Act
      LocalDate date = DateProvider.currentLocalDate();

      // Assert
      assertThat(date).isNotNull();
    }

    @Test
    @DisplayName("固定日時プロバイダー設定後、固定された日付が返されること")
    void shouldReturnFixedDateWhenProviderIsSet() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      LocalDate date = DateProvider.currentLocalDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(2024, 6, 15));
    }
  }

  @Nested
  @DisplayName("currentLocalTime メソッドのテスト")
  class CurrentLocalTimeTest {

    @Test
    @DisplayName("現在の時間が返されること")
    void shouldReturnCurrentLocalTime() {
      // Act
      LocalTime time = DateProvider.currentLocalTime();

      // Assert
      assertThat(time).isNotNull();
    }

    @Test
    @DisplayName("固定日時プロバイダー設定後、固定された時間が返されること")
    void shouldReturnFixedTimeWhenProviderIsSet() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      LocalTime time = DateProvider.currentLocalTime();

      // Assert
      assertThat(time).isEqualTo(LocalTime.of(10, 30, 0));
    }
  }

  @Nested
  @DisplayName("currentYearMonth メソッドのテスト")
  class CurrentYearMonthTest {

    @Test
    @DisplayName("現在の年月が返されること")
    void shouldReturnCurrentYearMonth() {
      // Act
      YearMonth yearMonth = DateProvider.currentYearMonth();

      // Assert
      assertThat(yearMonth).isNotNull();
    }

    @Test
    @DisplayName("固定日時プロバイダー設定後、固定された年月が返されること")
    void shouldReturnFixedYearMonthWhenProviderIsSet() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      YearMonth yearMonth = DateProvider.currentYearMonth();

      // Assert
      assertThat(yearMonth).isEqualTo(YearMonth.of(2024, 6));
    }
  }

  @Nested
  @DisplayName("currentMonthDay メソッドのテスト")
  class CurrentMonthDayTest {

    @Test
    @DisplayName("現在の月日が返されること")
    void shouldReturnCurrentMonthDay() {
      // Act
      MonthDay monthDay = DateProvider.currentMonthDay();

      // Assert
      assertThat(monthDay).isNotNull();
    }

    @Test
    @DisplayName("固定日時プロバイダー設定後、固定された月日が返されること")
    void shouldReturnFixedMonthDayWhenProviderIsSet() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      MonthDay monthDay = DateProvider.currentMonthDay();

      // Assert
      assertThat(monthDay).isEqualTo(MonthDay.of(6, 15));
    }
  }

  @Nested
  @DisplayName("currentTimeMillis メソッドのテスト")
  class CurrentTimeMillisTest {

    @Test
    @DisplayName("現在のエポックミリ秒が返されること")
    void shouldReturnCurrentTimeMillis() {
      // Act
      long millis = DateProvider.currentTimeMillis();

      // Assert
      assertThat(millis).isPositive();
    }
  }

  @Nested
  @DisplayName("clear メソッドのテスト")
  class ClearMethodTest {

    @Test
    @DisplayName("プロバイダーがデフォルト状態にリセットされること")
    void shouldResetProviderToDefault() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);
      assertThat(DateProvider.currentLocalDateTime()).isEqualTo(fixedDateTime);

      // Act
      DateProvider.clear();

      // Assert
      assertThat(DateProvider.currentLocalDateTime()).isNotEqualTo(fixedDateTime);
    }
  }
}
