package undecided.generic.sampleBatch.internal.batch.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.core.io.ClassPathResource;
import undecided.generic.sampleBatch.internal.batch.dto.SampleItemCsvDto;

@Tag("small")
@DisplayName("SampleItemCsvItemReaderのテスト")
class SampleItemCsvItemReaderTest {

  @Nested
  @DisplayName("openメソッドのテスト")
  class OpenTest {

    @Test
    @DisplayName("リソースがnullの場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenResourceIsNull() {
      // Arrange
      SampleItemCsvItemReader reader = new SampleItemCsvItemReader();
      ExecutionContext context = new ExecutionContext();

      // Act & Assert
      assertThatThrownBy(() -> reader.open(context))
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Resource must not be null");
    }

    @Test
    @DisplayName("存在しないリソースの場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenResourceNotFound() {
      // Arrange
      SampleItemCsvItemReader reader = new SampleItemCsvItemReader(new ClassPathResource("nonexistent.csv"));
      ExecutionContext context = new ExecutionContext();

      // Act & Assert
      assertThatThrownBy(() -> reader.open(context))
          .isInstanceOf(ItemStreamException.class);
    }
  }

  @Nested
  @DisplayName("readメソッドのテスト")
  class ReadTest {

    @Test
    @DisplayName("英語ヘッダーのCSVを正常に読み込めること")
    void shouldReadCsvWithEnglishHeaders() throws Exception {
      // Arrange
      SampleItemCsvItemReader reader =
          new SampleItemCsvItemReader(new ClassPathResource("data/sample-items.csv"));
      ExecutionContext context = new ExecutionContext();
      reader.open(context);

      // Act
      List<SampleItemCsvDto> items = new ArrayList<>();
      SampleItemCsvDto item;
      while ((item = reader.read()) != null) {
        items.add(item);
      }
      reader.close();

      // Assert
      assertThat(items).hasSize(10);
      SampleItemCsvDto first = items.get(0);
      assertThat(first.getItemCode()).isEqualTo("ITEM001");
      assertThat(first.getItemName()).isEqualTo("サンプル商品A");
      assertThat(first.getCategory()).isEqualTo("食品");
      assertThat(first.getPrice()).isEqualTo("1000");
    }

    @Test
    @DisplayName("日本語ヘッダーのCSVを正常に読み込めること")
    void shouldReadCsvWithJapaneseHeaders() throws Exception {
      // Arrange
      SampleItemCsvItemReader reader =
          new SampleItemCsvItemReader(new ClassPathResource("data/sample-items-jp-header.csv"));
      ExecutionContext context = new ExecutionContext();
      reader.open(context);

      // Act
      List<SampleItemCsvDto> items = new ArrayList<>();
      SampleItemCsvDto item;
      while ((item = reader.read()) != null) {
        items.add(item);
      }
      reader.close();

      // Assert
      assertThat(items).hasSize(3);
      SampleItemCsvDto first = items.get(0);
      assertThat(first.getItemCode()).isEqualTo("ITEM001");
      assertThat(first.getItemName()).isEqualTo("サンプル商品A");
      assertThat(first.getCategory()).isEqualTo("食品");
      assertThat(first.getPrice()).isEqualTo("1000");
    }

    @Test
    @DisplayName("open前にreadを呼び出した場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenReadBeforeOpen() {
      // Arrange
      SampleItemCsvItemReader reader = new SampleItemCsvItemReader();

      // Act & Assert
      assertThatThrownBy(() -> reader.read())
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }

    @Test
    @DisplayName("境界値を含むCSVを正常に読み込めること")
    void shouldReadCsvWithEdgeCases() throws Exception {
      // Arrange
      SampleItemCsvItemReader reader =
          new SampleItemCsvItemReader(new ClassPathResource("data/sample-items-edge-cases.csv"));
      ExecutionContext context = new ExecutionContext();
      reader.open(context);

      // Act
      List<SampleItemCsvDto> items = new ArrayList<>();
      SampleItemCsvDto item;
      while ((item = reader.read()) != null) {
        items.add(item);
      }
      reader.close();

      // Assert
      assertThat(items).hasSize(5);
      // Empty item code (Super CSV returns empty string for empty fields)
      assertThat(items.get(1).getItemCode()).isBlank();
      // Empty price — ITEM003 has empty price
      assertThat(items.get(2).getPrice()).isBlank();
      // Invalid price — ITEM004 has "invalid" price
      assertThat(items.get(3).getPrice()).isEqualTo("invalid");
    }
  }

  @Nested
  @DisplayName("closeメソッドのテスト")
  class CloseTest {

    @Test
    @DisplayName("closeを呼び出しても例外をスローしないこと")
    void shouldCloseWithoutException() throws Exception {
      // Arrange
      SampleItemCsvItemReader reader =
          new SampleItemCsvItemReader(new ClassPathResource("data/sample-items.csv"));
      ExecutionContext context = new ExecutionContext();
      reader.open(context);

      // Act & Assert
      assertThatCode(() -> reader.close()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("close後にreadを呼び出した場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenReadAfterClose() throws Exception {
      // Arrange
      SampleItemCsvItemReader reader =
          new SampleItemCsvItemReader(new ClassPathResource("data/sample-items.csv"));
      ExecutionContext context = new ExecutionContext();
      reader.open(context);
      reader.close();

      // Act & Assert
      assertThatThrownBy(() -> reader.read())
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }
  }
}
