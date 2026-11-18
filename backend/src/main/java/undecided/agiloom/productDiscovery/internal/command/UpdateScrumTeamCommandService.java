package undecided.agiloom.productDiscovery.internal.command;

import java.util.List;
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
        command.getPoName(),
        command.getSmEmployeeId(),
        command.getSmName());
  }

  public void delete(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeamRepository.delete(scrumTeam);
  }

  public void assignPo(@NonNull SnowflakeId id, @NonNull SnowflakeId poEmployeeId, @NonNull String poName) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.assignPo(poEmployeeId, poName);
  }

  public void unassignPo(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.unassignPo();
  }

  public void assignSm(@NonNull SnowflakeId id, @NonNull SnowflakeId smEmployeeId, @NonNull String smName) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.assignSm(smEmployeeId, smName);
  }

  public void unassignSm(@NonNull SnowflakeId id) {
    ScrumTeam scrumTeam =
        scrumTeamRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));

    scrumTeam.unassignSm();
  }

  /**
   * チームメンバーの従業員IDリストを更新します。
   *
   * @param id スクラムチームID
   * @param memberEmployeeIds メンバーの従業員IDリスト
   */
  public void updateMembers(@NonNull SnowflakeId id, @NonNull List<SnowflakeId> memberEmployeeIds) {
    // TODO: 本実装は implement フェーズで行う
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
