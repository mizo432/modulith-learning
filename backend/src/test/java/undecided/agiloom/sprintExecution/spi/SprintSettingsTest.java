package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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
        .productId(UUID.randomUUID())
        .businessDays(10)
        .endDayOfWeek(EndDayOfWeek.FRIDAY)
        .build();
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
          .productId(UUID.randomUUID())
          .build();

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

      settings.update(15, EndDayOfWeek.WEDNESDAY);

      assertThat(settings.getBusinessDays()).isEqualTo(15);
      assertThat(settings.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.WEDNESDAY);
    }

    @Test
    @DisplayName("businessDaysがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(null, EndDayOfWeek.FRIDAY))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("endDayOfWeekがnullの場合例外がスローされる")
    void shouldThrowExceptionWhenEndDayOfWeekIsNull() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(10, null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("businessDaysが0の場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysIsZero() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(0, EndDayOfWeek.FRIDAY))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("businessDaysが31の場合例外がスローされる")
    void shouldThrowExceptionWhenBusinessDaysExceeds30() {
      SprintSettings settings = createSettings();

      assertThatThrownBy(() -> settings.update(31, EndDayOfWeek.FRIDAY))
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
          .productId(UUID.randomUUID())
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

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
          .productId(UUID.randomUUID())
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      Set<LocalDate> holidays = new HashSet<>();
      holidays.add(LocalDate.of(2026, 11, 19)); // 水曜日が祝日

      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 祝日を除外して5営業日計算
      assertThat(endDate).isAfter(startDate);
    }

    @Test
    @DisplayName("終了曜日が調整される")
    void shouldAdjustToEndDayOfWeek() {
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(5L))
          .productId(UUID.randomUUID())
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .build();

      Set<LocalDate> holidays = new HashSet<>();
      LocalDate startDate = LocalDate.of(2026, 11, 16); // Monday

      LocalDate endDate = settings.calculateEndDate(startDate, holidays);

      // 3営業日後は水曜日またはその次の水曜日
      assertThat(endDate.getDayOfWeek().getValue()).isEqualTo(3); // Wednesday = 3
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
          .productId(UUID.randomUUID())
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

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
          .productId(UUID.randomUUID())
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

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
          .productId(UUID.randomUUID())
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

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
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteMethodTest {

    @Test
    @DisplayName("論理削除が実行される")
    void shouldSoftDelete() {
      SprintSettings settings = createSettings();

      settings.delete();

      assertThat(settings.getDeletedAt()).isNotNull();
    }
  }
}
