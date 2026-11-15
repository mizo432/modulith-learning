package undecided.agiloom.scrumTeamManagement.internal.command;

import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.scrumTeamManagement.internal.ScrumTeamRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
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
        command.getTeamName(), command.getBusinessDomain(), command.getDescription());
  }

  public void assignPo(@NonNull SnowflakeId id, @NonNull UUID poEmployeeId) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.assignPo(poEmployeeId);
  }

  public void unassignPo(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.unassignPo();
  }

  public void archive(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.archive();
  }

  public void delete(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.delete();
  }
}
