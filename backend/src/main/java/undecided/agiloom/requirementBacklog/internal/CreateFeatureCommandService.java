package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * フィーチャー作成サービス。
 */
class CreateFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  CreateFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      @NonNull SnowflakeIdGenerator snowflakeIdGenerator) {
    this.featureRepository = featureRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  /**
   * フィーチャーを作成します。
   *
   * @param command 作成コマンド
   * @return 作成されたフィーチャーのID
   */
  public SnowflakeId execute(@NonNull CreateFeatureCommand command) {
    return null;
  }
}
