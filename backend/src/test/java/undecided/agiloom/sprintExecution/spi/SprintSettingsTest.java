package undecided.agiloom.sprintExecution.spi;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintSettingsのテスト")
class SprintSettingsTest {

  private SprintSettings createSettings() {
    return SprintSettings.builder()
        .id(SnowflakeId.of(1L))
        .productId(SnowflakeId.of(100L))
        .businessDays(10)
        .endDayOfWeek(EndDayOfWeek.FRIDAY)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      SprintSettings settings = createSettings();

      assertThat(settings).isNotNull();
      assertThat(settings.getBusinessDays()).isEqualTo(10);
      assertThat(settings.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
    }

    @Test
    @DisplayName("デフォルト値が設定される")
    void shouldHaveDefaultValues() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2L))
          .productId(SnowflakeId.of(100L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      assertThat(settings.getBusinessDays()).isEqualTo(10);
      assertThat(settings.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("営業日数と終了曜日が更新される")
    void shouldUpdateBusinessDaysAndEndDayOfWeek() {
      SprintSettings settings = createSettings();

      settings.update(15, EndDayOfWeek.WEDNESDAY, LocalDateTime.now());

      assertThat(settings.getBusinessDays()).isEqualTo(15);
      assertThat(settings.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.WEDNESDAY);
    }

    @Test
    @DisplayName("businessDaysがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(null, EndDayOfWeek.FRIDAY, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("endDayOfWeekがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenEndDayOfWeekIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(10, null, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("businessDaysが0の場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysIsZero() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(0, EndDayOfWeek.FRIDAY, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("businessDaysが31の場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysExceeds30() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(31, EndDayOfWeek.FRIDAY, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("calculateEndDateメソッドのテスト")
  class CalculateEndDateTest {

    @Test
    @DisplayName("10営業日後の金曜日が計算される")
    void shouldCalculateEndDateWith10BusinessDays() {
      // 2026-11-16 (月) から開始
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(3L))
          .productId(SnowflakeId.of(100L))
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 10営業日後は2026-12-04(金)
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 12, 4));
    }

    @Test
    @DisplayName("祝日が除外されて計算される")
    void shouldExcludeHolidaysFromCalculation() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(4L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 11, 19)); // 水曜日が祝日

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 5営業日: 11/16(月)=1, 11/17(火)=2, 11/19(水)は祝日, 11/20(金)=3, 11/23(月)=4, 11/24(火)=5
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("終了曜日が調整される")
    void shouldAdjustToEndDayOfWeek() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(5L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日: 11/17(火)=1, 11/18(水)=2, 11/19(木)=3
      // 終了曜日が水曜日なので、11/25(水)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 25));
    }

    @Test
    @DisplayName("startDateがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenStartDateIsNull() {
      SprintSettings settings = createSettings();
      Set<LocalDate> holidays = new HashSet<>();

      assertThatThrownBy(() -> settings.calculateEndDate(null, holidays))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("holidaysがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenHolidaysIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.calculateEndDate(LocalDate.now(), null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("年末年始の連続祝日を跨ぐ場合の計算が正しく行われる")
    void shouldCalculateAcrossYearEndHolidays() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(6L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 12, 29)); // 火
      holidays.add(LocalDate.of(2026, 12, 30)); // 水
      holidays.add(LocalDate.of(2026, 12, 31)); // 木
      holidays.add(LocalDate.of(2027, 1, 1));   // 金
      holidays.add(LocalDate.of(2027, 1, 4));   // 月 (振替休日)

      LocalDate startDate = LocalDate.of(2026, 12, 28); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 5営業日: 12/28(月)=1, 1/5(月)=2, 1/6(火)=3, 1/7(水)=4, 1/8(木)=5
      // 終了曜日が金曜日なので、1/15(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2027, 1, 15));
    }

    @Test
    @DisplayName("祝日が土曜日と重なる場合、影響がない")
    void shouldHandleHolidayOnSaturday() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(7L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 11, 21)); // Saturday (祝日)

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日: 11/16(月), 11/17(火), 11/18(水) = 3営業目
      // 終了曜日が金曜日なので、11/20(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    @Test
    @DisplayName("連続祝日(3日以上)が含まれる場合の計算が正しく行われる")
    void shouldHandleConsecutiveHolidays() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(8L))
          .productId(SnowflakeId.of(100L))
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 11, 17)); // 火
      holidays.add(LocalDate.of(2026, 11, 18)); // 水
      holidays.add(LocalDate.of(2026, 11, 19)); // 木

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 5営業日: 11/16(月)=1, 11/20(金)=2, 11/23(月)=3, 11/24(火)=4, 11/25(水)=5
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("businessDaysが1の場合、最小の期間が計算される")
    void shouldCalculateWithOneBusinessDay() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(9L))
          .productId(SnowflakeId.of(100L))
          .businessDays(1)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 1営業日: 11/17(火)=1
      // 終了曜日が金曜日なので、11/20(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    @Test
    @DisplayName("businessDaysが30の場合、最大の期間が計算される")
    void shouldCalculateWith30BusinessDays() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(10L))
          .productId(SnowflakeId.of(100L))
          .businessDays(30)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 30営業日目: 2026-12-28(月) → 金曜日に調整: 2027-01-01(金)
      assertThat(endDate).isEqualTo(LocalDate.of(2027, 1, 1));
    }

    @Test
    @DisplayName("開始日が土曜日の場合、次の営業日から計算される")
    void shouldCalculateWhenStartDayIsSaturday() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(11L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 21); // Saturday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日: 11/23(月)=1, 11/24(火)=2, 11/25(水)=3
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("開始日が日曜日の場合、次の営業日から計算される")
    void shouldCalculateWhenStartDayIsSunday() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(12L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 22); // Sunday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日: 11/23(月)=1, 11/24(火)=2, 11/25(水)=3
      // 終了曜日が金曜日なので、11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }

    @Test
    @DisplayName("終了曜日調整時に祝日を除外する")
    void shouldExcludeHolidaysWhenAdjustingToEndDayOfWeek() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(13L))
          .productId(SnowflakeId.of(100L))
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 11, 20)); // 金曜日が祝日

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日: 11/16(月)=1, 11/17(火)=2, 11/18(水)=3
      // 終了曜日が金曜日だが、11/20(金)は祝日なので除外
      // 次の金曜日11/27(金)に調整
      assertThat(endDate).isEqualTo(LocalDate.of(2026, 11, 27));
    }
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteMethodTest {

    @Test
    @DisplayName("論理削除が実行される")
    void shouldSoftDelete() {
      SprintSettings settings = createSettings();

      settings.delete(LocalDateTime.now());

      assertThat(settings.getDeletedAt()).isNotNull();
    }
  }
}
