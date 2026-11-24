package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
class SprintFacadeImpl implements SprintFacade {

  /** 割り当て可能な未来スプリントの数（進行中を含む）。#650 の決定による。 */
  private static final int ASSIGNABLE_WINDOW_SIZE = 4;

  private final SprintRepository sprintRepository;

  SprintFacadeImpl(SprintRepository sprintRepository) {
    this.sprintRepository = sprintRepository;
  }

  @Override
  public Optional<Sprint> findById(@NonNull SnowflakeId sprintId) {
    return sprintRepository.findById(sprintId);
  }

  @Override
  public List<Sprint> findAssignableSprints(@NonNull SnowflakeId teamId) {
    List<Sprint> candidates =
        sprintRepository.findByTeamIdOrderBySprintNumberAsc(teamId).stream()
            .filter(sprint -> !sprint.isSprintZero())
            .filter(
                sprint ->
                    sprint.getStatus() == SprintStatus.ACTIVE
                        || sprint.getStatus() == SprintStatus.PLANNED)
            .toList();
    int anchor = 0;
    for (int i = 0; i < candidates.size(); i++) {
      if (candidates.get(i).getStatus() == SprintStatus.ACTIVE) {
        anchor = i;
        break;
      }
    }
    int end = Math.min(anchor + ASSIGNABLE_WINDOW_SIZE, candidates.size());
    return List.copyOf(candidates.subList(anchor, end));
  }

  @Override
  public Optional<Sprint> findNextSprint(@NonNull SnowflakeId teamId, int afterSprintNumber) {
    return sprintRepository.findByTeamIdOrderBySprintNumberAsc(teamId).stream()
        .filter(sprint -> !sprint.isSprintZero())
        .filter(sprint -> sprint.getSprintNumber() > afterSprintNumber)
        .filter(
            sprint ->
                sprint.getStatus() == SprintStatus.ACTIVE
                    || sprint.getStatus() == SprintStatus.PLANNED)
        .findFirst();
  }
}
