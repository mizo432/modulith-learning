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
      assertThat(result.value()).isEqualTo(value);
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

  @Nested
  @DisplayName("newInstance メソッドのテスト")
  class NewInstanceMethodTest {

    @Test
    @DisplayName("newInstance() メソッドは新しいSnowflakeIdを返し、値が空ではない")
    void shouldReturnNewNonEmptySnowflakeId() {
      // Act
      SnowflakeId result = SnowflakeId.newInstance();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("newInstance() メソッドの呼び出しごとに一意の値を生成する")
    void shouldGenerateUniqueSnowflakeIds() {
      // Act
      SnowflakeId firstInstance = SnowflakeId.newInstance();
      SnowflakeId secondInstance = SnowflakeId.newInstance();

      // Assert
      assertThat(firstInstance).isNotEqualTo(secondInstance);
      assertThat(firstInstance.value()).isNotEqualTo(secondInstance.value());
    }
  }

  @Nested
  @DisplayName("of メソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("正の値を渡すと、正しいSnowflakeIdオブジェクトを生成する")
    void shouldCreateSnowflakeIdForPositiveValue() {
      // Arrange
      Long value = 123456789L;

      // Act
      SnowflakeId result = SnowflakeId.of(value);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("nullを渡すと例外をスローする")
    void shouldThrowExceptionForNullValue() {
      // Act & Assert
      assertThatThrownBy(() -> SnowflakeId.of(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("value is marked non-null but is null");
    }
  }
}
