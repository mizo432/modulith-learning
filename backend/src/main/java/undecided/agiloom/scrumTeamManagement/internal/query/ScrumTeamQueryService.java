package undecided.agiloom.scrumTeamManagement.internal.query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.scrumTeamManagement.internal.ScrumTeamRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeamStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
public class ScrumTeamQueryService {

  private final ScrumTeamRepository scrumTeamRepository;

  public ScrumTeamQueryService(ScrumTeamRepository scrumTeamRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
  }

  public Optional<ScrumTeam> findById(@NonNull SnowflakeId id) {
    return scrumTeamRepository.findById(id);
  }

  public List<ScrumTeam> findAll(Pageable pageable) {
    return scrumTeamRepository.findAll(pageable).getContent();
  }

  public List<ScrumTeam> findByProductId(@NonNull UUID productId) {
    return scrumTeamRepository.findByProductIdAndDeletedAtIsNull(productId);
  }

  public List<ScrumTeam> findByStatus(@NonNull ScrumTeamStatus status) {
    return scrumTeamRepository.findByStatus(status);
  }
}
