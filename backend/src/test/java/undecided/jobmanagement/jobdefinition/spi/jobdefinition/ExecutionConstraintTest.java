package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ExecutionConstraintのテスト")
class ExecutionConstraintTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な値を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidValues() {
      ExecutionConstraint constraint = ExecutionConstraint.create(5, 300);

      assertThat(constraint).isNotNull();
      assertThat(constraint.getMaxConcurrentExecutions()).isEqualTo(5);
      assertThat(constraint.getTimeoutSeconds()).isEqualTo(300);
    }

    @Test
    @DisplayName("null値を指定してもインスタンスが作成される")
    void shouldCreateInstanceWhenNullValues() {
      ExecutionConstraint constraint = ExecutionConstraint.create(null, null);

      assertThat(constraint).isNotNull();
      assertThat(constraint.getMaxConcurrentExecutions()).isNull();
      assertThat(constraint.getTimeoutSeconds()).isNull();
    }

    @Test
    @DisplayName("片方のみnullでもインスタンスが作成される")
    void shouldCreateInstanceWhenOneNull() {
      ExecutionConstraint constraint = ExecutionConstraint.create(5, null);

      assertThat(constraint).isNotNull();
      assertThat(constraint.getMaxConcurrentExecutions()).isEqualTo(5);
      assertThat(constraint.getTimeoutSeconds()).isNull();
    }
  }


  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のインスタンスは等しいと判定される")
    void shouldBeEqualWhenValuesMatch() {
      ExecutionConstraint c1 = ExecutionConstraint.create(5, 300);
      ExecutionConstraint c2 = ExecutionConstraint.create(5, 300);

      assertThat(c1).isEqualTo(c2);
      assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないと判定される")
    void shouldBeNotEqualWhenValuesDiffer() {
      ExecutionConstraint c1 = ExecutionConstraint.create(5, 300);
      ExecutionConstraint c2 = ExecutionConstraint.create(10, 600);

      assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    @DisplayName("null値のインスタンス同士は等しいと判定される")
    void shouldBeEqualWhenBothNull() {
      ExecutionConstraint c1 = ExecutionConstraint.create(null, null);
      ExecutionConstraint c2 = ExecutionConstraint.create(null, null);

      assertThat(c1).isEqualTo(c2);
    }
  }
}
