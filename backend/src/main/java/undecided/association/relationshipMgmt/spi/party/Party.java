package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティ（個人や法人など）を表す抽象エンティティクラス。
 *
 * <p>このクラスは、関係管理において対象となるパーティの共通属性を保持します。
 * 継承戦略は{@link InheritanceType#TABLE_PER_CLASS}であり、各サブクラスごとに独立したテーブルが作成されます。
 *
 * <p>サブクラス:
 * <ul>
 *   <li>{@link undecided.association.relationshipMgmt.spi.party.org.Org} - 組織（法人、団体など）</li>
 *   <li>{@link undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit} - 組織単位（部門、チームなど）</li>
 *   <li>{@link undecided.association.relationshipMgmt.spi.party.person.Person} - 個人</li>
 * </ul>
 *
 * <p>このクラスのフィールドは{@code protected}として宣言されており、サブクラスからのアクセスを許可しつつ、
 * 外部からの直接変更を防いでいます。C3修正により{@code @Setter}は削除され、ドメインの不変性が保護されています。
 */
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Party {

  /** パーティの一意な識別子（Snowflake ID）。 */
  @Id
  @Column(name = "party_id")
  protected SnowflakeId partyId;

  /** パーティの種類コード（MASTER, PERSON, ORG）。 */
  @Column(name = "party_type_code", nullable = false, length = 20)
  protected PartyTypeCode partyTypeCode;

  /** パーティのコード（一意制約）。システム内での一意な識別に使用されます。 */
  @Column(name = "code", unique = true, length = 50)
  protected String code;

  /** パーティの有効開始日。この日時からパーティが有効となります。 */
  @Column(name = "valid_from", nullable = false)
  protected LocalDateTime validFrom;

  /** パーティの有効終了日。この日時までパーティが有効となります。nullの場合は現在も有効です。 */
  @Column(name = "valid_to")
  protected LocalDateTime validTo;

  /** 政府発行の識別子（例：マイナンバー、法人番号など）。外部システムとの連携に使用されます。 */
  protected GovtAssignedId govtAssignedId;

  /** 検索用名称（全角文字）。パーティの検索や表示に使用されます。 */
  protected SearchName searchName;

  /**
   * パーティの共通フィールドを初期化します。
   *
   * <p>M4/M5修正により、{@link undecided.association.relationshipMgmt.spi.party.org.Org#create}と
   * {@link undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit#create}の重複コードを
   * この基底クラスに共通化しました。サブクラスのファクトリメソッドから呼び出されます。
   *
   * @param partyId パーティID
   * @param partyTypeCode パーティ種類コード
   * @param code コード
   * @param validFrom 有効開始日
   * @param validTo 有効終了日（null可）
   * @param govtAssignedId 政府発行識別子（null可）
   * @param searchName 検索用名称（null可）
   */
  protected void initParty(
      SnowflakeId partyId,
      PartyTypeCode partyTypeCode,
      String code,
      LocalDateTime validFrom,
      @Nullable LocalDateTime validTo,
      @Nullable GovtAssignedId govtAssignedId,
      @Nullable SearchName searchName
  ) {
    this.partyId = partyId;
    this.partyTypeCode = partyTypeCode;
    this.code = code;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.govtAssignedId = govtAssignedId;
    this.searchName = searchName;
  }
}
