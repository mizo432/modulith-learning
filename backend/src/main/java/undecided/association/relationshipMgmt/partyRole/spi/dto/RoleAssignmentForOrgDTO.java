package undecided.association.relationshipMgmt.partyRole.spi.dto;

import java.util.UUID;
import org.jspecify.annotations.NonNull;

/**
 * 組織へのロール割り当て情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record RoleAssignmentForOrgDTO(
    UUID roleAssignmentForOrgId,
    @NonNull UUID roleId,
    @NonNull UUID organizationId) {

  /**
   * 組織へのロール割り当てDTOを作成します。
   *
   * @param roleAssignmentForOrgId 割り当てID
   * @param roleId ロールID
   * @param organizationId 組織ID
   * @return 組織へのロール割り当てDTO
   */
  public static RoleAssignmentForOrgDTO create(
      UUID roleAssignmentForOrgId,
      @NonNull UUID roleId,
      @NonNull UUID organizationId) {
    if (roleId == null) {
      throw new IllegalArgumentException("roleId must not be null");
    }
    if (organizationId == null) {
      throw new IllegalArgumentException("organizationId must not be null");
    }
    return new RoleAssignmentForOrgDTO(roleAssignmentForOrgId, roleId, organizationId);
  }
}
