package undecided.association.relationshipMgmt.spi.party;

import com.github.mizo432.shared.common.primitive.Lists2;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティ（個人や法人など）を表す抽象エンティティクラス。
 *
 * <p>このクラスは、関係管理において対象となるパーティの共通属性を保持します。
 */
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Party {
  @Id
  @Column(name = "party_id")
  private SnowflakeId partyId;

  /** パーティー種類コード（MASTER, PERSON, ORG）。 */
  @Column(name = "party_type_code", nullable = false, length = 20)
  private PartyTypeCode partyTypeCode;

  /** コード（一意制約）。 */
  @Column(name = "code", unique = true, length = 50)
  private String code;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /** 政府発行の識別子（例：マイナンバー、法人番号など）。 */
  private GovtAssignedId govtAssignedId;

  /** 検索用名称（全角文字）。 */
  private SearchName searchName;

  private List<ContactUse> contactUses = Lists2.newArrayList();
}
