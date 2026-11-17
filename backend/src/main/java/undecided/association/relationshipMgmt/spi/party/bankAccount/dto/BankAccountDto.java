package undecided.association.relationshipMgmt.spi.party.bankAccount.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountNumber;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.HoldersName;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 銀行口座情報を表すエンティティクラス。
 *
 * <p>このクラスはパーティ（PersonやOrganizationなど）が持つ銀行口座の情報を管理します。
 *
 * <p>銀行、支店、口座種別、口座番号の情報を格納し、振込・振替などの取引において使用されます。
 *
 * <p>用途:
 *
 * <p>給与振込、請求書払い込みなどの金融取引において、対象となる銀行口座を識別するために使用されます。
 */
@Getter
@AllArgsConstructor
public class BankAccountDto {
  /**
   * 銀行口座を一意に識別するためのID。
   *
   * <p>このフィールドは銀行口座を一意に識別します。
   *
   * <p>用途: 口座情報の検索や更新で使用されます。
   */
  @Id private SnowflakeId bankAccountId = SnowflakeId.EMPTY;

  /**
   * 銀行を識別するためのID。
   *
   * <p>このフィールドは口座が属する銀行を一意に識別します。
   *
   * <p>用途: 振込先銀行の指定や口座情報の検索で使用されます。
   */
  private SnowflakeId bankId = SnowflakeId.EMPTY;

  /**
   * 銀行支店を識別するためのID。
   *
   * <p>このフィールドは口座が属する銀行支店を一意に識別します。
   *
   * <p>用途: 振込先支店の指定や口座情報の検索で使用されます。
   */
  private SnowflakeId bankBranchId = SnowflakeId.EMPTY;

  /**
   * 口座種別（普通預金、当座預金など）。
   *
   * <p>このフィールドは銀行口座の種別を示します。
   *
   * <p>用途: 取引処理において口座種別に応じた処理を行うために使用されます。
   */
  private AccountType type = AccountType.UNKNOWN;

  /**
   * 口座番号。
   *
   * <p>このフィールドは銀行口座を一意に識別する番号を格納します。
   *
   * <p>用途: 振込・振替処理において対象口座を特定するために使用されます。
   */
  private AccountNumber accountNumber;

  /**
   * 口座名義人の名前。
   *
   * <p>このフィールドは銀行口座の所有者の名前を格納します。
   *
   * <p>用途: 振込・振替処理において口座名義人を特定するために使用されます。
   */
  private HoldersName holdersName;

  protected BankAccountDto() {
    bankAccountId = SnowflakeId.EMPTY;
    bankId = SnowflakeId.EMPTY;
    bankBranchId = SnowflakeId.EMPTY;
    type = AccountType.UNKNOWN;
    accountNumber = AccountNumber.empty();
    holdersName = HoldersName.empty();
  }
}
