package undecided.generic.calendarReg.internal.batch.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.core.io.ByteArrayResource;
import undecided.generic.calendarReg.internal.batch.dto.HolidayCsvDto;

@Tag("small")
@DisplayName("HolidayCsvItemReaderのテスト")
class HolidayCsvItemReaderTest {

  @Nested
  @DisplayName("openおよびreadメソッドのテスト")
  class OpenAndReadTest {

    @Test
    @DisplayName("デジタル庁標準の日本語ヘッダーCSVを正しく読み込めること")
    void shouldReadJapaneseHeaderCsvCorrectly() throws Exception {
      // Arrange
      String csvContent =
          """
          国民の祝日・休日月日,国民の祝日・休日名称
          2024/01/01,元日
          2024/01/08,成人の日
          2024/02/11,建国記念の日
          """;
      ByteArrayResource resource =
          new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
      HolidayCsvItemReader reader = new HolidayCsvItemReader(resource);
      ExecutionContext executionContext = new ExecutionContext();

      // Act
      reader.open(executionContext);
      HolidayCsvDto item1 = reader.read();
      HolidayCsvDto item2 = reader.read();
      HolidayCsvDto item3 = reader.read();
      HolidayCsvDto item4 = reader.read();
      reader.close();

      // Assert
      assertThat(item1).isNotNull();
      assertThat(item1.getHolidayDate()).isEqualTo("2024/01/01");
      assertThat(item1.getHolidayName()).isEqualTo("元日");
      assertThat(item1.getRemarks()).isNull();

      assertThat(item2).isNotNull();
      assertThat(item2.getHolidayDate()).isEqualTo("2024/01/08");
      assertThat(item2.getHolidayName()).isEqualTo("成人の日");

      assertThat(item3).isNotNull();
      assertThat(item3.getHolidayDate()).isEqualTo("2024/02/11");
      assertThat(item3.getHolidayName()).isEqualTo("建国記念の日");

      assertThat(item4).isNull();
    }

    @Test
    @DisplayName("英語ヘッダーおよび備考付きのCSVを正しく読み込めること")
    void shouldReadEnglishHeaderCsvWithRemarksCorrectly() throws Exception {
      // Arrange
      String csvContent =
          """
          holidayDate,holidayName,remarks
          2024-05-03,憲法記念日,ゴールデンウィーク
          """;
      ByteArrayResource resource =
          new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
      HolidayCsvItemReader reader = new HolidayCsvItemReader(resource);
      ExecutionContext executionContext = new ExecutionContext();

      // Act
      reader.open(executionContext);
      HolidayCsvDto item = reader.read();
      HolidayCsvDto eof = reader.read();
      reader.close();

      // Assert
      assertThat(item).isNotNull();
      assertThat(item.getHolidayDate()).isEqualTo("2024-05-03");
      assertThat(item.getHolidayName()).isEqualTo("憲法記念日");
      assertThat(item.getRemarks()).isEqualTo("ゴールデンウィーク");

      assertThat(eof).isNull();
    }

    @Test
    @DisplayName("リソースがnullの場合、open時にItemStreamExceptionをスローすること")
    void shouldThrowItemStreamExceptionWhenResourceIsNull() {
      // Arrange
      HolidayCsvItemReader reader = new HolidayCsvItemReader(null);
      ExecutionContext executionContext = new ExecutionContext();

      // Act & Assert
      assertThatThrownBy(() -> reader.open(executionContext))
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Resource must not be null");
    }

    @Test
    @DisplayName("open前にreadを呼び出した場合、ItemStreamExceptionをスローすること")
    void shouldThrowItemStreamExceptionWhenReadBeforeOpen() {
      // Arrange
      HolidayCsvItemReader reader = new HolidayCsvItemReader();

      // Act & Assert
      assertThatThrownBy(reader::read)
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }
  }
}
