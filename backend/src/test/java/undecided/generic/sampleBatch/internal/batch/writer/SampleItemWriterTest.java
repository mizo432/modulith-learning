package undecided.generic.sampleBatch.internal.batch.writer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.Chunk;
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.spi.SampleItem;

@Tag("small")
@DisplayName("SampleItemWriterのテスト")
@ExtendWith(MockitoExtension.class)
class SampleItemWriterTest {

  @Mock private SampleItemRepository repository;

  private SampleItemWriter writer;

  @Mock private Chunk<SampleItem> chunk;

  @BeforeEach
  void setUp() {
    writer = new SampleItemWriter(repository);
  }

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

      List<SampleItem> items = Arrays.asList(item1, item2);
      when(chunk.getItems()).thenReturn(items);
      when(chunk.size()).thenReturn(2);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("空のChunkの場合、リポジトリに空リストでsaveAllを呼び出すこと")
    void shouldSaveEmptyListWhenChunkIsEmpty() {
      // Arrange
      when(chunk.getItems()).thenReturn(List.of());
      when(chunk.size()).thenReturn(0);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository, times(1)).saveAll(anyList());
    }
  }
}
