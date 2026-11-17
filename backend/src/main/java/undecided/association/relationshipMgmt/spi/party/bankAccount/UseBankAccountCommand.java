package undecided.association.relationshipMgmt.spi.party.bankAccount;

import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;

/**
 * 銀行口座用途登録のコマンドインターフェースです。
 *
 * <p>パーティが銀行口座を特定の用途で使用する関係を登録するために使用されます。
 */
public interface UseBankAccountCommand {

  /**
   * 銀行口座の用途情報を新規登録します。
   *
   * @param bankAccountUseDto 銀行口座用途情報のDTO。bankAccountId、partyId、typeを含みます。
   */
  void use(BankAccountUseDto bankAccountUseDto);
}
