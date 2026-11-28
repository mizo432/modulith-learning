package undecided.agiloom.sprintExecution.internal.domain;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.generic.calendarReg.spi.HolidayQuery;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintSettingsCalculatorのテスト")
class SprintSettingsCalculatorTest {

  private final HolidayQuery holidayQuery = mock(HolidayQuery.class);
  private final SprintSettingsCalculator calculator = new SprintSettingsCalculator(holidayQuery);

  private SprintSettings createSettings() {
    return SprintSettings.builder()
        .id(SnowflakeId.of(1L))
        .productId(SnowflakeId.of(100L))
        .businessDays(10)
        .endDayOfWeek(EndDayOfWeek.FRIDAY)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("calculateEndDateメソッドのテスト")
  class CalculateEndDateTest {

    @Test
    @DisplayName("settingsがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenSettingsIsNull() {
      assertThatThrownBy(() -> calculator.calculateEndDate(null, LocalDate.now()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("settings must not be null");
    }

    @Test
    @DisplayName("startDateがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenStartDateIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> calculator.calculateEndDate(settings, null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("startDate must not be null");
    }

    @Test
    @DisplayName("祝日がなく土日もない期間で10営業日後の金曜日が計算されること")
    void shouldCalculateEndDateWithoutHolidays() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2L))
          .productId(SnowflakeId.of(100L))
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // 2026-11-16(月) から開始、祝日なし
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 10営業日: 11/17(火)〜11/30(月)=10営業目 → 終了曜日が金曜日なので12/4(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 12, 4));
    }

    @Test
    @DisplayName("祝日がある場合、祝日を除外して営業日計算されること")
    void shouldExcludeHolidaysFromCalculation() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(3L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday
      Holiday holiday = new Holiday();
      holiday.setId(20261120L);
      holiday.setHolidayDate(LocalDate.of(2026, 11, 20)); // Friday (祝日)
      holiday.setHolidayName("振替休日");
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of(holiday));

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 5営業日: 11/16(月), 11/17(火), 11/18(水), 11/19(木) = 4営業日
      // 11/20(金)は祝日なので除外 → 11/23(月) = 5営業目
      // 終了曜日が金曜日なので、次の金曜日(11/27)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("HolidayQuery.findBetweenが正しく呼ばれること")
    void shouldCallHolidayQueryFindBetween() {
      // Arrange
      SprintSettings settings = createSettings();
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      calculator.calculateEndDate(settings, startDate);

      // Assert
      verify(holidayQuery).findBetween(startDate, startDate.plusDays(90));
    }

    @Test
    @DisplayName("終了曜日が水曜日の場合、水曜日に調整されること")
    void shouldAdjustToWednesday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(4L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 3営業日: 11/17(火), 11/18(水), 11/19(木) = 3営業目
      // 終了曜日が水曜日なので、次の水曜日(11/25)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 25));
    }

    @Test
    @DisplayName("年末年始の連続祝日を跨ぐ場合の計算が正しく行われること")
    void shouldCalculateAcrossYearEndHolidays() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(5L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 12, 28); // Monday
      // 年末年始の連続祝日: 12/29(火), 12/30(水), 12/31(木), 1/1(金), 1/2(土), 1/3(日)
      List<Holiday> holidays = List.of(
          createHoliday(2026, 12, 29, "年末"),
          createHoliday(2026, 12, 30, "年末"),
          createHoliday(2026, 12, 31, "大晦日"),
          createHoliday(2027, 1, 1, "元日"),
          createHoliday(2027, 1, 4, "振替休日")
      );
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(holidays);

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 5営業日: 12/28(月)=1, 1/5(月)=2, 1/6(火)=3, 1/7(水)=4, 1/8(木)=5
      // 終了曜日が金曜日なので、1/15(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2027, 1, 15));
    }

    @Test
    @DisplayName("祝日が土曜日と重なる場合、土日は本来除外されるので影響がないこと")
    void shouldHandleHolidayOnSaturday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(6L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday
      // 土曜日が祝日(海の日など)
      Holiday holiday = new Holiday();
      holiday.setId(20261121L);
      holiday.setHolidayDate(LocalDate.of(2026, 11, 21)); // Saturday
      holiday.setHolidayName("海の日");
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of(holiday));

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 3営業日: 11/16(月), 11/17(火), 11/18(水) = 3営業目
      // 終了曜日が金曜日なので、11/20(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    @Test
    @DisplayName("連続祝日(3日以上)が含まれる場合の計算が正しく行われること")
    void shouldHandleConsecutiveHolidays() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(7L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday
      // 連続祝日: 11/17(火), 11/18(水), 11/19(木)
      List<Holiday> holidays = List.of(
          createHoliday(2026, 11, 17, "祝日1"),
          createHoliday(2026, 11, 18, "祝日2"),
          createHoliday(2026, 11, 19, "祝日3")
      );
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(holidays);

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 5営業日: 11/16(月)=1, 11/20(金)=2, 11/23(月)=3, 11/24(火)=4, 11/25(水)=5
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("祝日データが空の場合でも正常に計算されること")
    void shouldCalculateWithEmptyHolidayList() {
      // Arrange
      SprintSettings settings = createSettings();
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 10営業日: 11/17(火)〜11/30(月)=10営業目 → 終了曜日が金曜日なので12/4(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 12, 4));
    }

    @Test
    @DisplayName("1営業日のみの場合、次の営業日が終了日になること")
    void shouldCalculateWithOneBusinessDay() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(8L))
          .productId(SnowflakeId.of(100L))
          .businessDays(1)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 1営業日: 11/17(火) = 1営業目
      // 終了曜日が金曜日なので、11/20(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    @Test
    @DisplayName("開始日が金曜日で終了曜日も金曜日の場合、次の金曜日が終了日になること")
    void shouldCalculateWhenStartAndEndDayAreFriday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(9L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      LocalDate startDate = LocalDate.of(2026, 11, 20); // Friday
      when(holidayQuery.findBetween(startDate, startDate.plusDays(90))).thenReturn(List.of());

      // Act
      LocalDate endDate = calculator.calculateEndDate(settings, startDate);

      // Assert
      // 5営業日: 11/23(月), 11/24(火), 11/25(水), 11/26(木), 11/27(金) = 5営業目
      // 終了曜日が金曜日なので、11/27(金)でOK
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }
  }

  private Holiday createHoliday(int year, int month, int day, String name) {
    Holiday holiday = new Holiday();
    holiday.setId((long) (year * 10000 + month * 100 + day));
    holiday.setHolidayDate(LocalDate.of(year, month, day));
    holiday.setHolidayName(name);
    return holiday;
  }
}
