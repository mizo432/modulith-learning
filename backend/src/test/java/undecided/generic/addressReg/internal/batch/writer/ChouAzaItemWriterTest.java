package undecided.generic.addressReg.internal.batch.writer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.Chunk;
import undecided.generic.addressReg.internal.ChouAzaRepository;
import undecided.generic.addressReg.spi.ChouAza;

@Tag("small")
@DisplayName("ChouAzaItemWriterのテスト")
class ChouAzaItemWriterTest {

  private final ChouAzaRepository repository = mock(ChouAzaRepository.class);
  private final ChouAzaItemWriter writer = new ChouAzaItemWriter(repository);

  @Nested
  @DisplayName("writeメソッドのテスト")
  class WriteTest {

    @Test
    @DisplayName("nullのChunkが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenChunkIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> writer.write(null))
          .as("nullのChunkはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Chunkに含まれるChouAzaエンティティをrepository.saveAllで永続化すること")
    void shouldPersistAllItemsInChunk() {
      // Arrange
      ChouAza aza1 = new ChouAza();
      aza1.setId(110110001001L);
      ChouAza aza2 = new ChouAza();
      aza2.setId(1310160001001L);
      Chunk<ChouAza> chunk = new Chunk<>(List.of(aza1, aza2));

      // Act
      writer.write(chunk);

      // Assert
      verify(repository).saveAll(List.of(aza1, aza2));
    }
  }
}
