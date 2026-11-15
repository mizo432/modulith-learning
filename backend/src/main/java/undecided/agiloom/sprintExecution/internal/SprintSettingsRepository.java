package undecided.agiloom.sprintExecution.internal;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

/** スプリント設定リポジトリ。 */
public interface SprintSettingsRepository extends CrudRepository<SprintSettings, SnowflakeId> {

  /**
   * プロダクトIDでスプリント設定を検索します。
   *
   * @param productId プロダクトのID
   * @return 該当するスプリント設定のOptional
   */
  Optional<SprintSettings> findByProductId(UUID productId);
}
