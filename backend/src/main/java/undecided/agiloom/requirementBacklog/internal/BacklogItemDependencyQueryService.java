package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの依存関係を取得するクエリサービス（Issue #541）。
 *
 * <p>依存の充足は直接の依存先だけを見て判定します（Issue #665）。依存先が DONE のとき充足します。
 */
@Service
@Transactional(readOnly = true)
class BacklogItemDependencyQueryService {

  private final BacklogItemRepository backlogItemRepository;
  private final BacklogItemDependencyRepository dependencyRepository;

  BacklogItemDependencyQueryService(
      BacklogItemRepository backlogItemRepository,
      BacklogItemDependencyRepository dependencyRepository) {
    this.backlogItemRepository = backlogItemRepository;
    this.dependencyRepository = dependencyRepository;
  }

  BacklogItemDependenciesResponse getDependencies(@NonNull SnowflakeId backlogItemId) {
    List<BacklogItemDependencyResponse> dependencies =
        dependencyRepository.findByBacklogItemId(backlogItemId).stream()
            .map(dependency -> toDependencyResponse(dependency.getDependencyItemId()))
            .toList();

    List<BacklogItemDependency> dependentRelations =
        dependencyRepository.findByDependencyItemId(backlogItemId);
    List<BacklogItemDependencyResponse> dependents = List.of();
    if (!dependentRelations.isEmpty()) {
      boolean selfDone = loadItem(backlogItemId).getStatus() == BacklogItemStatus.DONE;
      dependents =
          dependentRelations.stream()
              .map(dependency -> toDependentResponse(dependency.getBacklogItemId(), selfDone))
              .toList();
    }

    int unmetCount = (int) dependencies.stream().filter(entry -> !entry.isSatisfied()).count();
    return BacklogItemDependenciesResponse.builder()
        .backlogItemId(backlogItemId)
        .dependencies(dependencies)
        .dependents(dependents)
        .dependenciesSatisfied(unmetCount == 0)
        .unmetDependencyCount(unmetCount)
        .build();
  }

  /** 一覧・詳細レスポンス用の依存要約を返します。 */
  BacklogItemDependencySummary summarize(@NonNull SnowflakeId backlogItemId) {
    List<BacklogItemDependency> dependencies = dependencyRepository.findByBacklogItemId(backlogItemId);
    boolean satisfied =
        dependencies.stream()
            .allMatch(
                dependency -> loadItem(dependency.getDependencyItemId()).getStatus() == BacklogItemStatus.DONE);
    return new BacklogItemDependencySummary(dependencies.size(), satisfied);
  }

  private BacklogItemDependencyResponse toDependencyResponse(SnowflakeId dependencyItemId) {
    BacklogItem item = loadItem(dependencyItemId);
    return BacklogItemDependencyResponse.builder()
        .backlogItemId(item.getId())
        .title(item.getTitle())
        .status(item.getStatus())
        .statusLabel(item.getStatus().label())
        .satisfied(item.getStatus() == BacklogItemStatus.DONE)
        .build();
  }

  private BacklogItemDependencyResponse toDependentResponse(SnowflakeId dependentItemId, boolean selfDone) {
    BacklogItem item = loadItem(dependentItemId);
    return BacklogItemDependencyResponse.builder()
        .backlogItemId(item.getId())
        .title(item.getTitle())
        .status(item.getStatus())
        .statusLabel(item.getStatus().label())
        .satisfied(selfDone)
        .build();
  }

  private BacklogItem loadItem(SnowflakeId backlogItemId) {
    return backlogItemRepository
        .findById(backlogItemId)
        .orElseThrow(() -> new IllegalArgumentException("BacklogItem not found: " + backlogItemId));
  }
}
