package undecided.association.relationshipMgmt.spi.party.bankAccount.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountNumber;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUseType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.HoldersName;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 銀行口座と用途を同時に作成するためのDTOクラスです。
 *
 * <p>銀行口座情報とパーティの関連付けを一度に登録するために使用されます。
 */
@Getter
@RequiredArgsConstructor
public class CreateBankAccountUseDto {

  /**
   * 銀行を識別するためのID。
   */
  private final SnowflakeId bankId;

  /**
   * 銀行支店を識別するためのID。
   */
  private final SnowflakeId bankBranchId;

  /**
   * 口座種別（普通預金、当座預金など）。
   */
  private final AccountType accountType;

  /**
   * 口座番号。
   */
  private final AccountNumber accountNumber;

  /**
   * 口座名義人の名前。
   */
  private final HoldersName holdersName;

  /**
   * 関連するパーティを識別するためのID。
   */
  private final SnowflakeId partyId;

  /**
   * 口座の用途種別（給与振込、請求書払い込みなど）。
   */
  private final BankAccountUseType useType;
}
