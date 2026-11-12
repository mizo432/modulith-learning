package undecided.association.relationshipMgmt.spi.party;

import com.github.mizo432.shared.common.primitive.Lists2;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.PartyRelationship;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティ（個人や法人など）を表す抽象エンティティクラス。
 *
 * <p>このクラスは、関係管理において対象となるパーティの共通属性を保持します。
 */
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Party {
  @Id
  @Column(name = "party_id")
  private SnowflakeId partyId;

  /** パーティー種類コード（MASTER, PERSON, ORG）。 */
  @Column(name = "party_type_code", nullable = false, length = 20)
  private PartyTypeCode partyTypeCode;

  /** 名称。 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** コード（一意制約）。 */
  @Column(name = "code", unique = true, length = 50)
  private String code;

  /** 政府発行の識別子（例：マイナンバー、法人番号など）。 */
  private GovtAssignedId govtAssignedId;

  /** 検索用名称（全角文字）。 */
  private SearchName searchName;

  private List<PartyRelationship> partyRelationships = Lists2.newArrayList();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Party party = (Party) o;
    return Objects.equals(partyId, party.partyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId);
  }
}
