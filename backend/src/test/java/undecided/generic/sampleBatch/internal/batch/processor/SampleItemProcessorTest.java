package undecided.generic.sampleBatch.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.internal.batch.dto.SampleItemCsvDto;
import undecided.generic.sampleBatch.spi.SampleItem;

@Tag("small")
@DisplayName("SampleItemProcessorのテスト")
class SampleItemProcessorTest {

  private final SampleItemRepository repository = mock(SampleItemRepository.class);
  private final SampleItemProcessor processor = new SampleItemProcessor(repository);

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
    @DisplayName("itemCodeが空の場合、nullを返すこと")
    void shouldReturnNullWhenItemCodeIsEmpty() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("");
      dto.setItemName("テスト商品");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("itemCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenItemCodeIsNull() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode(null);
      dto.setItemName("テスト商品");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("正常なデータの場合、SampleItemエンティティを生成すること")
    void shouldCreateSampleItemWhenDataIsValid() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM001");
      dto.setItemName("サンプル商品A");
      dto.setCategory("食品");
      dto.setPrice("1000");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getItemCode()).isEqualTo("ITEM001");
      assertThat(result.getItemName()).isEqualTo("サンプル商品A");
      assertThat(result.getCategory()).isEqualTo("食品");
      assertThat(result.getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("価格が空の場合、nullとして処理すること")
    void shouldSetPriceNullWhenPriceIsEmpty() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM002");
      dto.setItemName("サンプル商品B");
      dto.setCategory("飲料");
      dto.setPrice("");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getPrice()).isNull();
    }

    @Test
    @DisplayName("カテゴリが空の場合、空文字列として処理すること")
    void shouldSetCategoryEmptyWhenCategoryIsNull() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM003");
      dto.setItemName("サンプル商品C");
      dto.setCategory(null);
      dto.setPrice("2000");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getCategory()).isEmpty();
    }

    @Test
    @DisplayName("価格が数値に変換できない場合、例外をスローすること")
    void shouldThrowExceptionWhenPriceIsInvalid() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM004");
      dto.setItemName("サンプル商品D");
      dto.setCategory("その他");
      dto.setPrice("invalid");

      // Act & Assert
      assertThatThrownBy(() -> processor.process(dto))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("空白のitemCodeはトリム後も空とみなしてnullを返すこと")
    void shouldReturnNullWhenItemCodeIsBlank() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("   ");
      dto.setItemName("テスト商品");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }
  }
}
