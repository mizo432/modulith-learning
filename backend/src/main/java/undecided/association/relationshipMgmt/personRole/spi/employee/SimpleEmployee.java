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
 * <p>従業員番号、検索用名称、氏名などを管理します。{@link Employee}と比較してシンプルな構造を持っており、
 * 基本的な従業員情報のみを保持します。
 *
 * <p>C6修正により{@code @Setter}は削除され、{@code create()}ファクトリメソッドを使用してインスタンスを作成します。
 */
@Entity
@Table(name = "employees", schema = "relationship_mgmt")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleEmployee {

  /** 従業員の一意な識別子（Snowflake ID）。 */
  @Id
  protected SnowflakeId employeeId;

  /** 従業員番号。組織内での一意な識別子として使用されます。 */
  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "employee_no", nullable = false, length = 10)))
  protected EmployeeNo employeeNo;

  /** 検索用名称。従業員の検索や表示に使用されます。 */
  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "search_name", nullable = false, length = 50)))
  protected SearchName searchName;

  /** 従業員の氏名。公式な文書や識別に使用されます。 */
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
