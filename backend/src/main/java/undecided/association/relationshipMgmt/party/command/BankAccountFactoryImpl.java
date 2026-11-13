package undecided.association.relationshipMgmt.party.command;

import org.springframework.stereotype.Component;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 銀行口座エンティティを作成するためのファクトリ実装です。
 *
 * <p>SnowflakeIdGeneratorを使用して銀行口座に一意のIDを付与します。
 */
@Component
public class BankAccountFactoryImpl implements BankAccountFactory {
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  public BankAccountFactoryImpl(SnowflakeIdGenerator snowflakeIdGenerator) {
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  @Override
  public BankAccount create(BankAccount bankAccount) {
    var bankAccountId = snowflakeIdGenerator.nextIdValue();
    var result =
        new BankAccount(
            bankAccountId,
            bankAccount.getBankId(),
            bankAccount.getBankBranchId(),
            bankAccount.getType(),
            bankAccount.getAccountNumber(),
            bankAccount.getHoldersName());
    return result;
  }
}
