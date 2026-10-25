package undecided.shared.sample.jpa.entity.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleTeacherの単体テスト")
class SampleTeacherTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で教師エンティティが作成される")
    void shouldCreateTeacherWithValidArguments() {
      // Arrange
      SnowflakeId teacherId = SnowflakeId.of(1L);
      String teacherName = "山田先生";

      // Act
      SampleTeacher teacher = new SampleTeacher(teacherId, teacherName);

      // Assert
      assertThat(teacher).isNotNull();
      assertThat(teacher.getTeacherId()).isEqualTo(teacherId);
      assertThat(teacher.getTeacherName()).isEqualTo(teacherName);
    }

    @Test
    @DisplayName("subjectsリストは初期状態で空のArrayListである")
    void shouldHaveEmptySubjectsListInitially() {
      // Arrange & Act
      SampleTeacher teacher = new SampleTeacher(SnowflakeId.of(1L), "山田先生");

      // Assert
      assertThat(teacher.getSubjects()).isNotNull().isEmpty();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("教師名が設定・取得できる")
    void shouldSetAndGetTeacherName() {
      // Arrange
      SampleTeacher teacher = new SampleTeacher(SnowflakeId.of(1L), "初期名");

      // Act
      teacher.setTeacherName("変更後の名前");

      // Assert
      assertThat(teacher.getTeacherName()).isEqualTo("変更後の名前");
    }
  }

  @Nested
  @DisplayName("ManyToMany関連付けのテスト")
  class ManyToManyRelationshipTest {

    @Test
    @DisplayName("教師に科目を追加できる")
    void shouldAddSubjectToTeacher() {
      // Arrange
      SampleTeacher teacher = new SampleTeacher(SnowflakeId.of(1L), "山田先生");
      SampleSubject subject = new SampleSubject(SnowflakeId.of(2L), "数学", "MATH001");

      // Act
      teacher.getSubjects().add(subject);

      // Assert
      assertThat(teacher.getSubjects()).hasSize(1);
      assertThat(teacher.getSubjects().get(0)).isEqualTo(subject);
    }

    @Test
    @DisplayName("教師に複数の科目を追加できる")
    void shouldAddMultipleSubjectsToTeacher() {
      // Arrange
      SampleTeacher teacher = new SampleTeacher(SnowflakeId.of(1L), "山田先生");
      SampleSubject subject1 = new SampleSubject(SnowflakeId.of(2L), "数学", "MATH001");
      SampleSubject subject2 = new SampleSubject(SnowflakeId.of(3L), "英語", "ENG001");

      // Act
      teacher.getSubjects().add(subject1);
      teacher.getSubjects().add(subject2);

      // Assert
      assertThat(teacher.getSubjects()).hasSize(2);
    }

    @Test
    @DisplayName("複数の教師が同じ科目を担当できる")
    void shouldMultipleTeachersShareSubject() {
      // Arrange
      SampleSubject subject = new SampleSubject(SnowflakeId.of(1L), "数学", "MATH001");
      SampleTeacher teacher1 = new SampleTeacher(SnowflakeId.of(2L), "山田先生");
      SampleTeacher teacher2 = new SampleTeacher(SnowflakeId.of(3L), "佐藤先生");

      // Act
      teacher1.getSubjects().add(subject);
      teacher2.getSubjects().add(subject);

      // Assert
      assertThat(teacher1.getSubjects()).contains(subject);
      assertThat(teacher2.getSubjects()).contains(subject);
    }
  }
}
