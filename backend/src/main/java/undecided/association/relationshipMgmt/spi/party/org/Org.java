package undecided.association.relationshipMgmt.spi.party.org;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.ValidInterval;
import java.time.LocalDateTime;

/**
 * 組織（法人、団体など）を表すJPAエンティティクラス。
 *
 * <p>このクラスは {@link Party} を拡張し、組織特有の属性（組織名、組織コード、組織種類、
 * 親組織、状態など）を保持します。
 */
@Table(name = "Org", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Org extends Party {

  /** 組織の正式名称。 */
  private OrgName orgName;

  /** 組織の有効期間（開始日から終了日までの範囲）。 */
  private ValidInterval validInterval;

  /** 組織コード（一意制約）。 */
  @Column(name = "org_code", unique = true, length = 30)
  private OrgCode orgCode;

  /** 組織種類コード（COMPANY, STORE, DEPARTMENTなど）。 */
  @Column(name = "org_type_code", nullable = false, length = 20)
  private OrgTypeCode orgTypeCode;

  /** 親組織ID（自己参照）。 */
  @Column(name = "parent_org_id")
  private SnowflakeId parentOrgId;

  /** 組織の状態（ACTIVE, INACTIVEなど）。 */
  @Column(name = "status", nullable = false, length = 20)
  private OrgStatus status;

  /**
   * 組織を新規作成します。
   *
   * @param partyId パーティID
   * @param code コード
   * @param validFrom 有効開始日
   * @param orgName 組織名
   * @param orgCode 組織コード
   * @param orgTypeCode 組織種類コード
   * @param parentOrgId 親組織ID（null可）
   * @param status 状態
   * @return 新規作成されたOrgインスタンス
   */
  public static Org create(
      SnowflakeId partyId,
      String code,
      LocalDateTime validFrom,
      OrgName orgName,
      OrgCode orgCode,
      OrgTypeCode orgTypeCode,
      @Nullable SnowflakeId parentOrgId,
      OrgStatus status
  ) {
    Org org = new Org();
    org.partyId = partyId;
    org.partyTypeCode = PartyTypeCode.ORG;
    org.code = code;
    org.validFrom = validFrom;
    org.validTo = null;
    org.govtAssignedId = null;
    org.searchName = null;
    org.orgName = orgName;
    org.validInterval = ValidInterval.empty();
    org.orgCode = orgCode;
    org.orgTypeCode = orgTypeCode;
    org.parentOrgId = parentOrgId;
    org.status = status;
    return org;
  }
}
