package undecided.agiloom.sprintExecution.internal.command;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintZeroFactory;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@link SprintZeroFactory}の実装です。
 * スプリント0を作成し、リポジトリに保存します。
 */
@Component
public class SprintZeroFactoryImpl implements SprintZeroFactory {

    private final SprintRepository sprintRepository;

    public SprintZeroFactoryImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public Optional<Sprint> createSprintZero(@NonNull SnowflakeId productId, @NonNull SnowflakeId teamId, @NonNull String teamName) {
        // 既にスプリント0が存在する場合は既存のものを返す
        Optional<Sprint> existing = sprintRepository.findByProductIdAndTeamIdAndIsSprintZero(productId, teamId, true);
        if (existing.isPresent()) {
            return existing;
        }

        Sprint sprintZero = Sprint.createSprintZero(productId, teamId, teamName);
        sprintRepository.save(sprintZero);
        return Optional.of(sprintZero);
    }
}
