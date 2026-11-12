package undecided.jobmanagement.jobexecution.spi.jobexecution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ExecutionIdのテスト")
class ExecutionIdTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      ExecutionId executionId = ExecutionId.of("EXEC-001");

      assertThat(executionId).isNotNull();
      assertThat(executionId.getValue()).isEqualTo("EXEC-001");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> ExecutionId.of(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("ExecutionId cannot be null");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longId = "a".repeat(51);
      assertThatThrownBy(() -> ExecutionId.of(longId))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("ExecutionId cannot exceed 50 characters");
    }

    @Test
    @DisplayName("最大文字数ちょうどの場合インスタンスが作成される")
    void shouldCreateInstanceWhenExactlyMaxLength() {
      String exactId = "a".repeat(50);
      ExecutionId executionId = ExecutionId.of(exactId);

      assertThat(executionId).isNotNull();
      assertThat(executionId.getValue()).hasSize(50);
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のインスタンスは等しいと判定される")
    void shouldBeEqualWhenValuesMatch() {
      ExecutionId id1 = ExecutionId.of("EXEC-001");
      ExecutionId id2 = ExecutionId.of("EXEC-001");

      assertThat(id1).isEqualTo(id2);
      assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないと判定される")
    void shouldBeNotEqualWhenValuesDiffer() {
      ExecutionId id1 = ExecutionId.of("EXEC-001");
      ExecutionId id2 = ExecutionId.of("EXEC-002");

      assertThat(id1).isNotEqualTo(id2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値が文字列として返される")
    void shouldReturnValueAsString() {
      ExecutionId executionId = ExecutionId.of("EXEC-001");

      assertThat(executionId.toString()).isEqualTo("EXEC-001");
    }

    @Test
    @DisplayName("空文字の値の場合は空文字が返される")
    void shouldReturnEmptyWhenValueIsEmpty() {
      ExecutionId executionId = ExecutionId.of("");

      assertThat(executionId.toString()).isEmpty();
    }
  }

  @Nested
  @DisplayName("MAX_LENGTH定数のテスト")
  class MaxLengthConstantTest {

    @Test
    @DisplayName("MAX_LENGTHは50である")
    void shouldHaveMaxLength50() {
      assertThat(ExecutionId.MAX_LENGTH).isEqualTo(50);
    }
  }
}
