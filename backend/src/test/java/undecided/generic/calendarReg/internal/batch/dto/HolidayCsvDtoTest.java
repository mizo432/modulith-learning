package undecided.generic.calendarReg.internal.batch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("HolidayCsvDtoのテスト")
class HolidayCsvDtoTest {

  @Nested
  @DisplayName("getParsedHolidayDateメソッドのテスト")
  class GetParsedHolidayDateTest {

    @Test
    @DisplayName("スラッシュ区切りの日付 (yyyy/MM/dd) が正常にパースできること")
    void shouldParseSlashSeparatedDateWithLeadingZeros() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("2024/01/01");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("スラッシュ区切りのゼロ埋めなし日付 (yyyy/M/d) が正常にパースできること")
    void shouldParseSlashSeparatedDateWithoutLeadingZeros() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("1955/1/1");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(1955, 1, 1));
    }

    @Test
    @DisplayName("ハイフン区切りの日付 (yyyy-MM-dd) が正常にパースできること")
    void shouldParseHyphenSeparatedDate() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("2024-12-31");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    @DisplayName("区切りなしの日付 (yyyyMMdd) が正常にパースできること")
    void shouldParseDateWithoutSeparators() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("20240505");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(2024, 5, 5));
    }

    @Test
    @DisplayName("前後に空白がある日付文字列がトリムされて正常にパースできること")
    void shouldParseDateWithWhitespaces() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("  2024/02/11  ");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isEqualTo(LocalDate.of(2024, 2, 11));
    }

    @Test
    @DisplayName("holidayDateがnullの場合、nullを返すこと")
    void shouldReturnNullWhenHolidayDateIsNull() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate(null);

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isNull();
    }

    @Test
    @DisplayName("holidayDateが空文字または空白のみの場合、nullを返すこと")
    void shouldReturnNullWhenHolidayDateIsBlank() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("   ");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isNull();
    }

    @Test
    @DisplayName("holidayDateが無効な日付文字列の場合、nullを返すこと")
    void shouldReturnNullWhenHolidayDateIsInvalid() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("invalid-date");

      // Act
      LocalDate date = dto.getParsedHolidayDate();

      // Assert
      assertThat(date).isNull();
    }
  }

  @Nested
  @DisplayName("getHolidayIdメソッドのテスト")
  class GetHolidayIdTest {

    @Test
    @DisplayName("有効な日付文字列から対応するLong型のID (yyyyMMdd) を生成できること")
    void shouldGenerateLongIdFromValidHolidayDate() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("2024/01/08");

      // Act
      Long id = dto.getHolidayId();

      // Assert
      assertThat(id).isEqualTo(20240108L);
    }

    @Test
    @DisplayName("日付が無効な場合、nullを返すこと")
    void shouldReturnNullWhenDateIsInvalid() {
      // Arrange
      HolidayCsvDto dto = new HolidayCsvDto();
      dto.setHolidayDate("invalid");

      // Act
      Long id = dto.getHolidayId();

      // Assert
      assertThat(id).isNull();
    }
  }

  @Nested
  @DisplayName("GetterおよびSetterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetPropertiesCorrectly() {
      // Arrange & Act
      HolidayCsvDto dto = new HolidayCsvDto("2024/01/01", "元日", "国民の祝日");

      // Assert
      assertThat(dto.getHolidayDate()).isEqualTo("2024/01/01");
      assertThat(dto.getHolidayName()).isEqualTo("元日");
      assertThat(dto.getRemarks()).isEqualTo("国民の祝日");
    }

    @Test
    @DisplayName("equalsおよびhashCodeが同一値のオブジェクトで一致すること")
    void shouldVerifyEqualsAndHashCode() {
      HolidayCsvDto dto1 = new HolidayCsvDto("2024/01/01", "元日", null);
      HolidayCsvDto dto2 = new HolidayCsvDto("2024/01/01", "元日", null);

      assertThat(dto1).isEqualTo(dto2);
      assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
      assertThat(dto1.toString()).contains("2024/01/01", "元日");
    }
  }
}
