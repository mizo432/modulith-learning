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
 * <p>M3修正により、重複していた個別の住所フィールド（prefName, prefKanaなど17個）が削除され、
 * {@link Address}値オブジェクトのみを使用するようにしました。これによりデータの二重管理による
 * 整合性問題を解消しました。
 *
 * <p>Mi2修正により、非推奨の{@link Contact}型から{@link EmailAddress}型に移行しました。
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

  /** メールアドレス。既存スキーマのcontact列を使用します。 */
  @Column(name = "contact", length = 200)
  private EmailAddress emailAddress = EmailAddress.empty();

  /** 電話番号。 */
  @Column(name = "phone_no", length = 20)
  private PhoneNo phoneNo = PhoneNo.empty();

  /** 住所の値オブジェクト。完全な住所情報を保持します。 */
  @Column(name = "address", columnDefinition = "text")
  private Address address = Address.empty();

  /** 連絡先の種類（EMAIL, PHONE, ADDRESSなど）。 */
  @Enumerated(EnumType.STRING)
  private ContactUseType type = ContactUseType.UNKNOWN;

  /** 連絡先が使用された時刻。 */
  private WhenOccured whenOccured = WhenOccured.empty();

  /**
   * 連絡先の使用履歴を新規作成します。
   *
   * <p>Mi2修正により、{@code contact}パラメータが{@code emailAddress}に置き換えられました。
   * 非推奨の{@link Contact}型から{@link EmailAddress}型に移行しました。
   *
   * @param contactUseId 連絡先使用ID
   * @param partyId パーティID
   * @param emailAddress メールアドレス
   * @param phoneNo 電話番号
   * @param address 住所
   * @param type 種類
   * @param whenOccured 発生時刻
   * @return 新規作成されたContactUseインスタンス
   */
  public static ContactUse create(
      SnowflakeId contactUseId,
      SnowflakeId partyId,
      EmailAddress emailAddress,
      PhoneNo phoneNo,
      Address address,
      ContactUseType type,
      @Nullable WhenOccured whenOccured
  ) {
    ContactUse contactUse = new ContactUse();
    contactUse.contactUseId = contactUseId;
    contactUse.partyId = partyId;
    contactUse.emailAddress = emailAddress;
    contactUse.phoneNo = phoneNo;
    contactUse.address = address;
    contactUse.type = type;
    contactUse.whenOccured = whenOccured != null ? whenOccured : WhenOccured.empty();
    return contactUse;
  }
}
