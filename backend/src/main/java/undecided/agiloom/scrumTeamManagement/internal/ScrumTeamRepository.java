package undecided.agiloom.scrumTeamManagement.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeamStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface ScrumTeamRepository
    extends CrudRepository<ScrumTeam, SnowflakeId>, PagingAndSortingRepository<ScrumTeam, SnowflakeId> {

  List<ScrumTeam> findByProductId(UUID productId);

  List<ScrumTeam> findByProductIdAndDeletedAtIsNull(UUID productId);

  List<ScrumTeam> findByStatus(ScrumTeamStatus status);

  Optional<ScrumTeam> findByIdAndDeletedAtIsNull(SnowflakeId id);

  Optional<ScrumTeam> findByPoEmployeeIdAndDeletedAtIsNull(UUID poEmployeeId);
}
