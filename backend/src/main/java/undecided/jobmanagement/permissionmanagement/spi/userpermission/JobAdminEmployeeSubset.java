package undecided.jobmanagement.permissionmanagement.spi.userpermission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 管理者（ADMINロール保持者）の従業員IDを保持するサブセット（射影）。
 *
 * <p>association 領域の AdminEmployeeChanged イベントを受けて更新され、 ジョブ起動時の権限判定に使用します。
 */
@Entity
@Table(schema = "job_management", name = "admin_employee_subsets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobAdminEmployeeSubset {

  @Id
  @Column(name = "employee_id", nullable = false)
  private SnowflakeId employeeId;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public static JobAdminEmployeeSubset of(SnowflakeId employeeId) {
    JobAdminEmployeeSubset subset = new JobAdminEmployeeSubset();
    subset.employeeId = employeeId;
    subset.updatedAt = LocalDateTime.now();
    return subset;
  }
}
