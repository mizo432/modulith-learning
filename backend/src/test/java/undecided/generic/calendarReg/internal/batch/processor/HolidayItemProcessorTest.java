package undecided.generic.calendarReg.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.internal.batch.dto.HolidayCsvDto;
import undecided.generic.calendarReg.spi.Holiday;

@Tag("small")
@DisplayName("HolidayItemProcessorのテスト")
class HolidayItemProcessorTest {

  private final HolidayRepository repository = mock(HolidayRepository.class);
  private final HolidayItemProcessor processor = new HolidayItemProcessor(repository);

  @Nested
  @DisplayName("processメソッドのテスト")
  class ProcessTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInputIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> processor.process(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("holidayDateが無効またはパースできない場合、nullを返すこと")
    void shouldReturnNullWhenHolidayDateIsInvalid() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto("invalid-date", "元日", null);

      // Act
      Holiday result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("holidayNameがnullまたは空白の場合、nullを返すこと")
    void shouldReturnNullWhenHolidayNameIsBlank() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto("2024/01/01", "   ", null);

      // Act
      Holiday result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("既存レコードが存在しない場合、新規エンティティを生成すること")
    void shouldCreateNewEntityWhenRecordDoesNotExist() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 1);
      HolidayCsvDto dto = new HolidayCsvDto("2024/01/01", "元日", "国民の祝日");
      when(repository.findByHolidayDate(date)).thenReturn(Optional.empty());

      // Act
      Holiday result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(20240101L);
      assertThat(result.getHolidayDate()).isEqualTo(date);
      assertThat(result.getHolidayName()).isEqualTo("元日");
      assertThat(result.getRemarks()).isEqualTo("国民の祝日");
    }

    @Test
    @DisplayName("既存レコードが存在する場合、既存エンティティの値を更新すること")
    void shouldUpdateExistingEntityWhenRecordExists() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 8);
      Holiday existing = new Holiday();
      existing.setId(20240108L);
      existing.setHolidayDate(date);
      existing.setHolidayName("旧成人の日");
      existing.setRemarks(null);

      HolidayCsvDto dto = new HolidayCsvDto("2024/01/08", "成人の日", "ハッピーマンデー");
      when(repository.findByHolidayDate(date)).thenReturn(Optional.of(existing));

      // Act
      Holiday result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(20240108L);
      assertThat(result.getHolidayDate()).isEqualTo(date);
      assertThat(result.getHolidayName()).isEqualTo("成人の日");
      assertThat(result.getRemarks()).isEqualTo("ハッピーマンデー");
    }
  }
}
