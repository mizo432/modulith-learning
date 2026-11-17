package undecided.association.relationshipMgmt.partyRole.spi.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

/**
 * 従業員へのロール割り当てを表すエンティティクラス。
 *
 * <p>このクラスは、従業員にロールを割り当てるための関連付けを管理します。
 * 複数のロールを複数の従業員に割り当てることができます。
 *
 * <p>C6修正により{@code @Setter}は削除され、{@code create()}ファクトリメソッドを使用してインスタンスを作成します。
 */
@Entity
@Table(
    name = "role_assignment_for_emp",
    indexes = {
      @Index(name = "fk01_role_assignment_for_emp", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_emp", columnList = "employee_id")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAssignmentForEmp {

  /** ロール割り当ての一意な識別子。 */
  @Id
  protected UUID roleAssignmentForEmpId;

  /** 割り当てられるロールのID。 */
  @Column(name = "role_id")
  protected UUID roleId;

  /** ロールが割り当てられる従業員のID。 */
  @Column(name = "employee_id")
  protected UUID employeeId;

  /**
   * 従業員のロール割り当てを新規作成します。
   *
   * @param roleAssignmentForEmpId 割り当てID
   * @param roleId ロールID
   * @param employeeId 従業員ID
   * @return 新規作成されたRoleAssignmentForEmpインスタンス
   */
  public static RoleAssignmentForEmp create(UUID roleAssignmentForEmpId, UUID roleId, UUID employeeId) {
    RoleAssignmentForEmp entity = new RoleAssignmentForEmp();
    entity.roleAssignmentForEmpId = roleAssignmentForEmpId;
    entity.roleId = roleId;
    entity.employeeId = employeeId;
    return entity;
  }
}
