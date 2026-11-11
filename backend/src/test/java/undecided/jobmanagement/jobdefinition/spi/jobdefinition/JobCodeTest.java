package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("JobCodeのテスト")
class JobCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      JobCode jobCode = JobCode.of("JOB001");

      assertThat(jobCode).isNotNull();
      assertThat(jobCode.getValue()).isEqualTo("JOB001");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> JobCode.of(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("JobCode cannot be null");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longCode = "a".repeat(51);
      assertThatThrownBy(() -> JobCode.of(longCode))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("JobCode cannot exceed 50 characters");
    }

    @Test
    @DisplayName("最大文字数ちょうどの場合インスタンスが作成される")
    void shouldCreateInstanceWhenExactlyMaxLength() {
      String exactCode = "a".repeat(50);
      JobCode jobCode = JobCode.of(exactCode);

      assertThat(jobCode).isNotNull();
      assertThat(jobCode.getValue()).hasSize(50);
    }
  }


  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のインスタンスは等しいと判定される")
    void shouldBeEqualWhenValuesMatch() {
      JobCode code1 = JobCode.of("JOB001");
      JobCode code2 = JobCode.of("JOB001");

      assertThat(code1).isEqualTo(code2);
      assertThat(code1.hashCode()).isEqualTo(code2.hashCode());
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないと判定される")
    void shouldBeNotEqualWhenValuesDiffer() {
      JobCode code1 = JobCode.of("JOB001");
      JobCode code2 = JobCode.of("JOB002");

      assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    @DisplayName("nullとは等しくない")
    void shouldBeNotEqualWithNull() {
      JobCode code = JobCode.of("JOB001");

      assertThat(code).isNotEqualTo(null);
    }

    @Test
    @DisplayName("異なるクラスとは等しくない")
    void shouldBeNotEqualWithDifferentClass() {
      JobCode code = JobCode.of("JOB001");

      assertThat(code).isNotEqualTo("JOB001");
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値が文字列として返される")
    void shouldReturnValueAsString() {
      JobCode jobCode = JobCode.of("JOB001");

      assertThat(jobCode.toString()).isEqualTo("JOB001");
    }

    @Test
    @DisplayName("空文字の値の場合は空文字が返される")
    void shouldReturnEmptyWhenValueIsEmpty() {
      JobCode jobCode = JobCode.of("");

      assertThat(jobCode.toString()).isEmpty();
    }
  }

  @Nested
  @DisplayName("MAX_LENGTH定数のテスト")
  class MaxLengthConstantTest {

    @Test
    @DisplayName("MAX_LENGTHは50である")
    void shouldHaveMaxLength50() {
      assertThat(JobCode.MAX_LENGTH).isEqualTo(50);
    }
  }
}
