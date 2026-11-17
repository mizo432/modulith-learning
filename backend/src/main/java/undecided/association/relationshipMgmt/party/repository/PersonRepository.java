package undecided.association.relationshipMgmt.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 個人（Person）のJPAリポジトリインターフェースです。
 *
 * <p>このリポジトリは、個人エンティティの永続化操作を提供します。
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, SnowflakeId> {}
