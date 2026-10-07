package undecided.generic.bankReg.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.generic.bankReg.internal.BranchRepository;
import undecided.generic.bankReg.internal.batch.dto.BranchCsvDto;
import undecided.generic.bankReg.spi.branch.Branch;

@Tag("small")
@DisplayName("BranchItemProcessorのテスト")
@ExtendWith(MockitoExtension.class)
class BranchItemProcessorTest {

  @Mock private BranchRepository branchRepository;

  @InjectMocks private BranchItemProcessor processor;

  @Nested
  @DisplayName("processメソッドのテスト")
  class ProcessMethodTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenItemIsNull() {
      assertThatThrownBy(() -> processor.process(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("item must not be null");
    }

    @Test
    @DisplayName("bankCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenBankCodeIsNull() {
      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode(null);
      dto.setBranchCode("001");
      dto.setBranchName("東京営業部");

      Branch result = processor.process(dto);

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("bankCodeが空の場合、nullを返すこと")
    void shouldReturnNullWhenBankCodeIsBlank() {
      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("  ");
      dto.setBranchCode("001");
      dto.setBranchName("東京営業部");

      Branch result = processor.process(dto);

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("branchCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenBranchCodeIsNull() {
      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("0001");
      dto.setBranchCode(null);
      dto.setBranchName("東京営業部");

      Branch result = processor.process(dto);

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("新規支店が正しく作成されること")
    void shouldCreateNewBranchWhenNotExists() {
      processor.setDatasetId("dataset-001");

      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("0001");
      dto.setBranchCode("001");
      dto.setBranchName("東京営業部");
      dto.setBranchHalfKana("ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ");
      dto.setBranchFullKana("トウキヨウエイギヨウブ");
      dto.setBranchHiragana("とうきようえいぎようぶ");

      Branch.BranchId id = new Branch.BranchId();
      id.setBankCode("0001");
      id.setBranchCode("001");
      when(branchRepository.findById(id)).thenReturn(Optional.empty());

      Branch result = processor.process(dto);

      assertThat(result).isNotNull();
      assertThat(result.getBankCode()).isEqualTo("0001");
      assertThat(result.getBranchCode()).isEqualTo("001");
      assertThat(result.getBranchName()).isEqualTo("東京営業部");
      assertThat(result.getBranchHalfKana()).isEqualTo("ﾄｳｷﾖｳｴｲｷﾞﾖｳﾌﾞ");
      assertThat(result.getBranchFullKana()).isEqualTo("トウキヨウエイギヨウブ");
      assertThat(result.getBranchHiragana()).isEqualTo("とうきようえいぎようぶ");
      assertThat(result.getDatasetId()).isEqualTo("dataset-001");
    }

    @Test
    @DisplayName("既存支店が更新されること")
    void shouldUpdateExistingBranch() {
      processor.setDatasetId("dataset-002");

      Branch existing = new Branch();
      existing.setBankCode("0001");
      existing.setBranchCode("001");
      existing.setBranchName("旧名称");
      existing.setDatasetId("dataset-001");

      Branch.BranchId id = new Branch.BranchId();
      id.setBankCode("0001");
      id.setBranchCode("001");
      when(branchRepository.findById(id)).thenReturn(Optional.of(existing));

      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("0001");
      dto.setBranchCode("001");
      dto.setBranchName("新名称");
      dto.setBranchHalfKana(null);
      dto.setBranchFullKana(null);
      dto.setBranchHiragana(null);

      Branch result = processor.process(dto);

      assertThat(result).isSameAs(existing);
      assertThat(result.getBranchName()).isEqualTo("新名称");
      assertThat(result.getDatasetId()).isEqualTo("dataset-002");
    }

    @Test
    @DisplayName("空白文字列のbranchNameは空文字列になること")
    void shouldSetEmptyStringForBlankBranchName() {
      processor.setDatasetId("dataset-001");

      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("0001");
      dto.setBranchCode("001");
      dto.setBranchName("   ");

      Branch.BranchId id = new Branch.BranchId();
      id.setBankCode("0001");
      id.setBranchCode("001");
      when(branchRepository.findById(id)).thenReturn(Optional.empty());

      Branch result = processor.process(dto);

      assertThat(result.getBranchName()).isEqualTo("");
    }

    @Test
    @DisplayName("nullのdatasetIdの場合、空文字列が設定されること")
    void shouldSetEmptyDatasetIdWhenNull() {
      processor.setDatasetId(null);

      BranchCsvDto dto = new BranchCsvDto();
      dto.setBankCode("0001");
      dto.setBranchCode("001");
      dto.setBranchName("東京営業部");

      Branch.BranchId id = new Branch.BranchId();
      id.setBankCode("0001");
      id.setBranchCode("001");
      when(branchRepository.findById(id)).thenReturn(Optional.empty());

      Branch result = processor.process(dto);

      assertThat(result.getDatasetId()).isEqualTo("");
    }
  }
}
