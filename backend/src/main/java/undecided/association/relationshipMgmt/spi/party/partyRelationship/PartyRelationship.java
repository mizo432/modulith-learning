package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.supporting.snowflake.SnowflakeId;
/**
 * パーティ間の関係を表すJPAエンティティです。
 *
 * <p>このエンティティは、パーティ（人物や組織など）之间存在する関連付けを管理し、関係の種類や関連付け日時、関連付け解除日時を保持します。
 */
@Entity
@Table(name = "party_relationships", schema = "party_relationship_mgmt")
@Audited
@Getter
@EqualsAndHashCode(of = "relationshipId")
@ToString
public class PartyRelationship {

  @Id private SnowflakeId relationshipId;

  @ManyToOne
  @JoinColumn(name = "party_party_id")
  private Party party;

  @ManyToOne
  @JoinColumn(name = "related_party_party_id")
  private Party relatedParty;

  private RelationshipType relationshipType;

  private WhenAssociated whenAssociated;

  private WhenDisassociated whenDisassociated;

  /**
   * JPAプロキシ生成用のデフォルトコンストラクタです。
   *
   * <p>すべてのフィールドにデフォルト値を設定します。
   */
  protected PartyRelationship() {
    this.relationshipId = SnowflakeId.empty();
    this.relationshipType = RelationshipType.UNKNOWN;
    this.whenAssociated = WhenAssociated.empty();
    this.whenDisassociated = WhenDisassociated.empty();
  }

  /**
   * 指定されたパラメータから {@code PartyRelationship} インスタンスを作成します。
   *
   * @param relationshipId 関係の一意の識別子。{@code null} は許容されません。
   * @param party 一方のパーティ。{@code null} は許容されません。
   * @param relatedParty 他方のパーティ。{@code null} は許容されません。
   * @param relationshipType 関係の種類。{@code null} は許容されません。
   * @param whenAssociated 関連付け日時。{@code null} の可能性があります。
   * @param whenDisassociated 関連付け解除日時。{@code null} の可能性があります。
   */
  public PartyRelationship(
      SnowflakeId relationshipId,
      Party party,
      Party relatedParty,
      RelationshipType relationshipType,
      WhenAssociated whenAssociated,
      WhenDisassociated whenDisassociated) {
    Objects.requireNonNull(relationshipId, "relationshipId must not be null");
    Objects.requireNonNull(party, "party must not be null");
    Objects.requireNonNull(relatedParty, "relatedParty must not be null");
    Objects.requireNonNull(relationshipType, "relationshipType must not be null");

    this.relationshipId = relationshipId;
    this.party = party;
    this.relatedParty = relatedParty;
    this.relationshipType = relationshipType;
    this.whenAssociated = whenAssociated;
    this.whenDisassociated = whenDisassociated;
  }

  /**
   * 一方のパーティの識別子を返します。
   *
   * @return パーティの識別子。パーティが設定されていない場合は {@code null} を返します。
   */
  public SnowflakeId getPartyId() {
    return party != null ? party.getPartyId() : null;
  }

  /**
   * 他方のパーティの識別子を返します。
   *
   * @return 関連パーティの識別子。関連パーティが設定されていない場合は {@code null} を返します。
   */
  public SnowflakeId getRelatedPartyId() {
    return relatedParty != null ? relatedParty.getPartyId() : null;
  }

  public void setRelatedParty(Party relatedParty) {
    this.relatedParty = relatedParty;
  }

  public void setParty(Party party) {
    this.party = party;
  }
}
