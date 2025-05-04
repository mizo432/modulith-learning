package undecided.erp.projectActivity.domain.model.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ドメインモデルProjectについて")
class ProjectTest {

  @Nested
  @DisplayName("equalsメソッドのテスト")
  class EqualsTest {

    @Test
    @DisplayName("同じIDを持つProjectオブジェクトに対しては、同等と判断する")
    void shouldReturnTrueWhenIdIsSame() {
      ProjectAttribute attribute = ProjectAttribute.of(ProjectName.of("A"), ProjectCode.of("ABC"));
      Project project1 = Project.create(attribute);
      Project project2 = new Project(project1.getId(), attribute);

      assertThat(project1).isEqualTo(project2);
    }

    @Test
    @DisplayName("異なるIDを持つProjectオブジェクトに対しては、同等と判断しない")
    void shouldReturnFalseWhenIdIsDifferent() {
      ProjectAttribute attribute = ProjectAttribute.of(ProjectName.of("A"), ProjectCode.of("ABC"));
      Project project1 = Project.create(attribute);
      Project project2 = Project.create(attribute);

      assertThat(project1.equals(project2)).isFalse();
    }

    @Test
    @DisplayName("nullを引数として渡した場合、同等と判断しない")
    void shouldReturnFalseWhenNullIsGiven() {
      ProjectAttribute attribute = ProjectAttribute.of(ProjectName.of("A"), ProjectCode.of("ABC"));
      Project project = Project.create(attribute);

      assertThat(project.equals(null)).isFalse();
    }
  }

  @Nested
  @DisplayName("hashCodeメソッドのテスト")
  class HashCodeTest {

    @Test
    @DisplayName("同じIDを持つProjectオブジェクトに対しては、同じハッシュコードを返す")
    void shouldReturnSameHashCodeWhenIdIsSame() {
      ProjectAttribute attribute = ProjectAttribute.of(ProjectName.of("A"), ProjectCode.of("ABC"));
      Project project1 = Project.create(attribute);
      Project project2 = new Project(project1.getId(), attribute);

      assertThat(project1.hashCode()).isEqualTo(project2.hashCode());
    }

    @Test
    @DisplayName("異なるIDを持つProjectオブジェクトに対しては、異なるハッシュコードを返す")
    void shouldReturnDifferentHashCodeWhenIdIsDifferent() {
      ProjectAttribute attribute = ProjectAttribute.of(ProjectName.of("A"), ProjectCode.of("ABC"));
      Project project1 = Project.create(attribute);
      Project project2 = Project.create(attribute);

      assertThat(project1.hashCode()).isNotEqualTo(project2.hashCode());
    }
  }
}
