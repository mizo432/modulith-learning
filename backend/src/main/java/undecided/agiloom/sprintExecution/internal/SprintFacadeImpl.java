package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.supporting.snowflake.SnowflakeId;

@Service
class SprintFacadeImpl implements SprintFacade {

  private final SprintRepository sprintRepository;

  SprintFacadeImpl(SprintRepository sprintRepository) {
    this.sprintRepository = sprintRepository;
  }

  @Override
  public Optional<Sprint> findById(@NonNull SnowflakeId sprintId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<Sprint> findAssignableSprints(@NonNull SnowflakeId teamId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<Sprint> findNextSprint(@NonNull SnowflakeId teamId, int afterSprintNumber) {
    throw new UnsupportedOperationException("not implemented");
  }
}
