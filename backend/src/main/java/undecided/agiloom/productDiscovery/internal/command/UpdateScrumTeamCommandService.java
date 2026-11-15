package undecided.agiloom.productDiscovery.internal.command;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class UpdateScrumTeamCommandService {

  private final ScrumTeamRepository scrumTeamRepository;

  public UpdateScrumTeamCommandService(ScrumTeamRepository scrumTeamRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
  }

  public void execute(@NonNull UpdateScrumTeamCommand command) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(command.getId())
            .orElseThrow(
                () -> new IllegalArgumentException("ScrumTeam not found: " + command.getId()));

    scrumTeam.update(
        command.getName(),
        command.getBusinessDomain(),
        command.getPoEmployeeId(),
        command.getPoName());
  }

  public void delete(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeamRepository.delete(scrumTeam);
  }

  public void assignPo(@NonNull SnowflakeId id, @NonNull String poEmployeeId, @NonNull String poName) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.assignPo(java.util.UUID.fromString(poEmployeeId), poName);
  }

  public void unassignPo(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.unassignPo();
  }
}
