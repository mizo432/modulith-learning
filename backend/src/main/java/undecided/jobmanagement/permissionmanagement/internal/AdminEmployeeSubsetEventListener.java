package undecided.jobmanagement.permissionmanagement.internal;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import undecided.supporting.event.AdminEmployeeChanged;

/** 管理者（ADMINロール）の変更イベントを受けて、自領域のサブセットを最新化するリスナー。 */
@Component
class AdminEmployeeSubsetEventListener {

  private final AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  AdminEmployeeSubsetEventListener(AdminEmployeeSubsetRepository adminEmployeeSubsetRepository) {
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
  }

  @EventListener
  void on(AdminEmployeeChanged event) {
    // TODO: #748 で実装する
  }
}
