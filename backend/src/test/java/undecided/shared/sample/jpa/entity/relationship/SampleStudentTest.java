package undecided.shared.sample.jpa.entity.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleStudentの単体テスト")
class SampleStudentTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で学生エンティティが作成される")
    void shouldCreateStudentWithValidArguments() {
      // Arrange
      SnowflakeId studentId = SnowflakeId.of(1L);
      String studentName = "田中太郎";
      SampleCourse course = new SampleCourse(SnowflakeId.of(2L), "数学基礎");

      // Act
      SampleStudent student = new SampleStudent(studentId, studentName, course);

      // Assert
      assertThat(student).isNotNull();
      assertThat(student.getStudentId()).isEqualTo(studentId);
      assertThat(student.getStudentName()).isEqualTo(studentName);
      assertThat(student.getCourse()).isEqualTo(course);
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("学生名が設定・取得できる")
    void shouldSetAndGetStudentName() {
      // Arrange
      SampleStudent student =
          new SampleStudent(SnowflakeId.of(1L), "初期名", new SampleCourse(SnowflakeId.of(2L), "数学"));

      // Act
      student.setStudentName("変更後の名前");

      // Assert
      assertThat(student.getStudentName()).isEqualTo("変更後の名前");
    }

    @Test
    @DisplayName("courseが設定・取得できる")
    void shouldSetAndGetCourse() {
      // Arrange
      SampleStudent student =
          new SampleStudent(SnowflakeId.of(1L), "田中", new SampleCourse(SnowflakeId.of(2L), "数学"));

      // Act
      SampleCourse newCourse = new SampleCourse(SnowflakeId.of(3L), "英語");
      student.setCourse(newCourse);

      // Assert
      assertThat(student.getCourse()).isEqualTo(newCourse);
    }
  }

  @Nested
  @DisplayName("ManyToOne関連付けのテスト")
  class ManyToOneRelationshipTest {

    @Test
    @DisplayName("複数の学生が同じ課程に関連付けられる")
    void shouldMultipleStudentsShareSameCourse() {
      // Arrange
      SampleCourse course = new SampleCourse(SnowflakeId.of(1L), "数学基礎");

      // Act
      SampleStudent student1 = new SampleStudent(SnowflakeId.of(2L), "田中太郎", course);
      SampleStudent student2 = new SampleStudent(SnowflakeId.of(3L), "鈴木花子", course);

      // Assert
      assertThat(student1.getCourse()).isEqualTo(course);
      assertThat(student2.getCourse()).isEqualTo(course);
      assertThat(student1.getCourse()).isSameAs(student2.getCourse());
    }
  }
}
