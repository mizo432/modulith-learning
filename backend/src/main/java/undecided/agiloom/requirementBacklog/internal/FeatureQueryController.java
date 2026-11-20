package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
class FeatureQueryController {

  private final FeatureQueryService featureQueryService;
  private final FeatureResponseConverter featureResponseConverter;

  FeatureQueryController(
      FeatureQueryService featureQueryService,
      FeatureResponseConverter featureResponseConverter) {
    this.featureQueryService = featureQueryService;
    this.featureResponseConverter = featureResponseConverter;
  }

  @GetMapping("/api/v1/features")
  @Transactional(readOnly = true)
  List<FeatureResponse> getFeatures(@RequestParam String scrumTeamId) {
    SnowflakeId teamId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    List<Feature> features = featureQueryService.findByScrumTeamId(teamId);
    return features.stream()
        .map(featureResponseConverter::toFeatureResponse)
        .toList();
  }

  @GetMapping("/api/v1/features/{id}")
  @Transactional(readOnly = true)
  FeatureResponse getFeature(@PathVariable String id) {
    SnowflakeId featureId = SnowflakeId.of(Long.parseLong(id));
    Feature feature = featureQueryService.findById(featureId)
        .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + featureId));
    FeatureQueryService.FeatureProgress progress = featureQueryService.calculateProgress(featureId);
    List<SnowflakeId> backlogItemIds = featureQueryService.findBacklogItemIds(featureId);
    return featureResponseConverter.toFeatureResponse(feature, progress, backlogItemIds);
  }
}
