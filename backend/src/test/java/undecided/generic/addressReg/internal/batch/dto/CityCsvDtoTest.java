package undecided.generic.addressReg.internal.batch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("CityCsvDtoのテスト")
class CityCsvDtoTest {

  @Nested
  @DisplayName("getPrefectureCodeメソッドのテスト")
  class GetPrefectureCodeTest {

    @Test
    @DisplayName("lgCodeが6桁の場合、先頭2桁を返すこと")
    void shouldReturnFirstTwoDigitsWhenLgCodeIs6Digits() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("011002");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).as("先頭2桁の都道府県コードが取得できること").isEqualTo("01");
    }

    @Test
    @DisplayName("lgCodeの前後に空白がある場合、トリムした先頭2桁を返すこと")
    void shouldReturnTrimmedFirstTwoDigitsWhenLgCodeHasWhitespace() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("  131016  ");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).as("空白除去後の先頭2桁が取得できること").isEqualTo("13");
    }

    @Test
    @DisplayName("lgCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsNull() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode(null);

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).as("nullの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("lgCodeが空文字の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsEmpty() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).as("空文字の場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("lgCodeが1文字の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeLengthIsLessThanTwo() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("1");

      // Act
      String prefectureCode = dto.getPrefectureCode();

      // Assert
      assertThat(prefectureCode).as("2桁未満の場合はnullを返すこと").isNull();
    }
  }

  @Nested
  @DisplayName("getPrefectureIdメソッドのテスト")
  class GetPrefectureIdTest {

    @Test
    @DisplayName("lgCodeが有効な場合、先頭2桁をLong型で返すこと")
    void shouldReturnPrefectureIdAsLongWhenLgCodeIsValid() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("011002");

      // Act
      Long prefectureId = dto.getPrefectureId();

      // Assert
      assertThat(prefectureId).as("01は1Lとして取得できること").isEqualTo(1L);
    }

    @Test
    @DisplayName("lgCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsNull() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode(null);

      // Act
      Long prefectureId = dto.getPrefectureId();

      // Assert
      assertThat(prefectureId).as("nullの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("lgCodeの先頭2桁が数値でない場合、nullを返すこと")
    void shouldReturnNullWhenPrefectureCodeIsNotNumeric() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("AB1002");

      // Act
      Long prefectureId = dto.getPrefectureId();

      // Assert
      assertThat(prefectureId).as("数値以外の先頭2桁の場合はnullを返すこと").isNull();
    }
  }

  @Nested
  @DisplayName("GetterおよびSetterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetPropertiesCorrectly() {
      // Arrange & Act
      CityCsvDto dto =
          new CityCsvDto(
              "011011",
              "北海道",
              "ホッカイドウ",
              "Hokkaido",
              "郡名なし",
              "グンメイナシ",
              "NoCounty",
              "札幌市",
              "サッポロシ",
              "Sapporo-shi",
              "中央区",
              "チュウオウク",
              "Chuo-ku",
              "1947-04-17",
              "9999-12-31",
              "備考テスト");

      // Assert
      assertThat(dto.getLgCode()).as("lgCodeが一致すること").isEqualTo("011011");
      assertThat(dto.getPrefName()).as("prefNameが一致すること").isEqualTo("北海道");
      assertThat(dto.getPrefKana()).as("prefKanaが一致すること").isEqualTo("ホッカイドウ");
      assertThat(dto.getPrefRoma()).as("prefRomaが一致すること").isEqualTo("Hokkaido");
      assertThat(dto.getCountryName()).as("countryNameが一致すること").isEqualTo("郡名なし");
      assertThat(dto.getCountryKana()).as("countryKanaが一致すること").isEqualTo("グンメイナシ");
      assertThat(dto.getCountryRoma()).as("countryRomaが一致すること").isEqualTo("NoCounty");
      assertThat(dto.getCityName()).as("cityNameが一致すること").isEqualTo("札幌市");
      assertThat(dto.getCityKana()).as("cityKanaが一致すること").isEqualTo("サッポロシ");
      assertThat(dto.getCityRoma()).as("cityRomaが一致すること").isEqualTo("Sapporo-shi");
      assertThat(dto.getWardName()).as("wardNameが一致すること").isEqualTo("中央区");
      assertThat(dto.getWardKana()).as("wardKanaが一致すること").isEqualTo("チュウオウク");
      assertThat(dto.getWardRoma()).as("wardRomaが一致すること").isEqualTo("Chuo-ku");
      assertThat(dto.getEffectiveDate()).as("effectiveDateが一致すること").isEqualTo("1947-04-17");
      assertThat(dto.getAbolitionDate()).as("abolitionDateが一致すること").isEqualTo("9999-12-31");
      assertThat(dto.getRemarks()).as("remarksが一致すること").isEqualTo("備考テスト");
    }

    @Test
    @DisplayName("equalsおよびhashCodeが同一値のオブジェクトで一致すること")
    void shouldVerifyEqualsAndHashCode() {
      CityCsvDto dto1 =
          new CityCsvDto(
              "131016",
              "東京都",
              "トウキョウト",
              "Tokyo",
              null,
              null,
              null,
              "千代田区",
              "チヨダク",
              "Chiyoda-ku",
              null,
              null,
              null,
              "1947-04-17",
              "",
              null);
      CityCsvDto dto2 =
          new CityCsvDto(
              "131016",
              "東京都",
              "トウキョウト",
              "Tokyo",
              null,
              null,
              null,
              "千代田区",
              "チヨダク",
              "Chiyoda-ku",
              null,
              null,
              null,
              "1947-04-17",
              "",
              null);

      assertThat(dto1).as("同一内容のオブジェクトが等価であること").isEqualTo(dto2);
      assertThat(dto1.hashCode()).as("ハッシュコードが等しいこと").isEqualTo(dto2.hashCode());
      assertThat(dto1.toString()).as("文字列化に主要フィールドが含まれること").contains("131016", "千代田区");
    }
  }
}
