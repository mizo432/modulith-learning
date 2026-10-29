package undecided.agiloom.sprintExecution.internal.query;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
public class SprintQueryService {

    private final SprintRepository sprintRepository;

    public SprintQueryService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public Optional<Sprint> findById(@NonNull SnowflakeId id) {
        return sprintRepository.findById(id);
    }

    public List<Sprint> findAll(Pageable pageable) {
        return sprintRepository.findAll(pageable).getContent();
    }

    public List<Sprint> findByStatus(@NonNull SprintStatus status) {
        return sprintRepository.findByStatus(status);
    }
}
