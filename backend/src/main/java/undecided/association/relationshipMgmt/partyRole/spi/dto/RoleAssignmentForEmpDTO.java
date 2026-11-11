package undecided.association.relationshipMgmt.partyRole.spi.dto;

import java.util.UUID;
import org.jspecify.annotations.NonNull;

/**
 * 従業員へのロール割り当て情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record RoleAssignmentForEmpDTO(
    UUID roleAssignmentForEmpId,
    @NonNull UUID roleId,
    @NonNull UUID employeeId) {

  /**
   * 従業員へのロール割り当てDTOを作成します。
   *
   * @param roleAssignmentForEmpId 割り当てID
   * @param roleId ロールID
   * @param employeeId 従業員ID
   * @return 従業員へのロール割り当てDTO
   */
  public static RoleAssignmentForEmpDTO create(
      UUID roleAssignmentForEmpId,
      @NonNull UUID roleId,
      @NonNull UUID employeeId) {
    if (roleId == null) {
      throw new IllegalArgumentException("roleId must not be null");
    }
    if (employeeId == null) {
      throw new IllegalArgumentException("employeeId must not be null");
    }
    return new RoleAssignmentForEmpDTO(roleAssignmentForEmpId, roleId, employeeId);
  }
}
