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
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.ValidInterval;

/**
 * 組織単位（部署、チームなど）を表すJPAエンティティクラス。
 *
 * <p>このクラスは {@link Party} を拡張し、組織単位特有の属性（組織単位名、組織単位コード、
 * 組織単位種類、親組織単位、状態など）を保持します。
 *
 * <p>組織単位は階層構造を持っており、{@code largers}（親組織単位）と{@code smallers}（子組織単位）で
 * 親子関係を表現します。また、{@code org}フィールドを通じて所属する組織（{@link Org}）と関連付けられます。
 *
 * <p>C3修正により{@code @Setter}は削除され、{@code create()}ファクトリメソッドを使用してインスタンスを作成します。
 */
@Table(name = "OrgUnit", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrgUnit extends Party {

  /** 組織単位の正式名称。 */
  private OrgUnitName orgUnitName;

  /** 組織単位の有効期間（開始日から終了日までの範囲）。 */
  private ValidInterval validInterval;

  /** 組織単位コード（一意制約）。システム内での一意な識別に使用されます。 */
  @Column(name = "org_unit_code", unique = true, length = 30)
  private OrgUnitCode orgUnitCode;

  /** 組織単位の種類コード（DEPARTMENT, TEAM, SECTIONなど）。組織単位の性質を分類します。 */
  @Column(name = "org_unit_type_code", nullable = false, length = 20)
  private OrgUnitTypeCode orgUnitTypeCode;

  /** 親組織単位のID（自己参照）。nullの場合は最上位の組織単位です。 */
  @Column(name = "parent_org_unit_id")
  private SnowflakeId parentOrgUnitId;

  /** 組織単位の状態（ACTIVE, INACTIVEなど）。組織単位の稼働状態を示します。 */
  @Column(name = "status", nullable = false, length = 20)
  private OrgUnitStatus status;

  /** 所属する組織。OrgUnit は必ず1件の Org を持ちます。 */
  @ManyToOne(optional = false)
  @JoinColumn(name = "org_id", nullable = false)
  private Org org;

  /** 上位組織（親組織単位）。読み取り専用で、親組織単位へのナビゲーションに使用されます。 */
  @ManyToOne
  @JoinColumn(name = "parent_org_unit_id", insertable = false, updatable = false)
  private OrgUnit largers;

  /** 下位組織（子組織単位の一覧）。この組織単位に直属する子組織単位を保持します。 */
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
    return create(partyId, code, validFrom, orgUnitName, orgUnitCode, orgUnitTypeCode,
        parentOrgUnitId, status, org, null, null, null);
  }

  /**
   * 組織単位を新規作成します（オプションフィールドを含むオーバーロード）。
   *
   * <p>M4修正により、{@code validTo}、{@code govtAssignedId}、{@code searchName}を
   * {@code @Nullable}パラメータとして受け取るようになりました。
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
   * @param validTo 有効終了日（null可）
   * @param govtAssignedId 政府発行識別子（null可）
   * @param searchName 検索用名称（null可）
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
      Org org,
      @Nullable LocalDateTime validTo,
      @Nullable GovtAssignedId govtAssignedId,
      @Nullable SearchName searchName
  ) {
    OrgUnit orgUnit = new OrgUnit();
    orgUnit.initParty(partyId, PartyTypeCode.ORG_UNIT, code, validFrom, validTo, govtAssignedId, searchName);
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
