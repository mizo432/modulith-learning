package undecided.generic.addressReg.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.addressReg.spi.City;

@Tag("small")
@DisplayName("CityQueryImplのテスト")
class CityQueryImplTest {

  private final CityRepository repository = mock(CityRepository.class);
  private final CityQueryImpl query = new CityQueryImpl(repository);

  @Nested
  @DisplayName("findByLgCodeメソッドのテスト")
  class FindByLgCodeTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenLgCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByLgCode(null))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("該当するCityが存在する場合、Optionalでラップして返すこと")
    void shouldReturnCityWhenFound() {
      // Arrange
      City city = new City();
      city.setId(131016L);
      city.setLgCode("131016");
      when(repository.findByLgCode("131016")).thenReturn(city);

      // Act
      Optional<City> result = query.findByLgCode("131016");

      // Assert
      assertThat(result).as("Cityが存在すること").isPresent().contains(city);
    }

    @Test
    @DisplayName("該当するCityが存在しない場合、空のOptionalを返すこと")
    void shouldReturnEmptyOptionalWhenNotFound() {
      // Arrange
      when(repository.findByLgCode("999999")).thenReturn(null);

      // Act
      Optional<City> result = query.findByLgCode("999999");

      // Assert
      assertThat(result).as("空のOptionalであること").isEmpty();
    }
  }

  @Nested
  @DisplayName("findByPrefectureIdメソッドのテスト")
  class FindByPrefectureIdTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPrefectureIdIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByPrefectureId(null))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("都道府県IDに該当する市区町村一覧を返すこと")
    void shouldReturnCityListWhenFound() {
      // Arrange
      City city1 = new City();
      city1.setId(131016L);
      City city2 = new City();
      city2.setId(131024L);
      when(repository.findByPrefectureId(13L)).thenReturn(List.of(city1, city2));

      // Act
      List<City> result = query.findByPrefectureId(13L);

      // Assert
      assertThat(result).as("市区町村リストが取得できること").containsExactly(city1, city2);
    }
  }
}
