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
import undecided.generic.addressReg.internal.PrefectureRepository;
import undecided.generic.addressReg.spi.Prefecture;

@Tag("small")
@DisplayName("PrefectureItemWriterのテスト")
class PrefectureItemWriterTest {

  private final PrefectureRepository repository = mock(PrefectureRepository.class);
  private final PrefectureItemWriter writer = new PrefectureItemWriter(repository);

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
    @DisplayName("Chunk内の全エンティティをリポジトリで保存すること")
    void shouldSaveAllEntitiesInChunk() {
      // Arrange
      Prefecture p1 = new Prefecture();
      p1.setId(1L);
      p1.setPrefectureCode("01");

      Prefecture p2 = new Prefecture();
      p2.setId(2L);
      p2.setPrefectureCode("02");

      Chunk<Prefecture> chunk = new Chunk<>(List.of(p1, p2));

      // Act
      writer.write(chunk);

      // Assert
      verify(repository).saveAll(chunk.getItems());
    }
  }
}
