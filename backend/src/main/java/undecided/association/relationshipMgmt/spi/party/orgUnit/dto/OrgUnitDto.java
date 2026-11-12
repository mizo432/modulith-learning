package undecided.association.relationshipMgmt.spi.party.orgUnit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitCode;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitName;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitStatus;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織単位新規作成のためのDTOクラスです。
 *
 * <p>このクラスは、組織単位（OrgUnit）を新規作成するために必要な情報を格納します。
 * 組織単位名、組織単位コード、組織単位種類コード、親組織単位ID、状態、所属組織IDを含みます。
 */
@Getter
@RequiredArgsConstructor
@ToString
public class OrgUnitDto {

  /**
   * 組織単位の名称。
   *
   * <p>このフィールドは組織単位の表示名や公式名称として使用されます。
   */
  private final OrgUnitName orgUnitName;

  /**
   * 組織単位コード（一意制約）。
   *
   * <p>このフィールドは組織単位を一意に識別するためのコードです。
   */
  private final OrgUnitCode orgUnitCode;

  /**
   * 組織単位種類コード（DEPARTMENT, TEAM, SECTIONなど）。
   *
   * <p>このフィールドは組織単位の種類を識別するために使用されます。
   */
  private final OrgUnitTypeCode orgUnitTypeCode;

  /**
   * 親組織単位ID（自己参照）。
   *
   * <p>このフィールドは親組織単位を参照するために使用されます。
   * 親組織単位がない場合は {@code null} を許容します。
   */
  private final SnowflakeId parentOrgUnitId;

  /**
   * 組織単位の状態（ACTIVE, INACTIVEなど）。
   *
   * <p>このフィールドは組織単位の有効・無効状態を管理するために使用されます。
   */
  private final OrgUnitStatus status;

  /**
   * 所属する組織ID。
   *
   * <p>このフィールドはOrgUnitが属するOrgを参照するために使用されます。
   * OrgUnitは必ず1件のOrgを持つため、このフィールドは必須です。
   */
  private final SnowflakeId orgId;
}
