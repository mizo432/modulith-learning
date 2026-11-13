package undecided.association.relationshipMgmt.spi.party.orgUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.ValidInterval;

/**
 * 組織単位（部署、チームなど）を表すJPAエンティティクラス。
 *
 * <p>このクラスは {@link Party} を拡張し、組織単位特有の属性（組織単位名、組織単位コード、
 * 組織単位種類、親組織単位、状態など）を保持します。
 */
@Table(name = "OrgUnit", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrgUnit extends Party {

  /** 組織単位の名称。 */
  private OrgUnitName orgUnitName;

  /** 組織単位の有効期間（開始日から終了日までの範囲）。 */
  private ValidInterval validInterval;

  /** 組織単位コード（一意制約）。 */
  @Column(name = "org_unit_code", unique = true, length = 30)
  private OrgUnitCode orgUnitCode;

  /** 組織単位種類コード（DEPARTMENT, TEAM, SECTIONなど）。 */
  @Column(name = "org_unit_type_code", nullable = false, length = 20)
  private OrgUnitTypeCode orgUnitTypeCode;

  /** 親組織単位ID（自己参照）。 */
  @Column(name = "parent_org_unit_id")
  private SnowflakeId parentOrgUnitId;

  /** 組織単位の状態（ACTIVE, INACTIVEなど）。 */
  @Column(name = "status", nullable = false, length = 20)
  private OrgUnitStatus status;

  /** 所属する組織。OrgUnit は必ず1件の Org を持つ。 */
  @ManyToOne(optional = false)
  @JoinColumn(name = "org_id", nullable = false)
  private Org org;

  /** 上位組織（親組織単位）。 */
  @ManyToOne
  @JoinColumn(name = "parent_org_unit_id", insertable = false, updatable = false)
  private OrgUnit largers;

  /** 下位組織（子組織単位の一覧）。 */
  @OneToMany
  @JoinColumn(name = "parent_org_unit_id")
  private List<OrgUnit> smallers = new ArrayList<>();

  /**
   * 組織単位を新規作成します。
   *
   * @param partyId パーティID
   * @param code コード
   * @param validFrom 有効開始日
   * @param orgUnitName 組織単位名
   * @param orgUnitCode 組織単位コード
   * @param orgUnitTypeCode 組織単位種類コード
   * @param parentOrgUnitId 親組織単位ID（null可）
   * @param status 状態
   * @param org 所属する組織
   * @return 新規作成されたOrgUnitインスタンス
   */
  public static OrgUnit create(
      SnowflakeId partyId,
      String code,
      LocalDateTime validFrom,
      OrgUnitName orgUnitName,
      OrgUnitCode orgUnitCode,
      OrgUnitTypeCode orgUnitTypeCode,
      @Nullable SnowflakeId parentOrgUnitId,
      OrgUnitStatus status,
      Org org
  ) {
    OrgUnit orgUnit = new OrgUnit();
    orgUnit.partyId = partyId;
    orgUnit.partyTypeCode = PartyTypeCode.ORG_UNIT;
    orgUnit.code = code;
    orgUnit.validFrom = validFrom;
    orgUnit.validTo = null;
    orgUnit.govtAssignedId = null;
    orgUnit.searchName = null;
    orgUnit.orgUnitName = orgUnitName;
    orgUnit.validInterval = ValidInterval.empty();
    orgUnit.orgUnitCode = orgUnitCode;
    orgUnit.orgUnitTypeCode = orgUnitTypeCode;
    orgUnit.parentOrgUnitId = parentOrgUnitId;
    orgUnit.status = status;
    orgUnit.org = org;
    return orgUnit;
  }
}
