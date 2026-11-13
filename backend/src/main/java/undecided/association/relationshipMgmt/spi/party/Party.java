package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Party {
  @Id
  @Column(name = "party_id")
  protected SnowflakeId partyId;

  /** パーティー種類コード（MASTER, PERSON, ORG）。 */
  @Column(name = "party_type_code", nullable = false, length = 20)
  protected PartyTypeCode partyTypeCode;

  /** コード（一意制約）。 */
  @Column(name = "code", unique = true, length = 50)
  protected String code;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  protected LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  protected LocalDateTime validTo;

  /** 政府発行の識別子（例：マイナンバー、法人番号など）。 */
  protected GovtAssignedId govtAssignedId;

  /** 検索用名称（全角文字）。 */
  protected SearchName searchName;
}
