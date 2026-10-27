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
import undecided.generic.addressReg.internal.CityRepository;
import undecided.generic.addressReg.internal.batch.dto.CityCsvDto;
import undecided.generic.addressReg.spi.City;

@Tag("small")
@DisplayName("CityItemProcessorのテスト")
class CityItemProcessorTest {

  private final CityRepository repository = mock(CityRepository.class);
  private final CityItemProcessor processor = new CityItemProcessor(repository);

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
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("");

      // Act
      City result = processor.process(dto);

      // Assert
      assertThat(result).as("無効なlgCodeの場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("prefectureIdが取得できない場合、nullを返すこと")
    void shouldReturnNullWhenPrefectureIdIsInvalid() {
      // Arrange
      CityCsvDto dto = new CityCsvDto();
      dto.setLgCode("A"); // 1文字かつ非数値

      // Act
      City result = processor.process(dto);

      // Assert
      assertThat(result).as("prefectureId取得不可の場合はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("既存レコードが存在しない場合、新規エンティティを生成すること")
    void shouldCreateNewEntityWhenRecordDoesNotExist() {
      // Arrange
      CityCsvDto dto =
          new CityCsvDto(
              "011011",
              "北海道",
              "ホッカイドウ",
              "Hokkaido",
              null,
              null,
              null,
              "札幌市",
              "サッポロシ",
              "Sapporo-shi",
              "中央区",
              "チュウオウク",
              "Chuo-ku",
              "1947-04-17",
              null,
              "テスト備考");
      when(repository.findByLgCode("011011")).thenReturn(null);

      // Act
      City result = processor.process(dto);

      // Assert
      assertThat(result).as("生成されたエンティティが存在すること").isNotNull();
      assertThat(result.getId()).as("IDが11011Lであること").isEqualTo(11011L);
      assertThat(result.getPrefectureId()).as("prefectureIdが1Lであること").isEqualTo(1L);
      assertThat(result.getLgCode()).as("lgCodeが011011であること").isEqualTo("011011");
      assertThat(result.getCountryName()).as("countryNameがnullであること").isNull();
      assertThat(result.getCityName()).as("cityNameが札幌市であること").isEqualTo("札幌市");
      assertThat(result.getWardName()).as("wardNameが中央区であること").isEqualTo("中央区");
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
      City existing = new City();
      existing.setId(131016L);
      existing.setPrefectureId(13L);
      existing.setLgCode("131016");
      existing.setCityName("千代田");
      existing.setEffectiveDate(LocalDate.of(1947, 1, 1));
      existing.setAbolitionData(LocalDate.of(9999, 12, 31));

      CityCsvDto dto =
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
              "9999-12-31",
              "更新後備考");
      when(repository.findByLgCode("131016")).thenReturn(existing);

      // Act
      City result = processor.process(dto);

      // Assert
      assertThat(result).as("更新されたエンティティが存在すること").isNotNull();
      assertThat(result.getId()).as("IDが維持されること").isEqualTo(131016L);
      assertThat(result.getPrefectureId()).isEqualTo(13L);
      assertThat(result.getCityName()).isEqualTo("千代田区");
      assertThat(result.getCityKana()).isEqualTo("チヨダク");
      assertThat(result.getCityRoma()).isEqualTo("Chiyoda-ku");
      assertThat(result.getRemarks()).isEqualTo("更新後備考");
    }

    @Test
    @DisplayName("スラッシュ区切りの日付や郡部データを適切に変換できること")
    void shouldProcessCountyDataAndSlashDates() {
      // Arrange
      CityCsvDto dto =
          new CityCsvDto(
              "014605",
              "北海道",
              "ホッカイドウ",
              "Hokkaido",
              "空知郡",
              "ソラチグン",
              "Sorachi-gun",
              "南富良野町",
              "ミナミフラノチョウ",
              "Minamifurano-cho",
              null,
              null,
              null,
              "1947/04/17",
              "2026/12/31",
              null);
      when(repository.findByLgCode("014605")).thenReturn(null);

      // Act
      City result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getCountryName()).isEqualTo("空知郡");
      assertThat(result.getCountryKana()).isEqualTo("ソラチグン");
      assertThat(result.getCountryRoma()).isEqualTo("Sorachi-gun");
      assertThat(result.getCityName()).isEqualTo("南富良野町");
      assertThat(result.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(result.getAbolitionData()).isEqualTo(LocalDate.of(2026, 12, 31));
    }
  }
}
