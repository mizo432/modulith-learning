package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import com.github.mizo432.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先の使用履歴を表すエンティティです。
 *
 * <p>パーティと連絡先アドレスの関連付けを管理します。
 */
@Table(schema = SchemaRelationshipMgmt.NAME, name = "ContactUses")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUse {
  @Id private SnowflakeId contactUseId = SnowflakeId.EMPTY;
  @Column(name = "party_id")
  private SnowflakeId partyId = SnowflakeId.EMPTY;
  private Contact contact = Contact.empty();
  private PhoneNo phoneNo = PhoneNo.empty();
  @Column(name = "address", length = 200)
  private Address address = Address.empty();
  @Column(name = "pref_name", length = 10)
  private String prefName;
  @Column(name = "pref_kana", length = 15)
  private String prefKana;
  @Column(name = "postal_code", length = 8)
  private String postalCode;
  @Column(name = "country_name", length = 50)
  private String countryName;
  @Column(name = "country_kana", length = 50)
  private String countryKana;
  @Column(name = "city_name", length = 20)
  private String cityName;
  @Column(name = "city_kana", length = 20)
  private String cityKana;
  @Column(name = "ward_name", length = 20)
  private String wardName;
  @Column(name = "ward_kana", length = 20)
  private String wardKana;
  @Column(name = "oaza_cho_name", length = 20)
  private String oazaChoName;
  @Column(name = "oaza_cho_kana", length = 20)
  private String oazaChoKana;
  @Column(name = "chome_name", length = 20)
  private String chomeName;
  @Column(name = "chome_kana", length = 20)
  private String chomeKana;
  @Column(name = "koaza_name", length = 20)
  private String koazaName;
  @Column(name = "koaza_kana", length = 20)
  private String koazaKana;
  @Column(name = "building_name", length = 50)
  private String buildingName;
  @Column(name = "building_kana", length = 50)
  private String buildingKana;
  @Column(name = "room_no", length = 20)
  private String roomNo;
  @Enumerated(EnumType.STRING)
  private ContactUseType type = ContactUseType.UNKNOWN;
  private WhenOccured whenOccured = WhenOccured.empty();
}
