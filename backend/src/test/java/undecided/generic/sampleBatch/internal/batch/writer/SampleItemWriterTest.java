package undecided.generic.sampleBatch.internal.batch.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.spi.SampleItem;

@Tag("small")
@DisplayName("SampleItemWriterのテスト")
class SampleItemWriterTest {

  private final SampleItemRepository repository = mock(SampleItemRepository.class);
  private final SampleItemWriter writer = new SampleItemWriter(repository);

  @Nested
  @DisplayName("writeメソッドのテスト")
  class WriteTest {

    @Test
    @DisplayName("nullのChunkが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenChunkIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> writer.write(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("正常なChunkの場合、リポジトリにsaveAllを呼び出すこと")
    void shouldSaveAllItemsWhenChunkIsValid() {
      // Arrange
      SampleItem item1 = new SampleItem();
      item1.setItemCode("ITEM001");
      item1.setItemName("商品A");

      SampleItem item2 = new SampleItem();
      item2.setItemCode("ITEM002");
      item2.setItemName("商品B");

      @SuppressWarnings("unchecked")
      List<SampleItem> items = Arrays.asList(item1, item2);
      Chunk<SampleItem> chunk = mock(Chunk.class);
      when(chunk.getItems()).thenReturn(items);
      when(chunk.size()).thenReturn(2);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("空のChunkの場合、リポジトリに空リストでsaveAllを呼び出すこと")
    @SuppressWarnings("unchecked")
    void shouldSaveEmptyListWhenChunkIsEmpty() {
      // Arrange
      Chunk<SampleItem> chunk = mock(Chunk.class);
      when(chunk.getItems()).thenReturn(List.of());
      when(chunk.size()).thenReturn(0);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository, times(1)).saveAll(anyList());
    }
  }
}
