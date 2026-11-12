package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織ロール情報を表すエンティティクラス。
 *
 * <p>このクラスは、パーティーに割り当てられる組織ロールの情報を管理します。
 * PersonRoleと同様の構造を持ち、partyIdを共通の参照として使用します。
 */
@Entity
@Table(name = "organization_role", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationRole {
  @Id
  @Column(name = "organization_role_id")
  private SnowflakeId organizationRoleId;

  /** パーティーID。 */
  @Column(name = "party_id", nullable = false)
  private SnowflakeId partyId;

  /** ロールコード。 */
  @Column(name = "role_code", nullable = false, length = 30)
  private String roleCode;

  /** ロール名称。 */
  @Column(name = "role_name", nullable = false, length = 50)
  private String roleName;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 組織ロールを作成します。
   *
   * @param organizationRoleId 組織ロールID
   * @param partyId パーティーID
   * @param roleCode ロールコード
   * @param roleName ロール名称
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   */
  public OrganizationRole(
      SnowflakeId organizationRoleId,
      SnowflakeId partyId,
      String roleCode,
      String roleName,
      LocalDateTime validFrom,
      LocalDateTime validTo) {
    this.organizationRoleId = organizationRoleId;
    this.partyId = partyId;
    this.roleCode = roleCode;
    this.roleName = roleName;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
