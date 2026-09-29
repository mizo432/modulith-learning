package undecided.generic.addressReg.internal.batch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ChouAzaCsvDtoのテスト")
class ChouAzaCsvDtoTest {

  @Nested
  @DisplayName("getCityIdメソッドのテスト")
  class GetCityIdTest {

    @Test
    @DisplayName("lgCodeが有効な場合、Long型で返すこと")
    void shouldReturnCityIdWhenLgCodeIsValid() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("011011");

      // Act
      Long cityId = dto.getCityId();

      // Assert
      assertThat(cityId).as("011011は11011Lとして取得できること").isEqualTo(11011L);
    }

    @Test
    @DisplayName("lgCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsNull() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode(null);

      // Act
      Long cityId = dto.getCityId();

      // Assert
      assertThat(cityId).as("nullの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("lgCodeが空文字の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsEmpty() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("   ");

      // Act
      Long cityId = dto.getCityId();

      // Assert
      assertThat(cityId).as("空白の場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("lgCodeが非数値の場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsNotNumeric() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("ABC");

      // Act
      Long cityId = dto.getCityId();

      // Assert
      assertThat(cityId).as("非数値の場合はnullを返すこと").isNull();
    }
  }

  @Nested
  @DisplayName("getChouAzaIdメソッドのテスト")
  class GetChouAzaIdTest {

    @Test
    @DisplayName("lgCodeとmachiazaCodeが有効な場合、結合してLong型で返すこと")
    void shouldReturnChouAzaIdWhenCodesAreValid() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("131016");
      dto.setMachiazaCode("0001001");

      // Act
      Long chouAzaId = dto.getChouAzaId();

      // Assert
      assertThat(chouAzaId).as("結合した町字IDが取得できること").isEqualTo(1310160001001L);
    }

    @Test
    @DisplayName("machiazaCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenMachiazaCodeIsNull() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("131016");
      dto.setMachiazaCode(null);

      // Act
      Long chouAzaId = dto.getChouAzaId();

      // Assert
      assertThat(chouAzaId).as("machiazaCodeがnullの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("machiazaCodeが空文字の場合、nullを返すこと")
    void shouldReturnNullWhenMachiazaCodeIsEmpty() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("131016");
      dto.setMachiazaCode("  ");

      // Act
      Long chouAzaId = dto.getChouAzaId();

      // Assert
      assertThat(chouAzaId).as("machiazaCodeが空白の場合はnullを返すこと").isNull();
    }
  }

  @Nested
  @DisplayName("GetterおよびSetterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetPropertiesCorrectly() {
      // Arrange & Act
      ChouAzaCsvDto dto =
          new ChouAzaCsvDto(
              "011011",
              "0001001",
              "1",
              "旭ケ丘",
              "アサヒガオカ",
              "Asahigaoka",
              "１丁目",
              "１チョウメ",
              "1",
              "小字名",
              "コアザメイ",
              "Koaza",
              "0",
              "1",
              "1",
              "0",
              "0",
              null,
              null,
              "0",
              "0",
              "1",
              "1947-04-17",
              "9999-12-31",
              "備考テスト");

      // Assert
      assertThat(dto.getLgCode()).isEqualTo("011011");
      assertThat(dto.getMachiazaCode()).isEqualTo("0001001");
      assertThat(dto.getMachiazaType()).isEqualTo("1");
      assertThat(dto.getOazaChoName()).isEqualTo("旭ケ丘");
      assertThat(dto.getOazaChoKana()).isEqualTo("アサヒガオカ");
      assertThat(dto.getOazaChoRoma()).isEqualTo("Asahigaoka");
      assertThat(dto.getChomeName()).isEqualTo("１丁目");
      assertThat(dto.getChomeKana()).isEqualTo("１チョウメ");
      assertThat(dto.getChomeNumber()).isEqualTo("1");
      assertThat(dto.getKoazaName()).isEqualTo("小字名");
      assertThat(dto.getKoazaKana()).isEqualTo("コアザメイ");
      assertThat(dto.getKoazaRoma()).isEqualTo("Koaza");
      assertThat(dto.getMachiazaDist()).isEqualTo("0");
      assertThat(dto.getRsdtAddrFlg()).isEqualTo("1");
      assertThat(dto.getRsdtAddrMtdCode()).isEqualTo("1");
      assertThat(dto.getOazaChoAkaFlg()).isEqualTo("0");
      assertThat(dto.getKoazaAkaCode()).isEqualTo("0");
      assertThat(dto.getStatus()).isEqualTo("0");
      assertThat(dto.getWakeNumFlg()).isEqualTo("0");
      assertThat(dto.getSrcCode()).isEqualTo("1");
      assertThat(dto.getEffectiveDate()).isEqualTo("1947-04-17");
      assertThat(dto.getAbolitionDate()).isEqualTo("9999-12-31");
      assertThat(dto.getRemarks()).isEqualTo("備考テスト");
    }

    @Test
    @DisplayName("equalsおよびhashCodeが同一値のオブジェクトで一致すること")
    void shouldVerifyEqualsAndHashCode() {
      ChouAzaCsvDto dto1 = new ChouAzaCsvDto();
      dto1.setLgCode("131016");
      dto1.setMachiazaCode("0001001");

      ChouAzaCsvDto dto2 = new ChouAzaCsvDto();
      dto2.setLgCode("131016");
      dto2.setMachiazaCode("0001001");

      assertThat(dto1).isEqualTo(dto2);
      assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
      assertThat(dto1.toString()).contains("131016", "0001001");
    }
  }
}
