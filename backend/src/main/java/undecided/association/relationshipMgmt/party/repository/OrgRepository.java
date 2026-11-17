package undecided.association.relationshipMgmt.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織（Org）のJPAリポジトリインターフェースです。
 *
 * <p>このリポジトリは、組織エンティティの永続化操作を提供します。
 */
@Repository
public interface OrgRepository extends JpaRepository<Org, SnowflakeId> {}
