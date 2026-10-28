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
 * 組織基本情報を表すエンティティクラス。
 *
 * <p>このクラスは、組織の基本情報（組織名、種類、階層など）を管理します。
 */
@Entity
@Table(name = "organization", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization {
  @Id
  @Column(name = "organization_id")
  private SnowflakeId organizationId;

  /** パーティーID。 */
  @Column(name = "party_id", nullable = false, unique = true)
  private SnowflakeId partyId;

  /** 組織コード（一意制約）。 */
  @Column(name = "organization_code", nullable = false, unique = true, length = 30)
  private String organizationCode;

  /** 組織名。 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** 組織種類コード。 */
  @Column(name = "organization_type_code", nullable = false, length = 20)
  private String organizationTypeCode;

  /** 親組織ID（自己参照）。 */
  @Column(name = "parent_organization_id")
  private SnowflakeId parentOrganizationId;

  /** 状態（有効/無効）。 */
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 組織を作成します。
   *
   * @param organizationId 組織ID
   * @param partyId パーティーID
   * @param organizationCode 組織コード
   * @param name 組織名
   * @param organizationTypeCode 組織種類コード
   * @param parentOrganizationId 親組織ID
   * @param status 状態
   * @param validFrom 有効開始日
   */
  public Organization(
      SnowflakeId organizationId,
      SnowflakeId partyId,
      String organizationCode,
      String name,
      String organizationTypeCode,
      SnowflakeId parentOrganizationId,
      String status,
      LocalDateTime validFrom) {
    this.organizationId = organizationId;
    this.partyId = partyId;
    this.organizationCode = organizationCode;
    this.name = name;
    this.organizationTypeCode = organizationTypeCode;
    this.parentOrganizationId = parentOrganizationId;
    this.status = status;
    this.validFrom = validFrom;
  }
}
