package com.github.mizo432.shared.sample.jpa.entity.relationship;

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
 * 従業員（Employee）を表すサンプルエンティティです。
 *
 * <p>{@code @OneToOne}関連付けのサンプルで使用されます。 一人の従業員が一つの従業員プロフィール（SampleEmployeeProfile）を持つ関係を示します。
 */
@Entity
@Table(name = "sample_employees")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEmployee {

  @Id private SnowflakeId employeeId;

  /** 従業員名 */
  @Column(nullable = false, length = 100)
  private String employeeName;

  /** 従業員番号 */
  @Column(nullable = false, length = 20, unique = true)
  private String employeeNo;

  public SampleEmployee(SnowflakeId employeeId, String employeeName, String employeeNo) {
    this.employeeId = employeeId;
    this.employeeName = employeeName;
    this.employeeNo = employeeNo;
  }
}
