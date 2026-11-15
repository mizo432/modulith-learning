package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintRepository extends CrudRepository<Sprint, SnowflakeId>, PagingAndSortingRepository<Sprint, SnowflakeId> {

    List<Sprint> findByProductId(UUID productId);

    Optional<Sprint> findByProductIdAndSprintNumber(UUID productId, Integer sprintNumber);

    List<Sprint> findByTeamId(UUID teamId);

    /**
     * チームIDとスプリント番号でソートされたスプリント一覧を返します。
     * スプリント0が常に先頭に来る様に、sprintNumberで昇順ソートします。
     */
    List<Sprint> findByTeamIdOrderBySprintNumberAsc(UUID teamId);

    /**
     * 指定されたチームのスプリント0を検索します。
     */
    Optional<Sprint> findByTeamIdAndIsSprintZero(UUID teamId, Boolean isSprintZero);

    /**
     * 指定された製品とチームのスプリント0を検索します。
     */
    Optional<Sprint> findByProductIdAndTeamIdAndIsSprintZero(UUID productId, UUID teamId, Boolean isSprintZero);

    List<Sprint> findByStatus(SprintStatus status);
}
