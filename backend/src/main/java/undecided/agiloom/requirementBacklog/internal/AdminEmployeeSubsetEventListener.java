package undecided.agiloom.requirementBacklog.internal;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import undecided.supporting.event.AdminEmployeeChanged;

/**
 * 管理者（ADMINロール）の変更イベントを受けて、自領域のサブセットを最新化するリスナー。
 *
 * <p>association のテーブルは参照せず、イベントのスナップショットだけで
 * {@link AdminEmployeeSubset} を作成・削除します。
 */
@Component
class AdminEmployeeSubsetEventListener {

  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;
  private final Clock clock;

  AdminEmployeeSubsetEventListener(AdminEmployeeSubsetRepository adminEmployeeSubsetRepository, Clock clock) {
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
    this.clock = clock;
  }

  /**
   * ADMINロールの付与・剥離イベントを受信してサブセットを更新します。
   *
   * @param event 管理者変更イベント
   */
  @EventListener
  void on(AdminEmployeeChanged event) {
    if (event.isAdmin()) {
      AdminEmployeeSubset subset = AdminEmployeeSubset.of(event.employeeId(), LocalDateTime.now(clock));
      adminEmployeeSubsetRepository.save(subset);
    } else {
      adminEmployeeSubsetRepository.deleteById(event.employeeId());
    }
  }
}
