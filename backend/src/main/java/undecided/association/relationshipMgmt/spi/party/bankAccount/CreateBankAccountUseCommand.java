package undecided.association.relationshipMgmt.spi.party.bankAccount;

import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.CreateBankAccountUseDto;

/**
 * 銀行口座と用途を同時に作成するためのコマンドインターフェースです。
 *
 * <p>新しい銀行口座とそのパーティとの関連付けを一度に登録するために使用されます。
 */
public interface CreateBankAccountUseCommand {

  /**
   * 銀行口座とその用途情報を新規登録します。
   *
   * @param dto 銀行口座情報と用途情報のDTO。bankId、bankBranchId、accountType、accountNumber、holdersName、partyId、useTypeを含みます。
   */
  void createAndUse(CreateBankAccountUseDto dto);
}
