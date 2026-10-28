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
 * <p>このクラスは、組織に割り当てられるロールの情報を管理します。
 */
@Entity
@Table(name = "organization_role", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationRole {
  @Id
  @Column(name = "organization_role_id")
  private SnowflakeId organizationRoleId;

  /** 組織ID。 */
  @Column(name = "organization_id", nullable = false)
  private SnowflakeId organizationId;

  /** ロール種類コード。 */
  @Column(name = "role_type_code", nullable = false, length = 20)
  private String roleTypeCode;

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
   * @param organizationId 組織ID
   * @param roleTypeCode ロール種類コード
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   */
  public OrganizationRole(
      SnowflakeId organizationRoleId,
      SnowflakeId organizationId,
      String roleTypeCode,
      LocalDateTime validFrom,
      LocalDateTime validTo) {
    this.organizationRoleId = organizationRoleId;
    this.organizationId = organizationId;
    this.roleTypeCode = roleTypeCode;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
