package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import com.github.mizo432.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先の使用履歴を表すエンティティです。
 *
 * <p>パーティと連絡先アドレスの関連付けを管理します。連絡先の種類（メール、電話、住所など）ごとに
 * 使用履歴を記録し、いつどの連絡先が使用されたかを追跡できます。
 *
 * <p>注意: このエンティティは{@link Address}値オブジェクトと、個別の住所フィールド（{@code prefName}、
 * {@code prefKana}など）の両方を保持しています。これはデータの重複であり、{@code create()}メソッドで
 * Addressから個別フィールドに値をコピーしています。将来的にはAddress値オブジェクトのみを使用するように
 * リファクタリングすることを検討してください。
 *
 * <p>C3修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。
 */
@Table(schema = SchemaRelationshipMgmt.NAME, name = "ContactUses")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactUse {

  /** 連絡先使用履歴の一意な識別子。 */
  @Id
  private SnowflakeId contactUseId = SnowflakeId.EMPTY;

  /** 関連するパーティのID。 */
  @Column(name = "party_id")
  private SnowflakeId partyId = SnowflakeId.EMPTY;

  /** 連絡先アドレス（非推奨、{@link EmailAddress}を使用してください）。 */
  private Contact contact = Contact.empty();

  /** 電話番号。 */
  private PhoneNo phoneNo = PhoneNo.empty();

  /** 住所の値オブジェクト。完全な住所情報を保持します。 */
  @Column(name = "address", length = 200)
  private Address address = Address.empty();

  /** 都道府県名。Address値オブジェクトからコピーされます。 */
  @Column(name = "pref_name", length = 10)
  private String prefName;

  /** 都道府県名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "pref_kana", length = 15)
  private String prefKana;

  /** 郵便番号。Address値オブジェクトからコピーされます。 */
  @Column(name = "postal_code", length = 8)
  private String postalCode;

  /** 国名。Address値オブジェクトからコピーされます。 */
  @Column(name = "country_name", length = 50)
  private String countryName;

  /** 国名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "country_kana", length = 50)
  private String countryKana;

  /** 市区町村名。Address値オブジェクトからコピーされます。 */
  @Column(name = "city_name", length = 20)
  private String cityName;

  /** 市区町村名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "city_kana", length = 20)
  private String cityKana;

  /** 区名。Address値オブジェクトからコピーされます。 */
  @Column(name = "ward_name", length = 20)
  private String wardName;

  /** 区名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "ward_kana", length = 20)
  private String wardKana;

  /** 大字・町名。Address値オブジェクトからコピーされます。 */
  @Column(name = "oaza_cho_name", length = 20)
  private String oazaChoName;

  /** 大字・町名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "oaza_cho_kana", length = 20)
  private String oazaChoKana;

  /** 丁目名。Address値オブジェクトからコピーされます。 */
  @Column(name = "chome_name", length = 20)
  private String chomeName;

  /** 丁目名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "chome_kana", length = 20)
  private String chomeKana;

  /** 小字名。Address値オブジェクトからコピーされます。 */
  @Column(name = "koaza_name", length = 20)
  private String koazaName;

  /** 小字名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "koaza_kana", length = 20)
  private String koazaKana;

  /** 建物名。Address値オブジェクトからコピーされます。 */
  @Column(name = "building_name", length = 50)
  private String buildingName;

  /** 建物名（かな）。Address値オブジェクトからコピーされます。 */
  @Column(name = "building_kana", length = 50)
  private String buildingKana;

  /** 部屋番号。Address値オブジェクトからコピーされます。 */
  @Column(name = "room_no", length = 20)
  private String roomNo;

  /** 連絡先の種類（EMAIL, PHONE, ADDRESSなど）。 */
  @Enumerated(EnumType.STRING)
  private ContactUseType type = ContactUseType.UNKNOWN;

  /** 連絡先が使用された時刻。 */
  private WhenOccured whenOccured = WhenOccured.empty();

  /**
   * 連絡先の使用履歴を新規作成します。
   *
   * @param contactUseId 連絡先使用ID
   * @param partyId パーティID
   * @param contact 連絡先
   * @param phoneNo 電話番号
   * @param address 住所
   * @param type 種類
   * @param whenOccured 発生時刻
   * @return 新規作成されたContactUseインスタンス
   */
  public static ContactUse create(
      SnowflakeId contactUseId,
      SnowflakeId partyId,
      Contact contact,
      PhoneNo phoneNo,
      Address address,
      ContactUseType type,
      @Nullable WhenOccured whenOccured
  ) {
    ContactUse contactUse = new ContactUse();
    contactUse.contactUseId = contactUseId;
    contactUse.partyId = partyId;
    contactUse.contact = contact;
    contactUse.phoneNo = phoneNo;
    contactUse.address = address;
    contactUse.prefName = address.getPrefName();
    contactUse.prefKana = address.getPrefKana();
    contactUse.postalCode = address.getPostalCode();
    contactUse.countryName = address.getCountryName();
    contactUse.countryKana = address.getCountryKana();
    contactUse.cityName = address.getCityName();
    contactUse.cityKana = address.getCityKana();
    contactUse.wardName = address.getWardName();
    contactUse.wardKana = address.getWardKana();
    contactUse.oazaChoName = address.getOazaChoName();
    contactUse.oazaChoKana = address.getOazaChoKana();
    contactUse.chomeName = address.getChomeName();
    contactUse.chomeKana = address.getChomeKana();
    contactUse.koazaName = address.getKoazaName();
    contactUse.koazaKana = address.getKoazaKana();
    contactUse.buildingName = address.getBuildingName();
    contactUse.buildingKana = address.getBuildingKana();
    contactUse.roomNo = address.getRoomNo();
    contactUse.type = type;
    contactUse.whenOccured = whenOccured != null ? whenOccured : WhenOccured.empty();
    return contactUse;
  }
}
