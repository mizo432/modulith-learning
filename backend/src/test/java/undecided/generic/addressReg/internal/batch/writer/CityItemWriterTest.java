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
import undecided.generic.addressReg.internal.CityRepository;
import undecided.generic.addressReg.spi.City;

@Tag("small")
@DisplayName("CityItemWriterのテスト")
class CityItemWriterTest {

  private final CityRepository repository = mock(CityRepository.class);
  private final CityItemWriter writer = new CityItemWriter(repository);

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
    @DisplayName("Chunkに含まれるCityエンティティをrepository.saveAllで永続化すること")
    void shouldPersistAllItemsInChunk() {
      // Arrange
      City city1 = new City();
      city1.setId(11011L);
      City city2 = new City();
      city2.setId(131016L);
      Chunk<City> chunk = new Chunk<>(List.of(city1, city2));

      // Act
      writer.write(chunk);

      // Assert
      verify(repository).saveAll(List.of(city1, city2));
    }
  }
}
