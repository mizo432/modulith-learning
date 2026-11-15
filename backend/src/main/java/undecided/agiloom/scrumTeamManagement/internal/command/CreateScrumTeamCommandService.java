package undecided.agiloom.scrumTeamManagement.internal.command;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.scrumTeamManagement.internal.ScrumTeamRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateScrumTeamCommandService {

  private final ScrumTeamRepository scrumTeamRepository;

  public CreateScrumTeamCommandService(ScrumTeamRepository scrumTeamRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
  }

  public SnowflakeId execute(@NonNull CreateScrumTeamCommand command) {
    ScrumTeam scrumTeam =
        ScrumTeam.builder()
            .productId(command.getProductId())
            .teamName(command.getTeamName())
            .businessDomain(command.getBusinessDomain())
            .description(command.getDescription())
            .poEmployeeId(command.getPoEmployeeId())
            .build();
    ScrumTeam saved = scrumTeamRepository.save(scrumTeam);
    return saved.getId();
  }
}
