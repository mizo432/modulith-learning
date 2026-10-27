package undecided.shared.sample.jpa.entity.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 課程（Course）を表すサンプルエンティティです。
 *
 * <p>{@code @OneToMany}関連付けのサンプルとして、 複数の受講生（SampleStudent）を持つ課程を表します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code mappedBy}で所有側を指定し、逆方向の関連付けを定義
 *   <li>デフォルトで{@code ArrayList}を初期化して、null参照を防ぐ
 * </ul>
 */
@Entity
@Table(name = "sample_courses")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleCourse {

  @Id private SnowflakeId courseId;

  /** 課程名 */
  @Column(nullable = false, length = 100)
  private String courseName;

  /** この課程を受講する学生のリストです。 {@code mappedBy}により、SampleStudent側のcourseフィールドが所有側となります。 */
  @OneToMany(mappedBy = "course")
  private List<SampleStudent> students = new ArrayList<>();

  public SampleCourse(SnowflakeId courseId, String courseName) {
    this.courseId = courseId;
    this.courseName = courseName;
  }
}
