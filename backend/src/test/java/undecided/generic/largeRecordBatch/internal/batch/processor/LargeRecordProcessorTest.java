package undecided.generic.largeRecordBatch.internal.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.largeRecordBatch.internal.batch.dto.LargeRecordCsvDto;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

@Tag("small")
@DisplayName("LargeRecordProcessorのテスト")
class LargeRecordProcessorTest {

  private final LargeRecordProcessor processor = new LargeRecordProcessor();

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
    @DisplayName("recordCodeが空の場合、nullを返すこと")
    void shouldReturnNullWhenRecordCodeIsEmpty() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("");
      dto.setRecordName("テストレコード");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("recordCodeがnullの場合、nullを返すこと")
    void shouldReturnNullWhenRecordCodeIsNull() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode(null);
      dto.setRecordName("テストレコード");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("recordNameが空の場合、nullを返すこと")
    void shouldReturnNullWhenRecordNameIsEmpty() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC001");
      dto.setRecordName("");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("正常なデータの場合、LargeRecordエンティティを生成すること")
    void shouldCreateLargeRecordWhenDataIsValid() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000001");
      dto.setRecordName("サンプルレコード1");
      dto.setCategory("カテゴリA");
      dto.setSubCategory("サブ1");
      dto.setAmount("10000");
      dto.setQuantity("100");
      dto.setStatus("ACTIVE");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getRecordCode()).isEqualTo("REC000001");
      assertThat(result.getRecordName()).isEqualTo("サンプルレコード1");
      assertThat(result.getCategory()).isEqualTo("カテゴリA");
      assertThat(result.getSubCategory()).isEqualTo("サブ1");
      assertThat(result.getAmount()).isEqualTo(10000L);
      assertThat(result.getQuantity()).isEqualTo(100);
      assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("金額が空の場合、nullとして処理すること")
    void shouldSetAmountNullWhenAmountIsEmpty() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000002");
      dto.setRecordName("サンプルレコード2");
      dto.setAmount("");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getAmount()).isNull();
    }

    @Test
    @DisplayName("数量が空の場合、nullとして処理すること")
    void shouldSetQuantityNullWhenQuantityIsEmpty() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000003");
      dto.setRecordName("サンプルレコード3");
      dto.setQuantity("");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getQuantity()).isNull();
    }

    @Test
    @DisplayName("金額が数値に変換できない場合、nullとして処理すること（フォールトトレランス）")
    void shouldSetAmountNullWhenAmountIsInvalid() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000004");
      dto.setRecordName("サンプルレコード4");
      dto.setAmount("invalid");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getAmount()).isNull();
    }

    @Test
    @DisplayName("数量が数値に変換できない場合、nullとして処理すること（フォールトトレランス）")
    void shouldSetQuantityNullWhenQuantityIsInvalid() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000005");
      dto.setRecordName("サンプルレコード5");
      dto.setQuantity("invalid");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getQuantity()).isNull();
    }

    @Test
    @DisplayName("カテゴリがnullの場合、空文字列として処理すること")
    void shouldSetCategoryEmptyWhenCategoryIsNull() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000006");
      dto.setRecordName("サンプルレコード6");
      dto.setCategory(null);

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getCategory()).isEmpty();
    }

    @Test
    @DisplayName("ステータスがnullの場合、ACTIVEをデフォルトで設定すること")
    void shouldSetStatusActiveWhenStatusIsNull() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000007");
      dto.setRecordName("サンプルレコード7");
      dto.setStatus(null);

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("ステータスが不正な値の場合、ACTIVEをデフォルトで設定すること")
    void shouldSetStatusActiveWhenStatusIsInvalid() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000008");
      dto.setRecordName("サンプルレコード8");
      dto.setStatus("UNKNOWN");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("ステータスが小文字の場合、大文字に正規化すること")
    void shouldNormalizeStatusToUpperCase() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000009");
      dto.setRecordName("サンプルレコード9");
      dto.setStatus("active");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("INACTIVEステータスが正常に設定されること")
    void shouldSetStatusInactiveWhenValid() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000010");
      dto.setRecordName("サンプルレコード10");
      dto.setStatus("INACTIVE");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getStatus()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("空白のrecordCodeはトリム後も空とみなしてnullを返すこと")
    void shouldReturnNullWhenRecordCodeIsBlank() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("   ");
      dto.setRecordName("テストレコード");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("空白のrecordNameはトリム後も空とみなしてnullを返すこと")
    void shouldReturnNullWhenRecordNameIsBlank() {
      // Arrange
      LargeRecordCsvDto dto = new LargeRecordCsvDto();
      dto.setRecordCode("REC000011");
      dto.setRecordName("   ");

      // Act
      LargeRecord result = processor.process(dto);

      // Assert
      assertThat(result).isNull();
    }
  }
}
