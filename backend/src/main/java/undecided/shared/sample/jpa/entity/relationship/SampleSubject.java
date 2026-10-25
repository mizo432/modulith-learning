package undecided.shared.sample.jpa.entity.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 科目（Subject）を表すサンプルエンティティです。
 *
 * <p>{@code @ManyToMany}関連付けのサンプルで使用されます。 複数の教師（SampleTeacher）が複数の科目を担当する関係を示します。
 */
@Entity
@Table(name = "sample_subjects")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSubject {

  @Id private SnowflakeId subjectId;

  /** 科目名 */
  @Column(nullable = false, length = 100)
  private String subjectName;

  /** 科目コード */
  @Column(nullable = false, length = 20, unique = true)
  private String subjectCode;

  public SampleSubject(SnowflakeId subjectId, String subjectName, String subjectCode) {
    this.subjectId = subjectId;
    this.subjectName = subjectName;
    this.subjectCode = subjectCode;
  }
}
