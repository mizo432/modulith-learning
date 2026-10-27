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
import undecided.generic.addressReg.internal.PrefectureRepository;
import undecided.generic.addressReg.internal.batch.dto.PrefectureCsvDto;
import undecided.generic.addressReg.spi.Prefecture;

@Tag("small")
@DisplayName("PrefectureItemProcessorのテスト")
class PrefectureItemProcessorTest {

  private final PrefectureRepository repository = mock(PrefectureRepository.class);
  private final PrefectureItemProcessor processor = new PrefectureItemProcessor(repository);

  @Nested
  @DisplayName("processメソッドのテスト")
  class ProcessTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInputIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> processor.process(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("prefectureCodeが取得できない場合、nullを返すこと")
    void shouldReturnNullWhenPrefectureCodeIsInvalid() {
      // Arrange
      PrefectureCsvDto dto = new PrefectureCsvDto();
      dto.setLgCode("1"); // 1桁なのでコード取得不可

      // Act
      Prefecture result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("既存レコードが存在しない場合、新規エンティティを生成すること")
    void shouldCreateNewEntityWhenRecordDoesNotExist() {
      // Arrange
      PrefectureCsvDto dto =
          new PrefectureCsvDto("010006", "北海道", "ホッカイドウ", "Hokkaido", "1947-04-17", null, "テスト備考");
      when(repository.findByPrefectureCode("01")).thenReturn(null);

      // Act
      Prefecture result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getPrefectureCode()).isEqualTo("01");
      assertThat(result.getLgCode()).isEqualTo("010006");
      assertThat(result.getPrefName()).isEqualTo("北海道");
      assertThat(result.getPrefKana()).isEqualTo("ホッカイドウ");
      assertThat(result.getPrefRoma()).isEqualTo("Hokkaido");
      assertThat(result.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(result.getAbolitionData()).isEqualTo(LocalDate.of(9999, 12, 31));
      assertThat(result.getRemarks()).isEqualTo("テスト備考");
    }

    @Test
    @DisplayName("既存レコードが存在する場合、既存エンティティの値を更新すること")
    void shouldUpdateExistingEntityWhenRecordExists() {
      // Arrange
      Prefecture existing = new Prefecture();
      existing.setId(13L);
      existing.setPrefectureCode("13");
      existing.setLgCode("130000");
      existing.setPrefName("東京");
      existing.setPrefKana("トウキョウ");
      existing.setPrefRoma("Tokio");
      existing.setEffectiveDate(LocalDate.of(1947, 1, 1));
      existing.setAbolitionData(LocalDate.of(9999, 12, 31));

      PrefectureCsvDto dto =
          new PrefectureCsvDto(
              "130001", "東京都", "トウキョウト", "Tokyo", "1947-04-17", "9999-12-31", "更新後");
      when(repository.findByPrefectureCode("13")).thenReturn(existing);

      // Act
      Prefecture result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(13L);
      assertThat(result.getPrefectureCode()).isEqualTo("13");
      assertThat(result.getLgCode()).isEqualTo("130001");
      assertThat(result.getPrefName()).isEqualTo("東京都");
      assertThat(result.getPrefKana()).isEqualTo("トウキョウト");
      assertThat(result.getPrefRoma()).isEqualTo("Tokyo");
      assertThat(result.getEffectiveDate()).isEqualTo(LocalDate.of(1947, 4, 17));
      assertThat(result.getAbolitionData()).isEqualTo(LocalDate.of(9999, 12, 31));
      assertThat(result.getRemarks()).isEqualTo("更新後");
    }

    @Test
    @DisplayName("スラッシュ区切りの日付や廃止日を適切にパースできること")
    void shouldParseDatesWithSlashFormat() {
      // Arrange
      PrefectureCsvDto dto =
          new PrefectureCsvDto(
              "470007", "沖縄県", "オキナワケン", "Okinawa", "1972/05/15", "2026/12/31", null);
      when(repository.findByPrefectureCode("47")).thenReturn(null);

      // Act
      Prefecture result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getEffectiveDate()).isEqualTo(LocalDate.of(1972, 5, 15));
      assertThat(result.getAbolitionData()).isEqualTo(LocalDate.of(2026, 12, 31));
    }
  }
}
