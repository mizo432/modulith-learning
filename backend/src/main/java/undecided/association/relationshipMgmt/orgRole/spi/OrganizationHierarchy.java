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
 */
@Entity
@Table(name = "organization_hierarchy", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationHierarchy {
  @Id
  @Column(name = "hierarchy_id")
  private SnowflakeId hierarchyId;

  /** 組織ID。 */
  @Column(name = "organization_id", nullable = false)
  private SnowflakeId organizationId;

  /** 親組織ID。 */
  @Column(name = "parent_organization_id", nullable = false)
  private SnowflakeId parentOrganizationId;

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
   * @param organizationId 組織ID
   * @param parentOrganizationId 親組織ID
   * @param level 階層レベル
   * @param order 順序
   */
  public OrganizationHierarchy(
      SnowflakeId hierarchyId,
      SnowflakeId organizationId,
      SnowflakeId parentOrganizationId,
      int level,
      int order) {
    this.hierarchyId = hierarchyId;
    this.organizationId = organizationId;
    this.parentOrganizationId = parentOrganizationId;
    this.level = level;
    this.order = order;
  }
}
