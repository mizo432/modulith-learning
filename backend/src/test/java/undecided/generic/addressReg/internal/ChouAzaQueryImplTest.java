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
import undecided.generic.addressReg.spi.ChouAza;

@Tag("small")
@DisplayName("ChouAzaQueryImplのテスト")
class ChouAzaQueryImplTest {

  private final ChouAzaRepository repository = mock(ChouAzaRepository.class);
  private final ChouAzaQueryImpl query = new ChouAzaQueryImpl(repository);

  @Nested
  @DisplayName("findByLgCodeAndMachiazaCodeメソッドのテスト")
  class FindByLgCodeAndMachiazaCodeTest {

    @Test
    @DisplayName("lgCodeがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenLgCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByLgCodeAndMachiazaCode(null, "0001001"))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("machiazaCodeがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenMachiazaCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByLgCodeAndMachiazaCode("131016", null))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("該当するChouAzaが存在する場合、Optionalでラップして返すこと")
    void shouldReturnChouAzaWhenFound() {
      // Arrange
      ChouAza chouAza = new ChouAza();
      chouAza.setId(1310160001001L);
      chouAza.setLgCode("131016");
      chouAza.setMachiazaCode("0001001");
      when(repository.findByLgCodeAndMachiazaCode("131016", "0001001")).thenReturn(chouAza);

      // Act
      Optional<ChouAza> result = query.findByLgCodeAndMachiazaCode("131016", "0001001");

      // Assert
      assertThat(result).as("ChouAzaが存在すること").isPresent().contains(chouAza);
    }

    @Test
    @DisplayName("該当するChouAzaが存在しない場合、空のOptionalを返すこと")
    void shouldReturnEmptyOptionalWhenNotFound() {
      // Arrange
      when(repository.findByLgCodeAndMachiazaCode("999999", "9999999")).thenReturn(null);

      // Act
      Optional<ChouAza> result = query.findByLgCodeAndMachiazaCode("999999", "9999999");

      // Assert
      assertThat(result).as("空のOptionalであること").isEmpty();
    }
  }

  @Nested
  @DisplayName("findByCityIdメソッドのテスト")
  class FindByCityIdTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCityIdIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByCityId(null))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("市区町村IDに該当する町字一覧を返すこと")
    void shouldReturnChouAzaListWhenFound() {
      // Arrange
      ChouAza aza1 = new ChouAza();
      aza1.setId(1310160001001L);
      ChouAza aza2 = new ChouAza();
      aza2.setId(1310160001002L);
      when(repository.findByCityId(131016L)).thenReturn(List.of(aza1, aza2));

      // Act
      List<ChouAza> result = query.findByCityId(131016L);

      // Assert
      assertThat(result).as("町字リストが取得できること").containsExactly(aza1, aza2);
    }
  }

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
    @DisplayName("法定コードに該当する町字一覧を返すこと")
    void shouldReturnChouAzaListWhenFound() {
      // Arrange
      ChouAza aza1 = new ChouAza();
      aza1.setId(1310160001001L);
      aza1.setLgCode("131016");
      when(repository.findByLgCode("131016")).thenReturn(List.of(aza1));

      // Act
      List<ChouAza> result = query.findByLgCode("131016");

      // Assert
      assertThat(result).as("町字リストが取得できること").containsExactly(aza1);
    }
  }
}
