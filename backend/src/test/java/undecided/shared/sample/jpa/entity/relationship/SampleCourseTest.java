package undecided.shared.sample.jpa.entity.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleCourseの単体テスト")
class SampleCourseTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で課程エンティティが作成される")
    void shouldCreateCourseWithValidArguments() {
      // Arrange
      SnowflakeId courseId = SnowflakeId.of(1L);
      String courseName = "数学基礎";

      // Act
      SampleCourse course = new SampleCourse(courseId, courseName);

      // Assert
      assertThat(course).isNotNull();
      assertThat(course.getCourseId()).isEqualTo(courseId);
      assertThat(course.getCourseName()).isEqualTo(courseName);
    }

    @Test
    @DisplayName("studentsリストは初期状態で空のArrayListである")
    void shouldHaveEmptyStudentsListInitially() {
      // Arrange & Act
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "数学基礎");

      // Assert
      assertThat(course.getStudents()).isNotNull().isEmpty();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("課程名が設定・取得できる")
    void shouldSetAndGetCourseName() {
      // Arrange
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "初期名称");

      // Act
      course.setCourseName("変更後の名称");

      // Assert
      assertThat(course.getCourseName()).isEqualTo("変更後の名称");
    }

    @Test
    @DisplayName("studentsリストが設定・取得できる")
    void shouldSetAndGetStudents() {
      // Arrange
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "数学基礎");
      List<SampleStudent> students = List.of();

      // Act
      course.setStudents(students);

      // Assert
      assertThat(course.getStudents()).isSameAs(students);
    }
  }

  @Nested
  @DisplayName("OneToMany関連付けのテスト")
  class OneToManyRelationshipTest {

    @Test
    @DisplayName("studentsに学生を追加できる")
    void shouldAddStudentToCourse() {
      // Arrange
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "数学基礎");
      SampleStudent student = new SampleStudent(SnowflakeId.of(2L), "田中太郎", course);

      // Act
      course.getStudents().add(student);

      // Assert
      assertThat(course.getStudents()).hasSize(1);
      assertThat(course.getStudents().get(0)).isEqualTo(student);
    }

    @Test
    @DisplayName("複数の学生を追加できる")
    void shouldAddMultipleStudentsToCourse() {
      // Arrange
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "数学基礎");
      SampleStudent student1 = new SampleStudent(SnowflakeId.of(2L), "田中太郎", course);
      SampleStudent student2 = new SampleStudent(SnowflakeId.of(3L), "鈴木花子", course);

      // Act
      course.getStudents().add(student1);
      course.getStudents().add(student2);

      // Assert
      assertThat(course.getStudents()).hasSize(2);
    }
  }
}
