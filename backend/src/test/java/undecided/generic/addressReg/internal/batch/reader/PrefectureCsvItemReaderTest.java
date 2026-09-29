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
import undecided.generic.addressReg.internal.batch.dto.PrefectureCsvDto;

@Tag("small")
@DisplayName("PrefectureCsvItemReaderのテスト")
class PrefectureCsvItemReaderTest {

  @Nested
  @DisplayName("openおよびreadメソッドのテスト")
  class OpenAndReadTest {

    @Test
    @DisplayName("日本語ヘッダーのCSVを正しく読み込めること")
    void shouldReadJapaneseHeaderCsvCorrectly() throws Exception {
      // Arrange
      String csvContent =
          """
          全国地方公共団体コード,都道府県名,都道府県名_カナ,都道府県名_英字,効力発生日,廃止日,備考
          010006,北海道,ホッカイドウ,Hokkaido,1947-04-17,,
          130001,東京都,トウキョウト,Tokyo,1947-04-17,9999-12-31,首都
          """;
      ByteArrayResource resource =
          new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
      PrefectureCsvItemReader reader = new PrefectureCsvItemReader(resource);
      ExecutionContext executionContext = new ExecutionContext();

      // Act
      reader.open(executionContext);
      PrefectureCsvDto item1 = reader.read();
      PrefectureCsvDto item2 = reader.read();
      PrefectureCsvDto item3 = reader.read();
      reader.close();

      // Assert
      assertThat(item1).isNotNull();
      assertThat(item1.getLgCode()).isEqualTo("010006");
      assertThat(item1.getPrefName()).isEqualTo("北海道");
      assertThat(item1.getPrefKana()).isEqualTo("ホッカイドウ");
      assertThat(item1.getPrefRoma()).isEqualTo("Hokkaido");
      assertThat(item1.getEffectiveDate()).isEqualTo("1947-04-17");
      assertThat(item1.getAbolitionDate()).isNull();
      assertThat(item1.getRemarks()).isNull();

      assertThat(item2).isNotNull();
      assertThat(item2.getLgCode()).isEqualTo("130001");
      assertThat(item2.getPrefName()).isEqualTo("東京都");
      assertThat(item2.getPrefKana()).isEqualTo("トウキョウト");
      assertThat(item2.getPrefRoma()).isEqualTo("Tokyo");
      assertThat(item2.getEffectiveDate()).isEqualTo("1947-04-17");
      assertThat(item2.getAbolitionDate()).isEqualTo("9999-12-31");
      assertThat(item2.getRemarks()).isEqualTo("首都");

      assertThat(item3).isNull();
    }

    @Test
    @DisplayName("英語ヘッダーのCSVを正しく読み込めること")
    void shouldReadEnglishHeaderCsvCorrectly() throws Exception {
      // Arrange
      String csvContent =
          """
          lgCode,prefName,prefKana,prefRoma,effectiveDate,abolitionDate,remarks
          270008,大阪府,オオサカフ,Osaka,1947-04-17,,近畿
          """;
      ByteArrayResource resource =
          new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
      PrefectureCsvItemReader reader = new PrefectureCsvItemReader(resource);
      ExecutionContext executionContext = new ExecutionContext();

      // Act
      reader.open(executionContext);
      PrefectureCsvDto item = reader.read();
      PrefectureCsvDto eof = reader.read();
      reader.close();

      // Assert
      assertThat(item).isNotNull();
      assertThat(item.getLgCode()).isEqualTo("270008");
      assertThat(item.getPrefName()).isEqualTo("大阪府");
      assertThat(item.getPrefKana()).isEqualTo("オオサカフ");
      assertThat(item.getPrefRoma()).isEqualTo("Osaka");
      assertThat(item.getEffectiveDate()).isEqualTo("1947-04-17");
      assertThat(item.getAbolitionDate()).isNull();
      assertThat(item.getRemarks()).isEqualTo("近畿");

      assertThat(eof).isNull();
    }

    @Test
    @DisplayName("リソースがnullの場合、open時にItemStreamExceptionをスローすること")
    void shouldThrowItemStreamExceptionWhenResourceIsNull() {
      // Arrange
      PrefectureCsvItemReader reader = new PrefectureCsvItemReader(null);
      ExecutionContext executionContext = new ExecutionContext();

      // Act & Assert
      assertThatThrownBy(() -> reader.open(executionContext))
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Resource must not be null");
    }

    @Test
    @DisplayName("open前にreadを呼び出した場合、ItemStreamExceptionをスローすること")
    void shouldThrowItemStreamExceptionWhenReadBeforeOpen() {
      // Arrange
      PrefectureCsvItemReader reader = new PrefectureCsvItemReader();

      // Act & Assert
      assertThatThrownBy(reader::read)
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }
  }
}
