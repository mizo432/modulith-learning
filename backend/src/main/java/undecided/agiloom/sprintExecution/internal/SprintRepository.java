package undecided.agiloom.sprintExecution.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface SprintRepository extends CrudRepository<Sprint, SnowflakeId>, PagingAndSortingRepository<Sprint, SnowflakeId> {

    List<Sprint> findByProductId(SnowflakeId productId);

    Optional<Sprint> findByProductIdAndSprintNumber(SnowflakeId productId, Integer sprintNumber);

    List<Sprint> findByTeamId(SnowflakeId teamId);

    /**
     * チームIDとスプリント番号でソートされたスプリント一覧を返します。
     * スプリント0が常に先頭に来る様に、sprintNumberで昇順ソートします。
     */
    List<Sprint> findByTeamIdOrderBySprintNumberAsc(SnowflakeId teamId);

    /**
     * 指定されたチームのスプリント0を検索します。
     */
    Optional<Sprint> findByTeamIdAndIsSprintZero(SnowflakeId teamId, Boolean isSprintZero);

    /**
     * 指定された製品とチームのスプリント0を検索します。
     */
    Optional<Sprint> findByProductIdAndTeamIdAndIsSprintZero(SnowflakeId productId, SnowflakeId teamId, Boolean isSprintZero);

    List<Sprint> findByStatus(SprintStatus status);
}
