package undecided.agiloom.productDiscovery.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

public interface ScrumTeamRepository
    extends CrudRepository<ScrumTeam, SnowflakeId>, PagingAndSortingRepository<ScrumTeam, SnowflakeId> {

  List<ScrumTeam> findByProductId(SnowflakeId productId);
}
