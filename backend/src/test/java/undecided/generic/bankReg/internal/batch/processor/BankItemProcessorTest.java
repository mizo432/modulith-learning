package undecided.generic.bankReg.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.bankReg.internal.batch.dto.BankCsvDto;
import undecided.generic.bankReg.spi.bank.Bank;

@Tag("small")
@DisplayName("BankItemProcessorのテスト")
class BankItemProcessorTest {

  private final BankItemProcessor processor = new BankItemProcessor();

  @Nested
  @DisplayName("processメソッドのテスト")
  class ProcessMethodTest {

    @Test
    @DisplayName("有効なCSVデータからBankエンティティを正常に変換すること")
    void shouldConvertValidCsvDtoToBankEntity() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("0001");
      dto.setBankName("みずほ銀行");
      dto.setBankHalfKana("ﾐｽﾞﾎﾎｳｷﾝ");
      dto.setBankFullKana("ミズホホウキン");
      dto.setBankFullHira("みずほほうきん");
      dto.setBusinessTypeCode("B001");
      dto.setBusinessType("都市銀行");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getBankCode()).isEqualTo("0001");
      assertThat(result.getBankName()).isEqualTo("みずほ銀行");
      assertThat(result.getBankHalfKana()).isEqualTo("ﾐｽﾞﾎﾎｳｷﾝ");
      assertThat(result.getBankFullKana()).isEqualTo("ミズホホウキン");
      assertThat(result.getBankFullHira()).isEqualTo("みずほほうきん");
      assertThat(result.getBusinessTypeCode()).isEqualTo("B001");
      assertThat(result.getBusinessType()).isEqualTo("都市銀行");
      assertThat(result.getDatasetId()).isEqualTo("dataset-20261001");
    }

    @Test
    @DisplayName("金融機関コードがnullの場合、nullを返すこと")
    void shouldReturnNullWhenBankCodeIsNull() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankName("みずほ銀行");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("金融機関コードが空文字の場合、nullを返すこと")
    void shouldReturnNullWhenBankCodeIsEmpty() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("");
      dto.setBankName("みずほ銀行");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("金融機関コードが4桁でない場合、nullを返すこと")
    void shouldReturnNullWhenBankCodeLengthIsInvalid() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("001");
      dto.setBankName("みずほ銀行");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).as("4桁でない金融機関コードはスキップされること").isNull();
    }

    @Test
    @DisplayName("金融機関名がnullの場合、nullを返すこと")
    void shouldReturnNullWhenBankNameIsNull() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("0001");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("金融機関名が空文字の場合、nullを返すこと")
    void shouldReturnNullWhenBankNameIsEmpty() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("0001");
      dto.setBankName("");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("オプションフィールドがnullの場合、nullとして設定すること")
    void shouldSetNullForNullOptionalFields() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("0001");
      dto.setBankName("みずほ銀行");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getBankHalfKana()).isNull();
      assertThat(result.getBankFullKana()).isNull();
      assertThat(result.getBankFullHira()).isNull();
      assertThat(result.getBusinessTypeCode()).isNull();
      assertThat(result.getBusinessType()).isNull();
    }

    @Test
    @DisplayName("空白のオプションフィールドはnullとして設定すること")
    void shouldSetNullForBlankOptionalFields() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode("0001");
      dto.setBankName("みずほ銀行");
      dto.setBankHalfKana("   ");
      dto.setBankFullKana("  ");
      dto.setBankFullHira("");
      dto.setBusinessTypeCode(" ");
      dto.setBusinessType("  ");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getBankHalfKana()).isNull();
      assertThat(result.getBankFullKana()).isNull();
      assertThat(result.getBankFullHira()).isNull();
      assertThat(result.getBusinessTypeCode()).isNull();
      assertThat(result.getBusinessType()).isNull();
    }

    @Test
    @DisplayName("前後の空白をトリムして設定すること")
    void shouldTrimWhitespaceFromFields() {
      // Arrange
      processor.datasetId = "dataset-20261001";
      BankCsvDto dto = new BankCsvDto();
      dto.setBankCode(" 0001 ");
      dto.setBankName(" みずほ銀行 ");
      dto.setBankHalfKana(" ﾐｽﾞﾎﾎｳｷﾝ ");
      dto.setBankFullKana(" ミズホホウキン ");
      dto.setBankFullHira(" みずほほうきん ");
      dto.setBusinessTypeCode(" B001 ");
      dto.setBusinessType(" 都市銀行 ");

      // Act
      Bank result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getBankCode()).isEqualTo("0001");
      assertThat(result.getBankName()).isEqualTo("みずほ銀行");
      assertThat(result.getBankHalfKana()).isEqualTo("ﾐｽﾞﾎﾎｳｷﾝ");
      assertThat(result.getBankFullKana()).isEqualTo("ミズホホウキン");
      assertThat(result.getBankFullHira()).isEqualTo("みずほほうきん");
      assertThat(result.getBusinessTypeCode()).isEqualTo("B001");
      assertThat(result.getBusinessType()).isEqualTo("都市銀行");
    }

    @Test
    @DisplayName("引数がnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenItemIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> processor.process(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("item must not be null");
    }
  }
}
