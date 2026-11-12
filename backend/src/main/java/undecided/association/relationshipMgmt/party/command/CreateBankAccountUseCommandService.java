package undecided.association.relationshipMgmt.party.command;

import com.github.mizo432.shared.value.datetime.WhenOccured;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.BankAccountRepository;
import undecided.association.relationshipMgmt.party.repository.BankAccountUseRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.CreateBankAccountUseCommand;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.CreateBankAccountUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 銀行口座と用途を同時に作成するためのコマンドサービスです。
 *
 * <p>CreateBankAccountUseDtoからBankAccountとBankAccountUseエンティティを作成し、両方をデータベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreateBankAccountUseCommandService implements CreateBankAccountUseCommand {

  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final BankAccountRepository bankAccountRepository;
  private final BankAccountUseRepository bankAccountUseRepository;

  /**
   * 銀行口座とその用途情報を新規登録します。
   *
   * <p>受け取ったDTOからBankAccountとBankAccountUseエンティティを作成し、両方をリポジトリに保存します。
   *
   * @param dto 銀行口座情報と用途情報のDTO。bankId、bankBranchId、accountType、accountNumber、holdersName、partyId、useTypeを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void createAndUse(CreateBankAccountUseDto dto) {
    if (dto == null) {
      throw new IllegalArgumentException("CreateBankAccountUseDtoはnullであってはいけません");
    }
    if (dto.getBankId().isEmpty()) {
      throw new IllegalArgumentException("bankIdは必須です");
    }
    if (dto.getBankBranchId().isEmpty()) {
      throw new IllegalArgumentException("bankBranchIdは必須です");
    }
    if (dto.getAccountNumber() == null) {
      throw new IllegalArgumentException("accountNumberは必須です");
    }
    if (dto.getHoldersName() == null) {
      throw new IllegalArgumentException("holdersNameは必須です");
    }
    if (dto.getPartyId().isEmpty()) {
      throw new IllegalArgumentException("partyIdは必須です");
    }

    // 銀行口座を作成
    SnowflakeId bankAccountId = snowflakeIdGenerator.nextIdValue();
    BankAccount bankAccount =
        new BankAccount(
            bankAccountId,
            dto.getBankId(),
            dto.getBankBranchId(),
            dto.getAccountType(),
            dto.getAccountNumber(),
            dto.getHoldersName());
    bankAccountRepository.save(bankAccount);

    // 銀行口座用途を作成
    SnowflakeId bankAccountUseId = snowflakeIdGenerator.nextIdValue();
    BankAccountUse bankAccountUse =
        BankAccountUse.create(
            bankAccountUseId,
            bankAccountId,
            dto.getPartyId(),
            dto.getUseType(),
            WhenOccured.current());
    bankAccountUseRepository.save(bankAccountUse);
  }
}
