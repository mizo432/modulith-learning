package undecided.agiloom.requirementBacklog.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 管理者（ADMINロール保持者）の従業員IDを保持するサブセット（射影）。
 *
 * <p>association 領域の {@link undecided.association.relationshipMgmt.partyRole.spi.role.AdminEmployeeChanged}
 * イベントを受けて更新され、コメント削除時の権限判定に使用される。
 * モジュール内部でのみ使用するため、パッケージプライベートとします。
 */
@Entity
@Table(schema = "undecided_ag", name = "admin_employee_subsets")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class AdminEmployeeSubset {

  /** 従業員ID（主キー）。 */
  @Id
  @Column(name = "employee_id", nullable = false)
  protected SnowflakeId employeeId;

  /** サブセットが最後にイベントで更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** 更新日時を現在に更新します。 */
  void touch() {
    this.updatedAt = LocalDateTime.now();
  }

  /** イベントから新しいサブセットを作成します。 */
  static AdminEmployeeSubset of(@NonNull SnowflakeId employeeId) {
    return AdminEmployeeSubset.builder().employeeId(employeeId).build();
  }
}
