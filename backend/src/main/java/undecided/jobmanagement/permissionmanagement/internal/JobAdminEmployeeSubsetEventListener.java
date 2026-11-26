package undecided.jobmanagement.permissionmanagement.internal;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import undecided.jobmanagement.permissionmanagement.spi.userpermission.JobAdminEmployeeSubset;
import undecided.supporting.event.AdminEmployeeChanged;

/**
 * 管理者（ADMINロール）の変更イベントを受けて、自領域のサブセットを最新化するリスナー。
 *
 * <p>association のテーブルは参照せず、イベントのスナップショットだけで {@link JobAdminEmployeeSubset}
 * を作成・削除します。
 */
@Component
class JobAdminEmployeeSubsetEventListener {

  private final JobAdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  JobAdminEmployeeSubsetEventListener(JobAdminEmployeeSubsetRepository adminEmployeeSubsetRepository) {
    this.adminEmployeeSubsetRepository = adminEmployeeSubsetRepository;
  }

  /**
   * ADMINロールの付与・剥離イベントを受信してサブセットを更新します。
   *
   * @param event 管理者変更イベント
   */
  @EventListener
  void on(AdminEmployeeChanged event) {
    if (event.isAdmin()) {
      adminEmployeeSubsetRepository.save(JobAdminEmployeeSubset.of(event.employeeId()));
    } else {
      adminEmployeeSubsetRepository.deleteById(event.employeeId());
    }
  }
}
