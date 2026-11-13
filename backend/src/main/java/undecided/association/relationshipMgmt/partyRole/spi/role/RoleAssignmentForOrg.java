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

/**
 * 組織へのロール割り当てを表すエンティティクラス。
 *
 * <p>このクラスは、組織にロールを割り当てるための関連付けを管理します。
 * 複数のロールを複数の組織に割り当てることができます。
 *
 * <p>C6修正により{@code @Setter}は削除され、{@code create()}ファクトリメソッドを使用してインスタンスを作成します。
 */
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

  /** ロール割り当ての一意な識別子。 */
  @Id
  protected UUID roleAssignmentForOrgId;

  /** 割り当てられるロールのID。 */
  @Column(name = "role_id")
  protected UUID roleId;

  /** ロールが割り当てられる組織。 */
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
