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

      // 10営業日後は2026-11-30(月)→次の金曜日に調整→2026-12-04(金)
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
