package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import com.github.mizo432.shared.converter.EncryptedStringConverter;

/**
 * 従業員エンティティ。
 *
 * <p>従業員の氏名、有効期間などを管理します。
 */
@Entity(name = "TestEmployee")
@Table(name = "employees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {
  @Id protected UUID employeeId;

  @Column(nullable = false)
  @Convert(converter = EncryptedStringConverter.class)
  protected String lastName;

  @Column(nullable = false)
  @Convert(converter = EncryptedStringConverter.class)
  protected String firstName;

  @Column(nullable = false)
  protected LocalDateTime validFrom;

  protected LocalDateTime validTo;

  /**
   * 従業員を新規作成します。
   *
   * @param employeeId 従業員ID
   * @param lastName 姓
   * @param firstName 名
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @return 新規作成されたEmployeeインスタンス
   */
  public static Employee create(
      UUID employeeId,
      String lastName,
      String firstName,
      LocalDateTime validFrom,
      LocalDateTime validTo
  ) {
    Employee employee = new Employee();
    employee.employeeId = employeeId;
    employee.lastName = lastName;
    employee.firstName = firstName;
    employee.validFrom = validFrom;
    employee.validTo = validTo;
    return employee;
  }
}
