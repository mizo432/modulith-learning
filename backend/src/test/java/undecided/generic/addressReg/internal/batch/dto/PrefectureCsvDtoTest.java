package undecided.generic.addressReg.internal.batch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("PrefectureCsvDtoのテスト")
class PrefectureCsvDtoTest {

  @Nested
  @DisplayName("getPrefectureCodeメソッドのテスト")
  class GetPrefectureCodeTest {

    @Test
    @DisplayName("lgCodeが6桁の場合、先頭2桁を返すこと")
    void shouldReturnFirstTwoDigitsWhenLgCodeIs6Digits() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode("010006");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).isEqualTo("01");
    }

    @Test
    @DisplayName("lgCodeの前後に空白がある場合、トリムした先頭2桁を返すこと")
    void shouldReturnTrimmedFirstTwoDigitsWhenLgCodeHasWhitespace() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode("  130001  ");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).isEqualTo("13");
    }

    @Test
    @DisplayName("lgCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsNull() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode(null);

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).isNull();
    }

    @Test
    @DisplayName("lgCodeが空文字の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsEmpty() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode("");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).isNull();
    }

    @Test
    @DisplayName("lgCodeが1文字の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeLengthIsLessThanTwo() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode("1");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).isNull();
    }
  }

  @Nested
  @DisplayName("GetterおよびSetterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetPropertiesCorrectly() {
      // Arrange & Act
      PrefectureCsvDto dto =
          new PrefectureCsvDto(
              "010006", "北海道", "ホッカイドウ", "Hokkaido", "1947-04-17", "9999-12-31", "備考テスト");

      // Assert
      assertThat(dto.getLgCode()).isEqualTo("010006");
      assertThat(dto.getPrefName()).isEqualTo("北海道");
      assertThat(dto.getPrefKana()).isEqualTo("ホッカイドウ");
      assertThat(dto.getPrefRoma()).isEqualTo("Hokkaido");
      assertThat(dto.getEffectiveDate()).isEqualTo("1947-04-17");
      assertThat(dto.getAbolitionDate()).isEqualTo("9999-12-31");
      assertThat(dto.getRemarks()).isEqualTo("備考テスト");
    }

    @Test
    @DisplayName("equalsおよびhashCodeが同一値のオブジェクトで一致すること")
    void shouldVerifyEqualsAndHashCode() {
      PrefectureCsvDto dto1 =
          new PrefectureCsvDto("130001", "東京都", "トウキョウト", "Tokyo", "1947-04-17", "", null);
      PrefectureCsvDto dto2 =
          new PrefectureCsvDto("130001", "東京都", "トウキョウト", "Tokyo", "1947-04-17", "", null);

      assertThat(dto1).isEqualTo(dto2);
      assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
      assertThat(dto1.toString()).contains("130001", "東京都", "Tokyo");
    }
  }
}
