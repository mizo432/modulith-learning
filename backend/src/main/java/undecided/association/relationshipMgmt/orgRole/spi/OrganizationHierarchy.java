package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織階層を表すエンティティクラス。
 *
 * <p>このクラスは、組織の階層構造を管理します。
 * partyIdを使用してParty体系と統合されています。
 */
@Entity
@Table(name = "organization_hierarchy", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationHierarchy {
  @Id
  @Column(name = "hierarchy_id")
  private SnowflakeId hierarchyId;

  /** 組織のパーティーID。 */
  @Column(name = "party_id", nullable = false)
  private SnowflakeId partyId;

  /** 親組織のパーティーID。 */
  @Column(name = "parent_party_id", nullable = false)
  private SnowflakeId parentPartyId;

  /** 階層レベル。 */
  @Column(name = "level", nullable = false)
  private int level;

  /** 順序。 */
  @Column(name = "`order`", nullable = false)
  private int order;

  /**
   * 組織階層を作成します。
   *
   * @param hierarchyId 階層ID
   * @param partyId 組織のパーティーID
   * @param parentPartyId 親組織のパーティーID
   * @param level 階層レベル
   * @param order 順序
   */
  public OrganizationHierarchy(
      SnowflakeId hierarchyId,
      SnowflakeId partyId,
      SnowflakeId parentPartyId,
      int level,
      int order) {
    this.hierarchyId = hierarchyId;
    this.partyId = partyId;
    this.parentPartyId = parentPartyId;
    this.level = level;
    this.order = order;
  }
}
