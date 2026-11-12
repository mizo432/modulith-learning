package undecided.association.relationshipMgmt.spi.party.org.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.association.relationshipMgmt.spi.party.org.OrgCode;
import undecided.association.relationshipMgmt.spi.party.org.OrgName;
import undecided.association.relationshipMgmt.spi.party.org.OrgStatus;
import undecided.association.relationshipMgmt.spi.party.org.OrgTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織新規作成のためのDTOクラスです。
 *
 * <p>このクラスは、組織（Org）を新規作成するために必要な情報を格納します。
 * 組織名、組織コード、組織種類コード、親組織ID、状態を含みます。
 */
@Getter
@RequiredArgsConstructor
public class OrgDto {

  /**
   * 組織の正式名称。
   *
   * <p>このフィールドは組織の表示名や公式名称として使用されます。
   */
  private final OrgName orgName;

  /**
   * 組織コード（一意制約）。
   *
   * <p>このフィールドは組織を一意に識別するためのコードです。
   */
  private final OrgCode orgCode;

  /**
   * 組織種類コード（COMPANY, STORE, DEPARTMENTなど）。
   *
   * <p>このフィールドは組織の種類を識別するために使用されます。
   */
  private final OrgTypeCode orgTypeCode;

  /**
   * 親組織ID（自己参照）。
   *
   * <p>このフィールドは親組織を参照するために使用されます。
   * 親組織がない場合は {@code null} を許容します。
   */
  private final SnowflakeId parentOrgId;

  /**
   * 組織の状態（ACTIVE, INACTIVEなど）。
   *
   * <p>このフィールドは組織の有効・無効状態を管理するために使用されます。
   */
  private final OrgStatus status;
}
