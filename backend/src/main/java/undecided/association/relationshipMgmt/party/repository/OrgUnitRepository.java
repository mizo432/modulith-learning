package undecided.association.relationshipMgmt.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織単位（OrgUnit）のJPAリポジトリインターフェースです。
 *
 * <p>このリポジトリは、組織単位エンティティの永続化操作を提供します。
 */
@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, SnowflakeId> {}
