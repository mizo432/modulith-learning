package undecided.erp.projectActivity.domain.model.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProjectNameクラスの")
class ProjectNameTest {

  @DisplayName("ofメソッドのテスト")
  @Nested
  class OfMethodTest {

    @DisplayName("引数がnullの場合、IllegalArgumentExceptionを投げるべき")
    @Test
    void shouldThrowIllegalArgumentExceptionIfValueIsNull() {
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> ProjectName.of(null));
    }

    @DisplayName("引数が空の場合、IllegalArgumentExceptionを投げるべき")
    @Test
    void shouldThrowIllegalArgumentExceptionIfValueIsEmpty() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> ProjectName.of(""));
    }

    @DisplayName("正常な引数が提供された場合、ProjectNameインスタンスを返すべき")
    @Test
    void shouldReturnProjectNameIfValueIsProvided() {
      String validValue = "valid";
      ProjectName projectName = ProjectName.of(validValue);
      assertThat(projectName.getValue()).isEqualTo(validValue);
    }

  }

  @DisplayName("toStringメソッドのテスト")
  @Nested
  class ToStringMethodTest {

    @DisplayName("引数がnullの場合、nullを返すべき")
    @Test
    void shouldReturnNullIfValueIsNull() {
      ProjectName projectName = new ProjectName();
      assertThat(projectName.toString()).isEqualTo("null");
    }

    @DisplayName("正常な引数が提供された場合、該当するStringを返すべき")
    @Test
    void shouldReturnValueIfValueIsProvided() {
      String validValue = "valid";
      ProjectName projectName = ProjectName.of(validValue);
      assertThat(projectName.toString()).isEqualTo(validValue);
    }

  }

  @DisplayName("isEmptyメソッドのテスト")
  @Nested
  class IsEmptyMethodTest {

    @DisplayName("valueがnullの場合、trueを返すべき")
    @Test
    void shouldReturnTrueIfValueIsNull() {
      ProjectName projectName = new ProjectName();
      assertThat(projectName.isEmpty()).isEqualTo(true);
    }

    @DisplayName("valueが非null値を持つ場合、falseを返すべき")
    @Test
    void shouldReturnFalseIfValueIsNonNull() {
      String nonNullValue = "valid";
      ProjectName projectName = ProjectName.of(nonNullValue);
      assertThat(projectName.isEmpty()).isEqualTo(false);
    }
  }
}
