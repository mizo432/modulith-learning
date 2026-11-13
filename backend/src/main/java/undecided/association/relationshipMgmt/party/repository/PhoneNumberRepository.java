package undecided.association.relationshipMgmt.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberEntity;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity, SnowflakeId> {}
