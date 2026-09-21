package undecided.erp.shared.supporting.snowflake.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("NodeIdクラスのテスト")
@Tag("small")
class NodeIdTest {

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethod {

    @Test
    @DisplayName("valueがnullの場合、'null'を返すべき")
    void shouldReturnNullWhenValueIsNull() {
      // Arrange
      NodeId nodeId = new NodeId(null);

      // Act
      String result = nodeId.toString();

      // Assert
      assertThat(result).isEqualTo("null");
    }

    @Test
    @DisplayName("valueが正の整数の場合、その値を文字列として返すべき")
    void shouldReturnStringValueWhenValueIsPositive() {
      // Arrange
      NodeId nodeId = new NodeId(123);

      // Act
      String result = nodeId.toString();

      // Assert
      assertThat(result).isEqualTo("123");
    }

    @Test
    @DisplayName("valueが負の整数の場合、その値を文字列として返すべき")
    void shouldReturnStringValueWhenValueIsNegative() {
      // Arrange
      NodeId nodeId = new NodeId(-456);

      // Act
      String result = nodeId.toString();

      // Assert
      assertThat(result).isEqualTo("-456");
    }

    @Test
    @DisplayName("valueが0の場合、'0'を返すべき")
    void shouldReturnStringValueWhenValueIsZero() {
      // Arrange
      NodeId nodeId = new NodeId(0);

      // Act
      String result = nodeId.toString();

      // Assert
      assertThat(result).isEqualTo("0");
    }
  }
}
