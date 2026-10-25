package undecided.generic.sampleBatch.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @DisplayName("価格が数値に変換できない場合、nullを返してスキップすること")
    void shouldReturnNullWhenPriceIsInvalid() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM004");
      dto.setItemName("サンプル商品D");
      dto.setCategory("その他");
      dto.setPrice("invalid");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
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

  @Nested
  @DisplayName("既存データとの整合性テスト")
  class ExistingDataTest {

    @Test
    @DisplayName("既存データ不存在の場合、新規エンティティを生成すること")
    void shouldCreateNewEntityWhenNotExists() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM001");
      dto.setItemName("サンプル商品A");
      dto.setCategory("食品");
      dto.setPrice("1000");
      when(repository.findByItemCode("ITEM001")).thenReturn(null);

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getItemCode()).isEqualTo("ITEM001");
      assertThat(result.getItemName()).isEqualTo("サンプル商品A");
      assertThat(result.getId()).isNull(); // 新規エンティティはIDがnull
    }

    @Test
    @DisplayName("既存データ存在の場合、既存エンティティを更新すること")
    void shouldUpdateExistingEntityWhenExists() {
      // Arrange
      SampleItem existing = new SampleItem();
      existing.setId(1L);
      existing.setItemCode("ITEM001");
      existing.setItemName("旧商品名");
      existing.setCategory("旧カテゴリ");
      existing.setPrice(500L);
      when(repository.findByItemCode("ITEM001")).thenReturn(existing);

      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("ITEM001");
      dto.setItemName("新商品名");
      dto.setCategory("新カテゴリ");
      dto.setPrice("1500");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isSameAs(existing);
      assertThat(result.getId()).isEqualTo(1L); // 既存のIDが保持される
      assertThat(result.getItemName()).isEqualTo("新商品名");
      assertThat(result.getCategory()).isEqualTo("新カテゴリ");
      assertThat(result.getPrice()).isEqualTo(1500L);
    }

    @Test
    @DisplayName("itemCodeのトリム後の値で既存データを検索すること")
    void shouldSearchExistingDataWithTrimmedItemCode() {
      // Arrange
      SampleItem existing = new SampleItem();
      existing.setId(1L);
      existing.setItemCode("ITEM001");
      when(repository.findByItemCode("ITEM001")).thenReturn(existing);

      SampleItemCsvDto dto = new SampleItemCsvDto();
      dto.setItemCode("  ITEM001  ");
      dto.setItemName("更新商品");
      dto.setCategory("食品");
      dto.setPrice("1000");

      // Act
      SampleItem result = processor.process(dto);

      // Assert
      assertThat(result).isSameAs(existing);
      assertThat(result.getId()).isEqualTo(1L);
    }
  }
}
