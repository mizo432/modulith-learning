package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("JobVersionのテスト")
class JobVersionTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      JobVersion jobVersion = JobVersion.of("1.0.0");

      assertThat(jobVersion).isNotNull();
      assertThat(jobVersion.getValue()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> JobVersion.of(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("JobVersion cannot be null");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longVersion = "a".repeat(21);
      assertThatThrownBy(() -> JobVersion.of(longVersion))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("JobVersion cannot exceed 20 characters");
    }

    @Test
    @DisplayName("最大文字数ちょうどの場合インスタンスが作成される")
    void shouldCreateInstanceWhenExactlyMaxLength() {
      String exactVersion = "a".repeat(20);
      JobVersion jobVersion = JobVersion.of(exactVersion);

      assertThat(jobVersion).isNotNull();
      assertThat(jobVersion.getValue()).hasSize(20);
    }
  }


  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のインスタンスは等しいと判定される")
    void shouldBeEqualWhenValuesMatch() {
      JobVersion ver1 = JobVersion.of("1.0.0");
      JobVersion ver2 = JobVersion.of("1.0.0");

      assertThat(ver1).isEqualTo(ver2);
      assertThat(ver1.hashCode()).isEqualTo(ver2.hashCode());
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないと判定される")
    void shouldBeNotEqualWhenValuesDiffer() {
      JobVersion ver1 = JobVersion.of("1.0.0");
      JobVersion ver2 = JobVersion.of("2.0.0");

      assertThat(ver1).isNotEqualTo(ver2);
    }

    @Test
    @DisplayName("nullとは等しくない")
    void shouldBeNotEqualWithNull() {
      JobVersion ver = JobVersion.of("1.0.0");

      assertThat(ver).isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値が文字列として返される")
    void shouldReturnValueAsString() {
      JobVersion jobVersion = JobVersion.of("1.0.0");

      assertThat(jobVersion.toString()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("空文字の値の場合は空文字が返される")
    void shouldReturnEmptyWhenValueIsEmpty() {
      JobVersion jobVersion = JobVersion.of("");

      assertThat(jobVersion.toString()).isEmpty();
    }
  }

  @Nested
  @DisplayName("MAX_LENGTH定数のテスト")
  class MaxLengthConstantTest {

    @Test
    @DisplayName("MAX_LENGTHは20である")
    void shouldHaveMaxLength20() {
      assertThat(JobVersion.MAX_LENGTH).isEqualTo(20);
    }
  }
}
