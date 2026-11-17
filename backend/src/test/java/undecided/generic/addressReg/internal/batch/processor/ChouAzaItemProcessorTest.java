package undecided.generic.addressReg.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.addressReg.internal.ChouAzaRepository;
import undecided.generic.addressReg.internal.batch.dto.ChouAzaCsvDto;
import undecided.generic.addressReg.spi.ChouAza;

@Tag("small")
@DisplayName("ChouAzaItemProcessorのテスト")
class ChouAzaItemProcessorTest {

  private final ChouAzaRepository repository = mock(ChouAzaRepository.class);
  private final ChouAzaItemProcessor processor = new ChouAzaItemProcessor(repository);

  @Nested
  @DisplayName("processメソッドのテスト")
  class ProcessTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInputIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> processor.process(null))
          .as("null引数はIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("lgCodeが空または無効な場合、nullを返すこと")
    void shouldReturnNullWhenLgCodeIsInvalid() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("");
      dto.setMachiazaCode("0001001");

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).as("無効なlgCodeの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("machiazaCodeが空または無効な場合、nullを返すこと")
    void shouldReturnNullWhenMachiazaCodeIsInvalid() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("011011");
      dto.setMachiazaCode("");

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).as("無効なmachiazaCodeの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("cityIdまたはchouAzaIdが数値変換できない場合、nullを返すこと")
    void shouldReturnNullWhenIdConversionFails() {
      // Arrange
      ChouAzaCsvDto dto = new ChouAzaCsvDto();
      dto.setLgCode("ABC");
      dto.setMachiazaCode("DEF");

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).as("非数値コードの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("既存レコードが存在しない場合、新規エンティティを生成すること")
    void shouldCreateNewEntityWhenRecordDoesNotExist() {
      // Arrange
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
              null,
              null,
              null,
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
              null,
              "テスト備考");
      when(repository.findByLgCodeAndMachiazaCode("011011", "0001001")).thenReturn(null);

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).as("生成されたエンティティが存在すること").isNotNull();
      assertThat(result.getId()).as("IDが110110001001Lであること").isEqualTo(110110001001L);
      assertThat(result.getCityId()).as("cityIdが11011Lであること").isEqualTo(11011L);
      assertThat(result.getLgCode()).as("lgCodeが011011であること").isEqualTo("011011");
      assertThat(result.getMachiazaCode()).as("machiazaCodeが0001001であること").isEqualTo("0001001");
      assertThat(result.getOazaChoName()).as("大字・町名が旭ケ丘であること").isEqualTo("旭ケ丘");
      assertThat(result.getChomeName()).as("丁目名が１丁目であること").isEqualTo("１丁目");
      assertThat(result.getRsdtAddrFlg()).as("住居表示フラグがtrueであること").isTrue();
      assertThat(result.getEffectiveDate())
          .as("effectiveDateが正しいこと")
          .isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(result.getAbolitionData())
          .as("abolitionDataがデフォルト値であること")
          .isEqualTo(LocalDate.of(9999, 12, 31));
      assertThat(result.getRemarks()).as("remarksが正しいこと").isEqualTo("テスト備考");
    }

    @Test
    @DisplayName("既存レコードが存在する場合、既存エンティティの値を更新すること")
    void shouldUpdateExistingEntityWhenRecordExists() {
      // Arrange
      ChouAza existing = new ChouAza();
      existing.setId(1310160001001L);
      existing.setCityId(131016L);
      existing.setLgCode("131016");
      existing.setMachiazaCode("0001001");
      existing.setOazaChoName("旧丸の内");
      existing.setEffectiveDate(LocalDate.of(1947, 1, 1));
      existing.setAbolitionData(LocalDate.of(9999, 12, 31));

      ChouAzaCsvDto dto =
          new ChouAzaCsvDto(
              "131016",
              "0001001",
              "1",
              "丸の内",
              "マルノウチ",
              "Marunouchi",
              "１丁目",
              "１チョウメ",
              "1",
              null,
              null,
              null,
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
              "更新後備考");
      when(repository.findByLgCodeAndMachiazaCode("131016", "0001001")).thenReturn(existing);

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).as("更新されたエンティティが存在すること").isNotNull();
      assertThat(result.getId()).as("IDが維持されること").isEqualTo(1310160001001L);
      assertThat(result.getOazaChoName()).isEqualTo("丸の内");
      assertThat(result.getOazaChoKana()).isEqualTo("マルノウチ");
      assertThat(result.getOazaChoRoma()).isEqualTo("Marunouchi");
      assertThat(result.getRemarks()).isEqualTo("更新後備考");
    }

    @Test
    @DisplayName("スラッシュ区切りの日付や真偽値文字列を適切に変換できること")
    void shouldProcessSlashDatesAndBooleanStrings() {
      // Arrange
      ChouAzaCsvDto dto =
          new ChouAzaCsvDto(
              "472018",
              "0001001",
              "1",
              "泉崎",
              "イズミザキ",
              "Izumizaki",
              "１丁目",
              "１チョウメ",
              "1",
              null,
              null,
              null,
              "0",
              "true",
              "1",
              "false",
              "0",
              null,
              null,
              "1",
              "true",
              "1",
              "1972/05/15",
              "2026/12/31",
              null);
      when(repository.findByLgCodeAndMachiazaCode("472018", "0001001")).thenReturn(null);

      // Act
      ChouAza result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getRsdtAddrFlg()).isTrue();
      assertThat(result.getOazaChoAkaFlg()).isFalse();
      assertThat(result.getWakeNumFlg()).isTrue();
      assertThat(result.getStatus()).isEqualTo(1);
      assertThat(result.getEffectiveDate()).isEqualTo(LocalDate.of(1972, 5, 15));
      assertThat(result.getAbolitionData()).isEqualTo(LocalDate.of(2026, 12, 31));
    }
  }
}
