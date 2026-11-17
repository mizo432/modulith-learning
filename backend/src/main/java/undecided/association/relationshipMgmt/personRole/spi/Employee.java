package undecided.association.relationshipMgmt.personRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員基本情報を表すエンティティクラス。
 *
 * <p>このクラスは、従業員の基本情報（氏名、連絡先、入社日など）を管理します。
 */
@Entity
@Table(name = "employee", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {
  @Id
  @Column(name = "employee_id")
  private SnowflakeId employeeId;

  /** パーティーID。 */
  @Column(name = "party_id", nullable = false, unique = true)
  private SnowflakeId partyId;

  /** 従業員コード（一意制約）。 */
  @Column(name = "employee_code", nullable = false, unique = true, length = 30)
  private String employeeCode;

  /** 名。 */
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  /** 姓。 */
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  /** 名(カナ)。 */
  @Column(name = "first_name_kana", length = 50)
  private String firstNameKana;

  /** 姓(カナ)。 */
  @Column(name = "last_name_kana", length = 50)
  private String lastNameKana;

  /** メールアドレス（一意制約）。 */
  @Column(name = "email", unique = true, length = 100)
  private String email;

  /** 電話番号。 */
  @Column(name = "phone", length = 20)
  private String phone;

  /** 入社日。 */
  @Column(name = "hire_date", nullable = false)
  private LocalDate hireDate;

  /** 退社日。 */
  @Column(name = "resignation_date")
  private LocalDate resignationDate;

  /** 状態（有効/無効）。 */
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 従業員を作成します。
   *
   * @param employeeId 従業員ID
   * @param partyId パーティーID
   * @param employeeCode 従業員コード
   * @param firstName 名
   * @param lastName 姓
   * @param firstNameKana 名(カナ)
   * @param lastNameKana 姓(カナ)
   * @param email メールアドレス
   * @param phone 電話番号
   * @param hireDate 入社日
   * @param status 状態
   * @param validFrom 有効開始日
   */
  public Employee(
      SnowflakeId employeeId,
      SnowflakeId partyId,
      String employeeCode,
      String firstName,
      String lastName,
      String firstNameKana,
      String lastNameKana,
      String email,
      String phone,
      LocalDate hireDate,
      String status,
      LocalDateTime validFrom) {
    this.employeeId = employeeId;
    this.partyId = partyId;
    this.employeeCode = employeeCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.firstNameKana = firstNameKana;
    this.lastNameKana = lastNameKana;
    this.email = email;
    this.phone = phone;
    this.hireDate = hireDate;
    this.status = status;
    this.validFrom = validFrom;
  }
}
