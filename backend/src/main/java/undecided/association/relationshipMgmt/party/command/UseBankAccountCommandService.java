package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.BankAccountUseRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.UseBankAccountCommand;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;

/**
 * 銀行口座用途登録のコマンドサービスです。
 *
 * <p>BankAccountUseDtoからBankAccountUseエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class UseBankAccountCommandService implements UseBankAccountCommand {
  private final UseBankAccountFactory useBankAccountFactory;
  private final BankAccountUseRepository bankAccountUseRepository;

  /**
   * 銀行口座の用途情報を新規登録します。
   *
   * <p>受け取ったDTOからBankAccountUseエンティティを作成し、リポジトリに保存します。
   *
   * @param bankAccountUseDto 銀行口座用途情報のDTO。bankAccountId、partyId、typeを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void use(BankAccountUseDto bankAccountUseDto) {
    if (bankAccountUseDto == null) {
      throw new IllegalArgumentException("BankAccountUseDtoはnullであってはいけません");
    }
    if (bankAccountUseDto.getBankAccountId().isEmpty()) {
      throw new IllegalArgumentException("bankAccountIdは必須です");
    }
    if (bankAccountUseDto.getPartyId().isEmpty()) {
      throw new IllegalArgumentException("partyIdは必須です");
    }
    BankAccountUse bankAccountUse = useBankAccountFactory.createUseBankAccount(bankAccountUseDto);
    bankAccountUseRepository.save(bankAccountUse);
  }
}
