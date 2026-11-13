package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * シンプルな従業員エンティティ。
 *
 * <p>従業員番号、検索用名称、氏名などを管理します。
 */
@Entity
@Table(name = "employees", schema = "relationship_mgmt")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleEmployee {
  @Id protected SnowflakeId employeeId;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "employee_no", nullable = false, length = 10)))
  protected EmployeeNo employeeNo;

  /** 検索用名称 */
  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "search_name", nullable = false, length = 50)))
  protected SearchName searchName;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "full_name", nullable = false, length = 255)))
  protected FullName fullName;

  /**
   * 従業員を新規作成します。
   *
   * @param employeeId 従業員ID
   * @param employeeNo 従業員番号
   * @param searchName 検索用名称
   * @param fullName 氏名
   * @return 新規作成されたSimpleEmployeeインスタンス
   */
  public static SimpleEmployee create(
      SnowflakeId employeeId,
      EmployeeNo employeeNo,
      SearchName searchName,
      FullName fullName
  ) {
    SimpleEmployee employee = new SimpleEmployee();
    employee.employeeId = employeeId;
    employee.employeeNo = employeeNo;
    employee.searchName = searchName;
    employee.fullName = fullName;
    return employee;
  }
}
