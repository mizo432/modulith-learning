package undecided.shared.sample.jpa.entity.embeddable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先（Contact）を表すエンティティで、@Embeddedのサンプルです。
 *
 * <p>{@code @Embedded}と{@code @AttributeOverrides}を使用して、 埋め込み可能クラスのカラム名をカスタマイズするパターンを示します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code @AttributeOverrides}でカラム名をカスタマイズ
 *   <li>値オブジェクトの内部構造に依存せずに、カラム名を制御できる
 * </ul>
 */
@Entity
@Table(name = "sample_contacts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleContact {

  @Id private SnowflakeId contactId;

  /** 名前 */
  @Column(nullable = false, length = 100)
  private String name;

  /** 住所情報です。 {@code @Embedded}により、SampleAddressのフィールドがこのテーブルにカラムとして展開されます。 */
  @Embedded private SampleAddress address;

  /** 電話番号です。 {@code @AttributeOverrides}でカラム名をカスタマイズしています。 */
  @Embedded
  @AttributeOverrides(
      @AttributeOverride(name = "value", column = @Column(name = "phone_number", length = 20)))
  private SamplePhoneNumber phoneNumber;

  /** メールアドレス */
  @Column(length = 100)
  private String email;

  public SampleContact(
      SnowflakeId contactId,
      String name,
      SampleAddress address,
      SamplePhoneNumber phoneNumber,
      String email) {
    this.contactId = contactId;
    this.name = name;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }
}
