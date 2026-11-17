package undecided.agiloom.productDiscovery.internal.query;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
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

  public List<ScrumTeam> findByProductId(@NonNull SnowflakeId productId) {
    return scrumTeamRepository.findByProductId(productId);
  }

  public List<ScrumTeam> findAll(Pageable pageable) {
    return scrumTeamRepository.findAll(pageable).getContent();
  }
}
