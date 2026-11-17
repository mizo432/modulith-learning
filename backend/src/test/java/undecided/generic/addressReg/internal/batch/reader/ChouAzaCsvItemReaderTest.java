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
import undecided.generic.addressReg.internal.batch.dto.ChouAzaCsvDto;

@Tag("small")
@DisplayName("ChouAzaCsvItemReaderのテスト")
class ChouAzaCsvItemReaderTest {

  @Nested
  @DisplayName("openおよびreadメソッドのテスト")
  class OpenAndReadTest {

    @Test
    @DisplayName("Resourceがnullの状態でopenした場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenResourceIsNull() {
      // Arrange
      ChouAzaCsvItemReader reader = new ChouAzaCsvItemReader();

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
      ChouAzaCsvItemReader reader =
          new ChouAzaCsvItemReader(new ByteArrayResource("test".getBytes(StandardCharsets.UTF_8)));

      // Act & Assert
      assertThatThrownBy(reader::read)
          .as("open前のread実行はItemStreamExceptionをスローすること")
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }

    @Test
    @DisplayName("日本語ヘッダー付きCSVからChouAzaCsvDtoを正しく読み込めること")
    void shouldReadChouAzaCsvDtoCorrectly() throws Exception {
      // Arrange
      String csv =
          """
          全国地方公共団体コード,町字id,町字区分コード,大字・町名,大字・町名_カナ,大字・町名_英字,丁目名,丁目名_カナ,丁目名_数字,小字名,小字名_カナ,小字名_英字,同名町字識別コード,住居表示フラグ,住居表示方式コード,大字・町名_通称フラグ,小字_通称フラグ,大字・町名_電子国土基本図外字,小字_電子国土基本図外字,状態フラグ,起番フラグ,原典資料コード,効力発生日,廃止日,備考
          011011,0001001,1,旭ケ丘,アサヒガオカ,Asahigaoka,１丁目,１チョウメ,1,,,,0,1,1,0,0,,,0,0,1,1947-04-17,,
          131016,0004000,1,千代田,チヨダ,Chiyoda,,,,,,,0,0,,0,0,,,0,0,1,1947-04-17,,
          """;
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
      ChouAzaCsvItemReader reader = new ChouAzaCsvItemReader(resource);
      reader.open(new ExecutionContext());

      // Act
      ChouAzaCsvDto dto1 = reader.read();
      ChouAzaCsvDto dto2 = reader.read();
      ChouAzaCsvDto dto3 = reader.read();
      reader.close();

      // Assert
      assertThat(dto1).as("1件目のレコードが正しく読まれること").isNotNull();
      assertThat(dto1.getLgCode()).isEqualTo("011011");
      assertThat(dto1.getMachiazaCode()).isEqualTo("0001001");
      assertThat(dto1.getOazaChoName()).isEqualTo("旭ケ丘");
      assertThat(dto1.getChomeName()).isEqualTo("１丁目");
      assertThat(dto1.getRsdtAddrFlg()).isEqualTo("1");

      assertThat(dto2).as("2件目のレコードが正しく読まれること").isNotNull();
      assertThat(dto2.getLgCode()).isEqualTo("131016");
      assertThat(dto2.getMachiazaCode()).isEqualTo("0004000");
      assertThat(dto2.getOazaChoName()).isEqualTo("千代田");
      assertThat(dto2.getChomeName()).isNull();
      assertThat(dto2.getRsdtAddrFlg()).isEqualTo("0");

      assertThat(dto3).as("末尾以降はnullを返すこと").isNull();
    }

    @Test
    @DisplayName("updateメソッドが正常に完了すること")
    void shouldUpdateWithoutException() {
      // Arrange
      ChouAzaCsvItemReader reader = new ChouAzaCsvItemReader();
      ExecutionContext executionContext = new ExecutionContext();

      // Act & Assert
      reader.update(executionContext);
      assertThat(executionContext.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("closeが複数回呼ばれても安全に完了すること")
    void shouldCloseMultipleTimesSafely() throws Exception {
      // Arrange
      String csv = "全国地方公共団体コード,町字id\n011011,0001001\n";
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
      ChouAzaCsvItemReader reader = new ChouAzaCsvItemReader(resource);
      reader.open(new ExecutionContext());

      // Act & Assert
      reader.close();
      reader.close(); // 2回目のクローズでも例外が発���しないこと
    }

    @Test
    @DisplayName("クラスパスリソースのchou_aza.csvを正しく全件読み込めること")
    void shouldReadFullClasspathCsv() throws Exception {
      // Arrange
      ClassPathResource resource = new ClassPathResource("data/chou_aza.csv");
      ChouAzaCsvItemReader reader = new ChouAzaCsvItemReader(resource);
      reader.open(new ExecutionContext());

      // Act
      int count = 0;
      ChouAzaCsvDto dto;
      while ((dto = reader.read()) != null) {
        count++;
        assertThat(dto.getLgCode()).as("lgCodeが存在すること").isNotBlank();
        assertThat(dto.getMachiazaCode()).as("machiazaCodeが存在すること").isNotBlank();
      }
      reader.close();

      // Assert
      assertThat(count).as("サンプルCSVの件数（16件）が正しく読み取れること").isEqualTo(16);
    }
  }
}
