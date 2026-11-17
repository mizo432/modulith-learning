package undecided.association.relationshipMgmt.spi.party.bankAccount;

import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountDto;

/**
 * 銀行口座登録のコマンドインターフェースです。
 *
 * <p>新しい銀行口座情報を登録するために使用されます。
 */
public interface CreateBankAccountCommand {

  /**
   * 銀行口座情報を新規登録します。
   *
   * @param bankAccountDto 銀行口座情報のDTO。bankId、bankBranchId、type、accountNumber、holdersNameを含みます。
   */
  void create(BankAccountDto bankAccountDto);
}
