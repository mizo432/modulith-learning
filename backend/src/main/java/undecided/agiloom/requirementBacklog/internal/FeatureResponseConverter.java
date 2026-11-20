package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャーエンティティを FeatureResponse DTO に変換するコンバーターです。
 */
@Component
class FeatureResponseConverter {

  FeatureResponse toFeatureResponse(Feature feature) {
    return FeatureResponse.builder()
        .id(feature.getId())
        .name(feature.getName())
        .description(feature.getDescription())
        .status(feature.getStatus())
        .statusLabel(feature.getStatus().label())
        .scrumTeamId(String.valueOf(feature.getScrumTeamId().value()))
        .createdAt(feature.getCreatedAt())
        .updatedAt(feature.getUpdatedAt())
        .build();
  }

  FeatureResponse toFeatureResponse(
      Feature feature,
      FeatureQueryService.FeatureProgress progress,
      List<SnowflakeId> backlogItemIds) {
    return FeatureResponse.builder()
        .id(feature.getId())
        .name(feature.getName())
        .description(feature.getDescription())
        .status(feature.getStatus())
        .statusLabel(feature.getStatus().label())
        .scrumTeamId(String.valueOf(feature.getScrumTeamId().value()))
        .createdAt(feature.getCreatedAt())
        .updatedAt(feature.getUpdatedAt())
        .backlogItemIds(backlogItemIds)
        .completedStoryPoints(progress.completedStoryPoints())
        .totalStoryPoints(progress.totalStoryPoints())
        .progressPercentage(progress.progressPercentage())
        .build();
  }
}
