package undecided.generic.addressReg.internal.batch.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import undecided.generic.addressReg.internal.batch.dto.CityCsvDto;

@Tag("small")
@DisplayName("CityCsvItemReaderのテスト")
class CityCsvItemReaderTest {

  @Nested
  @DisplayName("openおよびreadメソッドのテスト")
  class OpenAndReadTest {

    @Test
    @DisplayName("Resourceがnullの状態でopenした場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenResourceIsNull() {
      // Arrange
      CityCsvItemReader reader = new CityCsvItemReader();

      // Act & Assert
      assertThatThrownBy(() -> reader.open(new ExecutionContext()))
          .as("nullリソースでのopenはItemStreamExceptionをスローすること")
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Resource must not be null");
    }

    @Test
    @DisplayName("openを呼び出さずにreadを実行した場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenReadWithoutOpen() {
      // Arrange
      CityCsvItemReader reader =
          new CityCsvItemReader(new ByteArrayResource("test".getBytes(StandardCharsets.UTF_8)));

      // Act & Assert
      assertThatThrownBy(reader::read)
          .as("open前のread実行はItemStreamExceptionをスローすること")
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }

    @Test
    @DisplayName("日本語ヘッダー付きCSVからCityCsvDtoを正しく読み込めること")
    void shouldReadCityCsvDtoCorrectly() throws Exception {
      // Arrange
      String csv =
          """
          全国地方公共団体コード,都道府県名,都道府県名_カナ,都道府県名_英字,郡名,郡名_カナ,郡名_英字,市区町村名,市区町村名_カナ,市区町村名_英字,政令市区名,政令市区名_カナ,政令市区名_英字,効力発生日,廃止日,備考
          011011,北海道,ホッカイドウ,Hokkaido,,,,札幌市,サッポロシ,Sapporo-shi,中央区,チュウオウク,Chuo-ku,1947-04-17,,
          014605,北海道,ホッカイドウ,Hokkaido,空知郡,ソラチグン,Sorachi-gun,南富良野町,ミナミフラノチョウ,Minamifurano-cho,,,,1947-04-17,,
          """;
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
      CityCsvItemReader reader = new CityCsvItemReader(resource);
      reader.open(new ExecutionContext());

      // Act
      CityCsvDto dto1 = reader.read();
      CityCsvDto dto2 = reader.read();
      CityCsvDto dto3 = reader.read();
      reader.close();

      // Assert
      assertThat(dto1).as("1件目のレコードが正しく読まれること").isNotNull();
      assertThat(dto1.getLgCode()).isEqualTo("011011");
      assertThat(dto1.getCityName()).isEqualTo("札幌市");
      assertThat(dto1.getWardName()).isEqualTo("中央区");
      assertThat(dto1.getCountryName()).isNull();

      assertThat(dto2).as("2件目の郡部レコードが正しく読まれること").isNotNull();
      assertThat(dto2.getLgCode()).isEqualTo("014605");
      assertThat(dto2.getCountryName()).isEqualTo("空知郡");
      assertThat(dto2.getCityName()).isEqualTo("南富良野町");
      assertThat(dto2.getWardName()).isNull();

      assertThat(dto3).as("末尾以降はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("クラスパスリソースのcities.csvを正しく全件読み込めること")
    void shouldReadFullClasspathCsv() throws Exception {
      // Arrange
      ClassPathResource resource = new ClassPathResource("data/cities.csv");
      CityCsvItemReader reader = new CityCsvItemReader(resource);
      reader.open(new ExecutionContext());

      // Act
      int count = 0;
      CityCsvDto dto;
      while ((dto = reader.read()) != null) {
        count++;
        assertThat(dto.getLgCode()).as("lgCodeが存在すること").isNotBlank();
      }
      reader.close();

      // Assert
      assertThat(count).as("サンプルCSVの件数（63件）が正しく読み取れること").isEqualTo(63);
    }
  }
}
