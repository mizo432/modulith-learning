package undecided.generic.bankReg.internal.batch.reader;

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
import undecided.generic.bankReg.internal.batch.dto.BranchCsvDto;

@Tag("small")
@DisplayName("BranchCsvItemReaderのテスト")
class BranchCsvItemReaderTest {

  @Nested
  @DisplayName("openメソッドのテスト")
  class OpenMethodTest {

    @Test
    @DisplayName("resourceがnullの場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenResourceIsNull() {
      BranchCsvItemReader reader = new BranchCsvItemReader();

      assertThatThrownBy(() -> reader.open(new ExecutionContext()))
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Resource must not be null");
    }

    @Test
    @DisplayName("有効なCSVリソースで正常にオープンすること")
    void shouldOpenSuccessfullyWithValidCsv() throws Exception {
      String csv =
          "bankCode,branchCode,branchName,branchHalfKana,branchFullKana,branchHiragana\n"
              + "0001,001,東京営業部,ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ,トウキヨウエイギヨウブ,とうきようえいぎようぶ\n";
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

      BranchCsvItemReader reader = new BranchCsvItemReader(resource);
      reader.open(new ExecutionContext());

      BranchCsvDto dto = reader.read();
      assertThat(dto).isNotNull();
      assertThat(dto.getBankCode()).isEqualTo("0001");
      assertThat(dto.getBranchCode()).isEqualTo("001");
      assertThat(dto.getBranchName()).isEqualTo("東京営業部");

      reader.close();
    }
  }

  @Nested
  @DisplayName("readメソッドのテスト")
  class ReadMethodTest {

    @Test
    @DisplayName("open前にreadした場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenReadBeforeOpen() {
      BranchCsvItemReader reader = new BranchCsvItemReader();

      assertThatThrownBy(() -> reader.read())
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }

    @Test
    @DisplayName("CSVデータが正しく読み込まれること")
    void shouldReadCsvDataCorrectly() throws Exception {
      String csv =
          "bankCode,branchCode,branchName,branchHalfKana,branchFullKana,branchHiragana\n"
              + "0001,001,東京営業部,ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ,トウキヨウエイギヨウブ,とうきようえいぎようぶ\n"
              + "0001,004,丸の内中央支店,ﾏﾙﾉｳﾁﾁﾕｳｵｳｼﾃﾝ,マルノウチチユウオウシテン,まるのうちちゆうおうしてん\n";
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

      BranchCsvItemReader reader = new BranchCsvItemReader(resource);
      reader.open(new ExecutionContext());

      BranchCsvDto dto1 = reader.read();
      assertThat(dto1).isNotNull();
      assertThat(dto1.getBankCode()).isEqualTo("0001");
      assertThat(dto1.getBranchCode()).isEqualTo("001");
      assertThat(dto1.getBranchName()).isEqualTo("東京営業部");

      BranchCsvDto dto2 = reader.read();
      assertThat(dto2).isNotNull();
      assertThat(dto2.getBankCode()).isEqualTo("0001");
      assertThat(dto2.getBranchCode()).isEqualTo("004");
      assertThat(dto2.getBranchName()).isEqualTo("丸の内中央支店");

      BranchCsvDto dto3 = reader.read();
      assertThat(dto3).isNull();

      reader.close();
    }

    @Test
    @DisplayName("日本語の支店名が正しく読み込まれること")
    void shouldReadJapaneseBranchNames() throws Exception {
      String csv =
          "bankCode,branchCode,branchName,branchHalfKana,branchFullKana,branchHiragana\n"
              + "0001,001,東京営業部,ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ,トウキヨウエイギヨウブ,とうきようえいぎようぶ\n";
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

      BranchCsvItemReader reader = new BranchCsvItemReader(resource);
      reader.open(new ExecutionContext());

      BranchCsvDto dto = reader.read();
      assertThat(dto).isNotNull();
      assertThat(dto.getBankCode()).isEqualTo("0001");
      assertThat(dto.getBranchCode()).isEqualTo("001");
      assertThat(dto.getBranchName()).isEqualTo("東京営業部");
      assertThat(dto.getBranchHalfKana()).isEqualTo("ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ");
      assertThat(dto.getBranchFullKana()).isEqualTo("トウキヨウエイギヨウブ");
      assertThat(dto.getBranchHiragana()).isEqualTo("とうきようえいぎようぶ");

      reader.close();
    }
  }

  @Nested
  @DisplayName("closeメソッドのテスト")
  class CloseMethodTest {

    @Test
    @DisplayName("close後にreadした場合、ItemStreamExceptionをスローすること")
    void shouldThrowExceptionWhenReadAfterClose() throws Exception {
      String csv = "bankCode,branchCode,branchName\n" + "0001,001,東京営業部\n";
      ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

      BranchCsvItemReader reader = new BranchCsvItemReader(resource);
      reader.open(new ExecutionContext());
      reader.close();

      assertThatThrownBy(() -> reader.read())
          .isInstanceOf(ItemStreamException.class)
          .hasMessageContaining("Reader is not open");
    }
  }
}
