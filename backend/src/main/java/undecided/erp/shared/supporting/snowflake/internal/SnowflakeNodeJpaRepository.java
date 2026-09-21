package undecided.erp.shared.supporting.snowflake.internal;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface SnowflakeNodeJpaRepository extends JpaRepository<SnowflakeNode, Integer> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      select n
      from SnowflakeNode n
      where n.nodeId = :nodeId
      """)
  Optional<SnowflakeNode> findByIdForUpdate(int nodeId);


}
