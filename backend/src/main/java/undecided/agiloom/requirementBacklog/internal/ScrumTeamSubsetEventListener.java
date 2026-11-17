package undecided.agiloom.requirementBacklog.internal;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;

/**
 * スクラムチームの変更イベントを受けて、自領域のサブセットを最新化するリスナー。
 *
 * <p>productDiscovery のテーブルは参照せず、イベントのスナップショットだけで
 * {@link ScrumTeamSubset} を作成・更新・削除します。
 */
@Component
class ScrumTeamSubsetEventListener {

  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  ScrumTeamSubsetEventListener(ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  @ApplicationModuleListener
  void on(ScrumTeamChanged event) {
    if (event.removed()) {
      scrumTeamSubsetRepository.deleteById(event.scrumTeamId());
      return;
    }
    scrumTeamSubsetRepository
        .findById(event.scrumTeamId())
        .ifPresentOrElse(
            subset -> {
              subset.apply(event);
              scrumTeamSubsetRepository.save(subset);
            },
            () -> scrumTeamSubsetRepository.save(ScrumTeamSubset.from(event)));
  }
}
