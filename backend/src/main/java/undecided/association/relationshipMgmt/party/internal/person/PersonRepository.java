package undecided.association.relationshipMgmt.party.internal.person;

import org.springframework.data.jpa.repository.JpaRepository;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Person エンティティのリポジトリインターフェースです。
 *
 * <p>{@link Person} の永続化操作を提供します。
 */
public interface PersonRepository extends JpaRepository<Person, SnowflakeId> {}
