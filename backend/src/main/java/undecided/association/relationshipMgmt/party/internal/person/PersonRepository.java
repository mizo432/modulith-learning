package undecided.association.relationshipMgmt.party.internal.person;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Person エンティティのリポジトリインターフェースです。
 *
 * <p>{@link Person} の永続化操作を提供します。
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, SnowflakeId> {

  Optional<Person> findBySearchName(SearchName searchName);
}
