package undecided.generic.calendarReg.internal.batch.writer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.spi.Holiday;

@Tag("small")
@DisplayName("HolidayItemWriterのテスト")
class HolidayItemWriterTest {

  private final HolidayRepository repository = mock(HolidayRepository.class);
  private final HolidayItemWriter writer = new HolidayItemWriter(repository);

  @Nested
  @DisplayName("writeメソッドのテスト")
  class WriteTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenChunkIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> writer.write(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("チャンク内のエンティティがRepositoryを介して一括保存されること")
    void shouldSaveAllItemsInChunk() {
      // Arrange
      Holiday h1 = new Holiday();
      h1.setId(20240101L);
      h1.setHolidayDate(LocalDate.of(2024, 1, 1));
      h1.setHolidayName("元日");

      Holiday h2 = new Holiday();
      h2.setId(20240108L);
      h2.setHolidayDate(LocalDate.of(2024, 1, 8));
      h2.setHolidayName("成人の日");

      List<Holiday> list = List.of(h1, h2);
      Chunk<Holiday> chunk = new Chunk<>(list);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository).saveAll(list);
    }
  }
}
