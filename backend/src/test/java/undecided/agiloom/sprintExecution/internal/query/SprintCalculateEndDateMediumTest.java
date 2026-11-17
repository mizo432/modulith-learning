package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("スプリント終了日計算の統合テスト")
class SprintCalculateEndDateMediumTest {

  @Autowired
  private SprintSettingsRepository sprintSettingsRepository;

  @Autowired
  private HolidayRepository holidayRepository;

  @Autowired
  private SprintSettingsQueryService sprintSettingsQueryService;

  private UUID productId;

  @BeforeEach
  void setUp() {
    sprintSettingsRepository.deleteAll();
    holidayRepository.deleteAll();
    productId = UUID.randomUUID();
  }

  @Nested
  @DisplayName("祝日なしでの終了日計算のテスト")
  class CalculateEndDateWithoutHolidaysTest {

    @Test
    @DisplayName("10営業日、金曜日終了で正しく計算されること")
    void shouldCalculateEndDateWith10BusinessDaysEndingFriday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1001L))
          .productId(productId)
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();
      sprintSettingsRepository.save(settings);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 10営業日: 11/17(火)〜11/30(月)=10営業目 → 終了曜日が金曜日なので12/4(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 12, 4));
    }

    @Test
    @DisplayName("5営業日、水曜日終了で正しく計算されること")
    void shouldCalculateEndDateWith5BusinessDaysEndingWednesday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1002L))
          .productId(productId)
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .build();
      sprintSettingsRepository.save(settings);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 5営業日: 11/17(火), 11/18(水), 11/19(木), 11/20(金), 11/23(月) = 5営業目
      // 終了曜日が水曜日なので、11/25(水)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 25));
    }

    @Test
    @DisplayName("1営業日、金曜日終了で正しく計算されること")
    void shouldCalculateEndDateWith1BusinessDayEndingFriday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1003L))
          .productId(productId)
          .businessDays(1)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();
      sprintSettingsRepository.save(settings);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 1営業日: 11/17(火) = 1営業目
      // 終了曜日が金曜日なので、11/20(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    @Test
    @DisplayName("30営業日、金曜日終了で正しく計算されること")
    void shouldCalculateEndDateWith30BusinessDaysEndingFriday() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1004L))
          .productId(productId)
          .businessDays(30)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();
      sprintSettingsRepository.save(settings);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 30営業日後、金曜日に調整された日付が返ること
      assertThat(endDate).isAfter(startDate);
      assertThat(endDate.getDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY.getDayOfWeek());
    }
  }

  @Nested
  @DisplayName("祝日ありでの終了日計算のテスト")
  class CalculateEndDateWithHolidaysTest {

    @Test
    @DisplayName("期間中に祝日がある場合、祝日を除外して計算されること")
    void shouldExcludeHolidaysFromCalculation() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2001L))
          .productId(productId)
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();
      sprintSettingsRepository.save(settings);

      // 11/20(金)を祝日として登録
      Holiday holiday = new Holiday();
      holiday.setId(20261120L);
      holiday.setHolidayDate(LocalDate.of(2026, 11, 20));
      holiday.setHolidayName("振替休日");
      holidayRepository.save(holiday);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 5営業日: 11/16(月), 11/17(火), 11/18(水), 11/19(木) = 4営業日
      // 11/20(金)は祝日なので除外 → 11/23(月) = 5営業目
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("連続祝日がある場合、正しく除外されて計算されること")
    void shouldHandleConsecutiveHolidays() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2002L))
          .productId(productId)
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();
      sprintSettingsRepository.save(settings);

      // 連続祝日: 11/17(火), 11/18(水), 11/19(木)
      List<Holiday> holidays = List.of(
          createHoliday(2026, 11, 17, "祝日1"),
          createHoliday(2026, 11, 18, "祝日2"),
          createHoliday(2026, 11, 19, "祝日3")
      );
      holidayRepository.saveAll(holidays);

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      // Act
      LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productId, startDate);

      // Assert
      // 5営業日: 11/16(月)=1, 11/20(金)=2, 11/23(月)=3, 11/24(火)=4, 11/25(水)=5
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }
  }

  @Nested
  @DisplayName("getSettingsOrDefaultメソッドの統合テスト")
  class GetSettingsOrDefaultTest {

    @Test
    @DisplayName("設定が存在する場合、既存の設定を返すこと")
    void shouldReturnExistingSettings() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(3001L))
          .productId(productId)
          .businessDays(15)
          .endDayOfWeek(EndDayOfWeek.THURSDAY)
          .build();
      sprintSettingsRepository.save(settings);

      // Act
      SprintSettings result = sprintSettingsQueryService.getSettingsOrDefault(productId);

      // Assert
      assertThat(result.getBusinessDays()).isEqualTo(15);
      assertThat(result.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.THURSDAY);
    }

    @Test
    @DisplayName("設定が存在しない場合、デフォルト値を返すこと")
    void shouldReturnDefaultSettings() {
      // Act
      SprintSettings result = sprintSettingsQueryService.getSettingsOrDefault(productId);

      // Assert
      assertThat(result.getBusinessDays()).isEqualTo(10);
      assertThat(result.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
      assertThat(result.getProductId()).isEqualTo(productId);
    }
  }

  @Nested
  @DisplayName("例外ケースのテスト")
  class ExceptionTest {

    @Test
    @DisplayName("設定が存在しない場合、calculateEndDateは例外をスローすること")
    void shouldThrowExceptionWhenSettingsNotFound() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);

      // Act & Assert
      assertThatThrownBy(() -> sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint settings not found for product");
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
