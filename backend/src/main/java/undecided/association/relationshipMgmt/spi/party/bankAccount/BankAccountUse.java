package undecided.association.relationshipMgmt.spi.party.bankAccount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import com.github.mizo432.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 銀行口座の用途情報を表すエンティティクラス。
 *
 * <p>このクラスはパーティ（PersonやOrganizationなど）が銀行口座をどのように使用しているかを管理します。
 *
 * <p>口座とパーティの関連付け、用途種別、および有効期間の情報を格納します。
 *
 * <p>用途:
 *
 * <p>給与振込先、請求書払い込み元など、銀行口座の使用目的を識別するために使用されます。
 */
@Table(schema = SchemaRelationshipMgmt.NAME, name = "bank_account_use")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccountUse {
  /**
   * 銀行口座用途を一意に識別するためのID。
   *
   * <p>このフィールドは銀行口座の用途記録を一意に識別します。
   *
   * <p>用途: 口座用途情報の検索や更新で使用されます。
   */
  @Id protected SnowflakeId bankAccountUseId;

  /**
   * 関連する銀行口座を識別するためのID。
   *
   * <p>このフィールドは用途が適用される銀行口座を参照します。
   *
   * <p>用途: 口座情報と用途情報の関連付けで使用されます。
   */
  protected SnowflakeId bankAccountId;

  /**
   * 関連するパーティを識別するためのID。
   *
   * <p>このフィールドは口座を使用するパーティ（PersonやOrganizationなど）を参照します。
   *
   * <p>用途: パーティに紐づく口座用途の検索で使用されます。
   */
  @Column(name = "party_id")
  protected SnowflakeId partyId;

  /**
   * 口座の用途種別（給与振込、請求書払い込みなど）。
   *
   * <p>このフィールドは銀行口座がどのような目的で使用されているかを示します。
   *
   * <p>用途: 取引処理において用途に応じた処理を行うために使用されます。
   */
  protected BankAccountUseType type;

  /**
   * 口座用途が設定された日時。
   *
   * <p>このフィールドは口座が特定の用途で使用され始めた時期を記録します。
   *
   * <p>用途: 有効期間の管理や履歴の追跡で使用されます。
   */
  protected WhenOccured whenOccured;

  /**
   * 銀行口座の用途情報を新規作成します。
   *
   * @param bankAccountUseId 銀行口座用途ID
   * @param bankAccountId 銀行口座ID
   * @param partyId パーティID
   * @param type 用途種別
   * @param whenOccured 発生時刻
   * @return 新規作成されたBankAccountUseインスタンス
   */
  public static BankAccountUse create(
      SnowflakeId bankAccountUseId,
      SnowflakeId bankAccountId,
      SnowflakeId partyId,
      BankAccountUseType type,
      WhenOccured whenOccured
  ) {
    BankAccountUse bankAccountUse = new BankAccountUse();
    bankAccountUse.bankAccountUseId = bankAccountUseId;
    bankAccountUse.bankAccountId = bankAccountId;
    bankAccountUse.partyId = partyId;
    bankAccountUse.type = type;
    bankAccountUse.whenOccured = whenOccured;
    return bankAccountUse;
  }
}
