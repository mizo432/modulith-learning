package undecided.agiloom.sprintExecution.internal.query;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
public class SprintQueryService implements SprintQuery {

    private final SprintRepository sprintRepository;
    private final SprintResponseAssembler sprintResponseAssembler;

    public SprintQueryService(SprintRepository sprintRepository,
                              SprintResponseAssembler sprintResponseAssembler) {
        this.sprintRepository = sprintRepository;
        this.sprintResponseAssembler = sprintResponseAssembler;
    }

    public Optional<SprintDTO> findById(@NonNull SnowflakeId id) {
        return sprintRepository.findByIdAndDeletedAtIsNull(id).map(sprintResponseAssembler::toDTO);
    }

    public List<SprintDTO> findAll(@NonNull Pageable pageable) {
        return sprintRepository.findAllByDeletedAtIsNull(pageable).getContent().stream()
            .map(sprintResponseAssembler::toDTO)
            .toList();
    }

    public List<SprintDTO> findByStatus(@NonNull SprintStatus status) {
        return sprintRepository.findByStatusAndDeletedAtIsNull(status).stream()
            .map(sprintResponseAssembler::toDTO)
            .toList();
    }
}
