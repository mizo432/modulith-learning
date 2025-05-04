package undecided.erp.projectActivity.domain.model.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProjectAttributeTest {

  @DisplayName("ofメソッドのテスト")
  @Nested
  class OfMethod {

    @DisplayName("引数がnullの場合")
    @Test
    void shouldThrowExceptionWhenArgumentsAreNull() {
      assertThatThrownBy(() -> ProjectAttribute.of(null, null))
          .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("引数が空の場合")
    @Test
    void shouldReturnEmptyObjectWhenArgumentsAreEmpty() {
      ProjectName projectName = ProjectName.EMPTY;
      ProjectCode projectCode = ProjectCode.EMPTY;
      ProjectAttribute result = ProjectAttribute.of(projectName, projectCode);

      assertThat(result).isEqualTo(ProjectAttribute.EMPTY);
    }

    @DisplayName("引数が正しい場合")
    @Test
    void shouldReturnProjectAttributeWhenArgumentsAreValid() {
      ProjectName projectName = ProjectName.of("Test Project");
      ProjectCode projectCode = ProjectCode.of("Cod");
      ProjectAttribute result = ProjectAttribute.of(projectName, projectCode);
      System.out.println(result);
      assertThat(result.getName()).isEqualTo(projectName);
      assertThat(result.getCode()).isEqualTo(projectCode);
      assertThat(result).isNotEqualTo(ProjectAttribute.EMPTY);
    }
  }
}
