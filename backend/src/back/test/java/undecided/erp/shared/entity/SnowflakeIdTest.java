package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SnowflakeIdTest - toBase36Stringメソッドのテスト")
class SnowflakeIdTest {

  @Nested
  @DisplayName("toBase36Stringメソッドのテスト")
  class ToBase36StringTest {

    @Test
    @DisplayName("有効な値が正しくBase36文字列に変換されることを確認する")
    void shouldConvertValidValueToBase36String() {
      long validValue = 123456789L;
      SnowflakeId snowflakeId = SnowflakeId.reconstruct(validValue);

      String result = snowflakeId.toBase36String();

      assertThat(result).isEqualTo(Long.toString(validValue, 36));
    }

    @Test
    @DisplayName("SnowflakeIdが空の場合、例外がスローされることを確認する")
    void shouldThrowExceptionWhenSnowflakeIdIsEmpty() {
      SnowflakeId snowflakeId = SnowflakeId.empty();

      assertThatThrownBy(snowflakeId::toBase36String)
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("value is empty");
    }
  }
}
