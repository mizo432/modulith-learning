package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.BankAccountRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;
import undecided.association.relationshipMgmt.spi.party.bankAccount.CreateBankAccountCommand;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountDto;

/**
 * 銀行口座登録のコマンドサービスです。
 *
 * <p>BankAccountDtoからBankAccountエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreateBankAccountCommandService implements CreateBankAccountCommand {
  private final BankAccountFactory bankAccountFactory;
  private final BankAccountRepository bankAccountRepository;

  /**
   * 銀行口座情報を新規登録します。
   *
   * <p>受け取ったDTOからBankAccountエンティティを作成し、ファクトリでIDを付与した後、リポジトリに保存します。
   *
   * @param bankAccountDto 銀行口座情報のDTO。bankId、bankBranchId、type、accountNumber、holdersNameを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void create(BankAccountDto bankAccountDto) {
    if (bankAccountDto == null) {
      throw new IllegalArgumentException("BankAccountDtoはnullであってはいけません");
    }
    if (bankAccountDto.getBankId().isEmpty()) {
      throw new IllegalArgumentException("bankIdは必須です");
    }
    if (bankAccountDto.getBankBranchId().isEmpty()) {
      throw new IllegalArgumentException("bankBranchIdは必須です");
    }
    if (bankAccountDto.getAccountNumber() == null || bankAccountDto.getAccountNumber().isEmpty()) {
      throw new IllegalArgumentException("accountNumberは必須です");
    }
    if (bankAccountDto.getHoldersName() == null || bankAccountDto.getHoldersName().isEmpty()) {
      throw new IllegalArgumentException("holdersNameは必須です");
    }
    BankAccount bankAccount =
        new BankAccount(
            undecided.supporting.snowflake.SnowflakeId.EMPTY,
            bankAccountDto.getBankId(),
            bankAccountDto.getBankBranchId(),
            bankAccountDto.getType(),
            bankAccountDto.getAccountNumber(),
            bankAccountDto.getHoldersName());
    BankAccount created = bankAccountFactory.create(bankAccount);
    bankAccountRepository.save(created);
  }
}
