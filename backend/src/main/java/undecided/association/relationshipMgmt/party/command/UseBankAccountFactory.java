package undecided.association.relationshipMgmt.party.command;

import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;

public interface UseBankAccountFactory {
  BankAccountUse createUseBankAccount(BankAccountUseDto bankAccountUseDto);
}
