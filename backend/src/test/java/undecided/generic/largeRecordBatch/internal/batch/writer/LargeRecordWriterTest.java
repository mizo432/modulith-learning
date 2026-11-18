package undecided.generic.largeRecordBatch.internal.batch.writer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import undecided.generic.largeRecordBatch.spi.LargeRecord;

@Tag("small")
@DisplayName("LargeRecordWriterのテスト")
@ExtendWith(MockitoExtension.class)
class LargeRecordWriterTest {

  @Mock
  private undecided.generic.largeRecordBatch.internal.LargeRecordRepository repository;

  private LargeRecordWriter writer;

  @Mock private Chunk<LargeRecord> chunk;

  @BeforeEach
  void setUp() {
    writer = new LargeRecordWriter(repository);
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
      LargeRecord record1 = new LargeRecord();
      record1.setRecordCode("REC000001");
      record1.setRecordName("レコード1");

      LargeRecord record2 = new LargeRecord();
      record2.setRecordCode("REC000002");
      record2.setRecordName("レコード2");

      List<LargeRecord> items = List.of(record1, record2);
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

    @Test
    @DisplayName("大量のレコードを含むChunkの場合、リポジトリにsaveAllを呼び出すこと")
    void shouldSaveLargeChunk() {
      // Arrange
      List<LargeRecord> items = new ArrayList<>();
      for (int i = 0; i < 200; i++) {
        LargeRecord record = new LargeRecord();
        record.setRecordCode("REC" + String.format("%06d", i));
        record.setRecordName("レコード" + i);
        items.add(record);
      }
      when(chunk.getItems()).thenReturn(items);
      when(chunk.size()).thenReturn(200);

      // Act
      writer.write(chunk);

      // Assert
      verify(repository, times(1)).saveAll(anyList());
    }
  }
}
