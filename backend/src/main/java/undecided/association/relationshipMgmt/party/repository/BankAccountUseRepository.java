package undecided.association.relationshipMgmt.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface BankAccountUseRepository extends JpaRepository<BankAccountUse, SnowflakeId> {}
