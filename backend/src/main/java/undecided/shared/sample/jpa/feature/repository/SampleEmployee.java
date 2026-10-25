package undecided.shared.sample.jpa.feature.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * リポジトリバリエーションのサンプルで使用される従業員エンティティです。
 *
 * <p>{@code JpaRepository} を使用するバッチ処理やページネーションの例として使用します。
 */
@Entity
@Table(name = "sample_employees")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEmployee {

  @Id private SnowflakeId employeeId;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "department", nullable = false, length = 50)
  private String department;

  @Column(name = "hire_date", nullable = false)
  private LocalDate hireDate;

  @Column(name = "salary")
  private Long salary;

  public SampleEmployee(
      SnowflakeId employeeId, String name, String department, LocalDate hireDate) {
    this.employeeId = employeeId;
    this.name = name;
    this.department = department;
    this.hireDate = hireDate;
  }

  public SampleEmployee(
      SnowflakeId employeeId, String name, String department, LocalDate hireDate, Long salary) {
    this.employeeId = employeeId;
    this.name = name;
    this.department = department;
    this.hireDate = hireDate;
    this.salary = salary;
  }
}
