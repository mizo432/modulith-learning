package undecided.association.relationshipMgmt.spi.party.org;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.ValidInterval;

/**
 * 組織（法人、団体など）を表すJPAエンティティクラス。
 *
 * <p>このクラスは {@link Party} を拡張し、組織特有の属性（組織名、組織コード、組織種類、
 * 親組織、状態など）を保持します。
 */
@Table(name = "Org", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
   * 空のOrgインスタンスを作成します。
   *
   * <p>このメソッドは、protectedコンストラクタにアクセスできない外部クラスから
   * Orgインスタンスを作成するために使用されます。
   *
   * @return 空のOrgインスタンス
   */
  public static Org createNew() {
    return new Org();
  }
}
