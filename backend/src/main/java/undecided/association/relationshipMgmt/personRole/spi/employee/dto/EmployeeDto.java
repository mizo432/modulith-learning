package undecided.association.relationshipMgmt.personRole.spi.employee.dto;

import org.jspecify.annotations.NonNull;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record EmployeeDto(
    SnowflakeId employeeId,
    @NonNull EmployeeNo employeeNo,
    @NonNull SearchName searchName,
    @NonNull FullName fullName) {

  /**
   * 従業員DTOを作成します。
   *
   * @param employeeId 従業員ID
   * @param employeeNo 従業員番号
   * @param searchName 検索名
   * @param fullName 氏名
   * @return 従業員DTO
   */
  public static EmployeeDto create(
      SnowflakeId employeeId,
      @NonNull EmployeeNo employeeNo,
      @NonNull SearchName searchName,
      @NonNull FullName fullName) {
    if (employeeNo == null) {
      throw new IllegalArgumentException("employeeNo must not be null");
    }
    if (searchName == null) {
      throw new IllegalArgumentException("searchName must not be null");
    }
    if (fullName == null) {
      throw new IllegalArgumentException("fullName must not be null");
    }
    return new EmployeeDto(employeeId, employeeNo, searchName, fullName);
  }
}
