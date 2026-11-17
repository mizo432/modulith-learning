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

    /**
     * チームに属するスプリント一覧をスプリント番号順で返します。
     * スプリント0が常に先頭に来ます。
     *
     * @param teamId チームのID
     * @return スプリント番号順のスプリント一覧
     */
    public List<Sprint> findByTeamIdOrderBySprintNumberAsc(@NonNull SnowflakeId teamId) {
        return sprintRepository.findByTeamIdOrderBySprintNumberAsc(teamId);
    }

    /**
     * 指定されたチームのスプリント0を検索します。
     *
     * @param teamId チームのID
     * @return スプリント0（存在する場合）
     */
    public Optional<Sprint> findSprintZeroByTeamId(@NonNull SnowflakeId teamId) {
        return sprintRepository.findByTeamIdAndIsSprintZero(teamId, true);
    }
}
