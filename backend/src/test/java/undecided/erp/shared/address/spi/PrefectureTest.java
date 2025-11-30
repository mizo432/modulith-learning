package undecided.erp.shared.address.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Prefectureクラスのテスト")
class PrefectureTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTests {

    @Test
    @DisplayName("正常な引数でPrefectureインスタンスを作成できること")
    void shouldCreatePrefectureWithValidArguments() {
      // Arrange
      String code = "13";
      String name = "東京都";
      String kana = "トウキョウト";

      // Act
      Prefecture result = Prefecture.create(code, name, kana);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getCode()).isEqualTo(code);
      assertThat(result.getName()).isEqualTo(name);
      assertThat(result.getKana()).isEqualTo(kana);
      assertThat(result.getPrefectureId()).isNotNull();
    }

    @Test
    @DisplayName("code引数がnullの場合は例外をスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Arrange
      String name = "東京都";
      String kana = "トウキョウト";

      // Act & Assert
      assertThatThrownBy(() -> Prefecture.create(null, name, kana))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("name引数がnullの場合は例外をスローすること")
    void shouldThrowExceptionWhenNameIsNull() {
      // Arrange
      String code = "13";
      String kana = "トウキョウト";

      // Act & Assert
      assertThatThrownBy(() -> Prefecture.create(code, null, kana))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("kana引数がnullの場合は例外をスローすること")
    void shouldThrowExceptionWhenKanaIsNull() {
      // Arrange
      String code = "13";
      String name = "東京都";

      // Act & Assert
      assertThatThrownBy(() -> Prefecture.create(code, name, null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
