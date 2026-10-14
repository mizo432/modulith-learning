package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティ間の関係を表すJPAエンティティです。
 *
 * <p>このエンティティは、パーティ（人物や組織など）之间存在する関連付けを管理し、 関係の種類や関連付け日時、関連付け解除日時を保持します。
 */
@Table(name = "party_relationships", schema = "party_relationship_mgmt")
@Entity
@Getter
@EqualsAndHashCode(of = "relationshipId")
public class PartyRelationship {

  @Id private SnowflakeId relationshipId;

  private SnowflakeId partyId;

  private SnowflakeId relatedPartyId;

  private RelationshipType relationshipType;

  private WhenAassociated whenAssociated;

  private WhenDisassociated whenDisassociated;

  /**
   * JPAプロキシ生成用のデフォルトコンストラクタです。
   *
   * <p>すべてのフィールドにデフォルト値を設定します。
   */
  protected PartyRelationship() {
    this.relationshipId = SnowflakeId.empty();
    this.partyId = SnowflakeId.empty();
    this.relatedPartyId = SnowflakeId.empty();
    this.relationshipType = RelationshipType.UNKNOWN;
    this.whenAssociated = WhenAassociated.empty();
    this.whenDisassociated = WhenDisassociated.empty();
  }

  /**
   * 指定されたパラメータから {@code PartyRelationship} インスタンスを作成します。
   *
   * @param relationshipId 関係の一意の識別子。{@code null} は許容されません。
   * @param partyId 一方のパーティの識別子。{@code null} は許容されません。
   * @param relatedPartyId 他方のパーティの識別子。{@code null} は許容されません。
   * @param relationshipType 関係の種類。{@code null} は許容されません。
   * @param whenAssociated 関連付け日時。{@code null} の可能性があります。
   * @param whenDisassociated 関連付け解除日時。{@code null} の可能性があります。
   * @throws IllegalArgumentException 必須パラメータが {@code null} の場合
   */
  public PartyRelationship(
      SnowflakeId relationshipId,
      SnowflakeId partyId,
      SnowflakeId relatedPartyId,
      RelationshipType relationshipType,
      WhenAassociated whenAssociated,
      WhenDisassociated whenDisassociated) {
    Objects.requireNonNull(relationshipId, "relationshipId must not be null");
    Objects.requireNonNull(partyId, "partyId must not be null");
    Objects.requireNonNull(relatedPartyId, "relatedPartyId must not be null");
    Objects.requireNonNull(relationshipType, "relationshipType must not be null");

    this.relationshipId = relationshipId;
    this.partyId = partyId;
    this.relatedPartyId = relatedPartyId;
    this.relationshipType = relationshipType;
    this.whenAssociated = whenAssociated;
    this.whenDisassociated = whenDisassociated;
  }
}
