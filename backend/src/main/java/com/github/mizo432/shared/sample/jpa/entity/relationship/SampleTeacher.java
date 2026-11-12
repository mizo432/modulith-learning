package com.github.mizo432.shared.sample.jpa.entity.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 教師（Teacher）を表すサンプルエンティティです。
 *
 * <p>{@code @ManyToMany}関連付けのサンプルとして、 複数の教師が複数の科目（SampleSubject）を担当する関係を示します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code @JoinTable}で中間テーブルを明示的に定義
 *   <li>{@code @JoinColumn}で双方の外部キーカラムを指定
 *   <li>デフォルトで{@code ArrayList}を初期化して、null参照を防ぐ
 * </ul>
 */
@Entity
@Table(name = "sample_teachers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleTeacher {

  @Id private SnowflakeId teacherId;

  /** 教師名 */
  @Column(nullable = false, length = 100)
  private String teacherName;

  /**
   * この教師が担当する科目のリストです。 {@code @ManyToMany}により、複数の教師が複数の科目に関連付けられます。 中間テーブルは{@code
   * sample_teacher_subject}として定義されます。
   */
  @ManyToMany
  @JoinTable(
      name = "sample_teacher_subject",
      joinColumns = @JoinColumn(name = "teacher_id"),
      inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private List<SampleSubject> subjects = new ArrayList<>();

  public SampleTeacher(SnowflakeId teacherId, String teacherName) {
    this.teacherId = teacherId;
    this.teacherName = teacherName;
  }
}
