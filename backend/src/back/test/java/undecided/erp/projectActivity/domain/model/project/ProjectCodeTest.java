package undecided.erp.projectActivity.domain.model.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProjectCodeTest {

  @DisplayName("ProjectCodeのofメソッド")
  @Nested
  class OfMethodTest {

    @DisplayName("値が3文字の半角文字列の場合、正しくインスタンスを生成")
    @Test
    void shouldReturnProjectCodeWhenValueIsThreeHalfWidthCharacters() {
      String testCode = "123";
      ProjectCode projectCode = ProjectCode.of(testCode);

      assertThat(projectCode.getValue()).isEqualTo(testCode);
    }

    @DisplayName("値が空文字列の場合、IllegalArgumentExceptionを投げる")
    @Test
    void shouldThrowIllegalArgumentExceptionWhenValueIsEmpty() {
      assertThatThrownBy(() -> ProjectCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("ProjectCode cannot be empty. Please provide a valid code.");
    }

    @DisplayName("値が3文字の全角文字列の場合、IllegalArgumentExceptionを投げる")
    @Test
    void shouldThrowIllegalArgumentExceptionWhenValueIsThreeFullWidthCharacters() {
      assertThatThrownBy(() -> ProjectCode.of("１２３"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ProjectCode must be exactly");
    }

    @DisplayName("nullを渡したときはIllegalArgumentExceptionを投げる")
    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
      assertThatThrownBy(() -> ProjectCode.of(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @DisplayName("ProjectCodeのtoStringメソッド")
  @Nested
  class ToStringMethodTest {

    @DisplayName("値が3文字の半角文字列の場合、その文字列を返す")
    @Test
    void shouldReturnStringValueWhenValueIsThreeHalfWidthCharacters() {
      String testCode = "123";
      ProjectCode projectCode = ProjectCode.of(testCode);

      assertThat(projectCode.toString()).isEqualTo(testCode);
    }

    @DisplayName("値がnullの場合、'null'を返す")
    @Test
    void shouldReturnNullStringWhenValueIsNull() {
      ProjectCode projectCode = new ProjectCode();

      assertThat(projectCode.toString()).isEqualTo("null");
    }
  }

  @DisplayName("ProjectCodeのisEmptyメソッド")
  @Nested
  class IsEmptyMethodTest {

    @DisplayName("値がnullの場合、trueを返す")
    @Test
    void shouldReturnTrueWhenValueIsNull() {
      ProjectCode projectCode = new ProjectCode();

      assertThat(projectCode.isEmpty()).isTrue();
    }

    @DisplayName("値が空でない場合、falseを返す")
    @Test
    void shouldReturnFalseWhenValueIsNotEmpty() {
      String testCode = "123";
      ProjectCode projectCode = ProjectCode.of(testCode);

      assertThat(projectCode.isEmpty()).isFalse();
    }
  }
}
