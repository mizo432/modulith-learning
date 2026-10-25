package undecided.shared.sample.jpa.entity.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 受講生（Student）を表すサンプルエンティティです。
 *
 * <p>{@code @ManyToOne}関連付けのサンプルとして、 一つの課程（SampleCourse）に所属する受講生を表します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code @JoinColumn}で外部キーカラムを明示的に指定
 *   <li>{@code nullable = false}で必須関係を制約
 * </ul>
 */
@Entity
@Table(name = "sample_students")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleStudent {

  @Id private SnowflakeId studentId;

  /** 学生名 */
  @Column(nullable = false, length = 100)
  private String studentName;

  /** この学生が受講する課程です。 {@code @ManyToOne}により、複数の学生が一つの課程に関連付けられます。 */
  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private SampleCourse course;

  public SampleStudent(SnowflakeId studentId, String studentName, SampleCourse course) {
    this.studentId = studentId;
    this.studentName = studentName;
    this.course = course;
  }
}
