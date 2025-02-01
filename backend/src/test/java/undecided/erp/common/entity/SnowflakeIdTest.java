package undecided.erp.common.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SnowflakeIdTest")
class SnowflakeIdTest {

  @Nested
  @DisplayName("toBase36String メソッドのテスト")
  class ToBase36StringTest {

    @Test
    @DisplayName("正の値の場合、base36文字列を返す")
    void shouldReturnBase36StringForPositiveValue() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(123456789L);

      // Act
      String result = snowflakeId.toBase36String();

      // Assert
      assertThat(result).isEqualTo("21i3v9");
    }

    @Test
    @DisplayName("空のSnowflakeIdの場合、例外をスローする")
    void shouldThrowExceptionWhenValueIsEmpty() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.empty();

      // Act & Assert
      assertThatThrownBy(snowflakeId::toBase36String)
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("value is empty");
    }
  }

  @Nested
  @DisplayName("reconstruct メソッドのテスト")
  class ReconstructTest {

    @Test
    @DisplayName("指定された値がnullではない場合、SnowflakeIdオブジェクトを正しく再構築する")
    void shouldReconstructSnowflakeIdWithValue() {
      // Arrange
      Long value = 123456789L;

      // Act
      SnowflakeId result = SnowflakeId.reconstruct(value);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("指定された値がnullの場合、SnowflakeId.EMPTYを返す")
    void shouldReturnEmptySnowflakeIdWhenValueIsNull() {
      // Act
      SnowflakeId result = SnowflakeId.reconstruct(null);

      // Assert
      assertThat(result).isSameAs(SnowflakeId.empty());
    }
  }

  @Nested
  @DisplayName("empty メソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("empty() メソッドは常にSnowflakeId.EMPTYを返す")
    void shouldReturnEmptySnowflakeIdAlways() {
      // Act
      SnowflakeId result = SnowflakeId.empty();

      // Assert
      assertThat(result).isSameAs(SnowflakeId.EMPTY);
    }
  }
}
