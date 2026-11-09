package undecided.jobmanagement.jobdefinition.spi.jobdefinition;

import com.github.mizo432.shared.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ParameterSpecのテスト")
class ParameterSpecTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      ParameterSpec paramSpec = ParameterSpec.of("--input file.txt");

      assertThat(paramSpec).isNotNull();
      assertThat(paramSpec.getValue()).isEqualTo("--input file.txt");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> ParameterSpec.of(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("ParameterSpec cannot be null");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longSpec = "a".repeat(1001);
      assertThatThrownBy(() -> ParameterSpec.of(longSpec))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("ParameterSpec cannot exceed 1000 characters");
    }

    @Test
    @DisplayName("最大文字数ちょうどの場合インスタンスが作成される")
    void shouldCreateInstanceWhenExactlyMaxLength() {
      String exactSpec = "a".repeat(1000);
      ParameterSpec paramSpec = ParameterSpec.of(exactSpec);

      assertThat(paramSpec).isNotNull();
      assertThat(paramSpec.getValue()).hasSize(1000);
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      ParameterSpec paramSpec = new ParameterSpec();

      assertThat(paramSpec).isNotNull();
      assertThat(paramSpec.getValue()).isNull();
    }
  }

  @Nested
  @DisplayName("パブリックコンストラクタのテスト")
  class PublicConstructorTest {

    @Test
    @DisplayName("値を指定してインスタンスが作成される")
    void shouldCreateInstanceWithValue() {
      ParameterSpec paramSpec = new ParameterSpec("--verbose");

      assertThat(paramSpec).isNotNull();
      assertThat(paramSpec.getValue()).isEqualTo("--verbose");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のインスタンスは等しいと判定される")
    void shouldBeEqualWhenValuesMatch() {
      ParameterSpec spec1 = ParameterSpec.of("--input");
      ParameterSpec spec2 = ParameterSpec.of("--input");

      assertThat(spec1).isEqualTo(spec2);
      assertThat(spec1.hashCode()).isEqualTo(spec2.hashCode());
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないと判定される")
    void shouldBeNotEqualWhenValuesDiffer() {
      ParameterSpec spec1 = ParameterSpec.of("--input");
      ParameterSpec spec2 = ParameterSpec.of("--output");

      assertThat(spec1).isNotEqualTo(spec2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値が文字列として返される")
    void shouldReturnValueAsString() {
      ParameterSpec paramSpec = ParameterSpec.of("--input");

      assertThat(paramSpec.toString()).isEqualTo("--input");
    }

    @Test
    @DisplayName("null値の場合はnullが返される")
    void shouldReturnNullWhenValueIsNull() {
      ParameterSpec paramSpec = new ParameterSpec();

      assertThat(paramSpec.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("MAX_LENGTH定数のテスト")
  class MaxLengthConstantTest {

    @Test
    @DisplayName("MAX_LENGTHは1000である")
    void shouldHaveMaxLength1000() {
      assertThat(ParameterSpec.MAX_LENGTH).isEqualTo(1000);
    }
  }
}
