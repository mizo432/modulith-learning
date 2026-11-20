package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.Feature;
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
    // Validate name
    if (command.name().isEmpty()) {
      throw new IllegalArgumentException("Feature name must not be empty");
    }
    if (command.name().length() > Feature.NAME_MAX_LENGTH) {
      throw new IllegalArgumentException(
          "Feature name must not exceed " + Feature.NAME_MAX_LENGTH + " characters");
    }

    // Check team exists
    ScrumTeamSubset team = scrumTeamSubsetRepository.findById(command.scrumTeamId())
        .orElseThrow(() -> new IllegalArgumentException("Scrum team not found: " + command.scrumTeamId()));

    // Check team member permission
    if (!isTeamMember(team, command.actorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException("Not a team member");
    }

    // Create and save feature
    SnowflakeId id = snowflakeIdGenerator.nextIdValue();
    Feature feature = Feature.builder()
        .id(id)
        .name(command.name())
        .description(command.description())
        .scrumTeamId(command.scrumTeamId())
        .build();
    feature = featureRepository.save(feature);
    return feature.getId();
  }

  /**
   * チームメンバーかどうかを判定します。
   * {@link ScrumTeamSubset#isTeamMember} へ委譲します。
   */
  boolean isTeamMember(ScrumTeamSubset team, SnowflakeId actorEmployeeId) {
    return team.isTeamMember(actorEmployeeId);
  }
}
