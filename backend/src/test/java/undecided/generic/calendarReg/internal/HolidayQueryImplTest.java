package undecided.generic.calendarReg.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.calendarReg.spi.Holiday;

@Tag("small")
@DisplayName("HolidayQueryImplのテスト")
class HolidayQueryImplTest {

  private final HolidayRepository repository = mock(HolidayRepository.class);
  private final HolidayQueryImpl query = new HolidayQueryImpl(repository);

  @Nested
  @DisplayName("findByHolidayDateメソッドのテスト")
  class FindByHolidayDateTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDateIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByHolidayDate(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("存在する日付の場合、該当するHolidayを返すこと")
    void shouldReturnHolidayWhenExists() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 1);
      Holiday holiday = new Holiday();
      holiday.setId(20240101L);
      holiday.setHolidayDate(date);
      holiday.setHolidayName("元日");
      when(repository.findByHolidayDate(date)).thenReturn(Optional.of(holiday));

      // Act
      Optional<Holiday> result = query.findByHolidayDate(date);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getHolidayName()).isEqualTo("元日");
    }

    @Test
    @DisplayName("存在しない日付の場合、空のOptionalを返すこと")
    void shouldReturnEmptyWhenNotExists() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 2);
      when(repository.findByHolidayDate(date)).thenReturn(Optional.empty());

      // Act
      Optional<Holiday> result = query.findByHolidayDate(date);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findByYearメソッドのテスト")
  class FindByYearTest {

    @Test
    @DisplayName("指定した年の1月1日から12月31日までの祝日リストを取得できること")
    void shouldReturnHolidaysInGivenYear() {
      // Arrange
      LocalDate from = LocalDate.of(2024, 1, 1);
      LocalDate to = LocalDate.of(2024, 12, 31);
      Holiday h1 = new Holiday();
      h1.setId(20240101L);
      h1.setHolidayDate(from);
      h1.setHolidayName("元日");

      when(repository.findBetween(from, to)).thenReturn(List.of(h1));

      // Act
      List<Holiday> result = query.findByYear(2024);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getHolidayName()).isEqualTo("元日");
    }
  }

  @Nested
  @DisplayName("findBetweenメソッドのテスト")
  class FindBetweenTest {

    @Test
    @DisplayName("fromがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenFromIsNull() {
      assertThatThrownBy(() -> query.findBetween(null, LocalDate.of(2024, 1, 1)))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenToIsNull() {
      assertThatThrownBy(() -> query.findBetween(LocalDate.of(2024, 1, 1), null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("期間内の祝日リストを取得できること")
    void shouldReturnHolidaysBetweenDates() {
      // Arrange
      LocalDate from = LocalDate.of(2024, 1, 1);
      LocalDate to = LocalDate.of(2024, 1, 31);
      Holiday h1 = new Holiday();
      h1.setId(20240101L);
      h1.setHolidayDate(from);
      h1.setHolidayName("元日");

      when(repository.findBetween(from, to)).thenReturn(List.of(h1));

      // Act
      List<Holiday> result = query.findBetween(from, to);

      // Assert
      assertThat(result).containsExactly(h1);
    }
  }
}
