package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;
import undecided.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Component
@RequiredArgsConstructor
public class UseBankAccountFactoryImpl implements UseBankAccountFactory {

  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @Override
  public @NonNull BankAccountUse createUseBankAccount(
      @NonNull BankAccountUseDto bankAccountUseDto) {
    return BankAccountUse.create(
        snowflakeIdGenerator.nextIdValue(),
        bankAccountUseDto.getBankAccountId(),
        bankAccountUseDto.getPartyId(),
        bankAccountUseDto.getType(),
        WhenOccured.current());
  }
}
