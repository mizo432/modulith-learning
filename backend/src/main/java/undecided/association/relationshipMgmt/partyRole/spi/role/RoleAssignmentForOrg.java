package undecided.association.relationshipMgmt.partyRole.spi.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import undecided.association.relationshipMgmt.spi.party.org.Org;

@Entity
@Table(
    name = "role_assignment_for_org",
    indexes = {
      @Index(name = "fk01_role_assignment_for_org", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_org", columnList = "org_id")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAssignmentForOrg {
  @Id protected UUID roleAssignmentForOrgId;

  @Column(name = "role_id")
  protected UUID roleId;

  @ManyToOne
  @JoinColumn(name = "org_id")
  protected Org org;

  /**
   * 組織のロール割り当てを新規作成します。
   *
   * @param roleAssignmentForOrgId 割り当てID
   * @param roleId ロールID
   * @param org 組織エンティティ
   * @return 新規作成されたRoleAssignmentForOrgインスタンス
   */
  public static RoleAssignmentForOrg create(UUID roleAssignmentForOrgId, UUID roleId, Org org) {
    RoleAssignmentForOrg entity = new RoleAssignmentForOrg();
    entity.roleAssignmentForOrgId = roleAssignmentForOrgId;
    entity.roleId = roleId;
    entity.org = org;
    return entity;
  }
}
