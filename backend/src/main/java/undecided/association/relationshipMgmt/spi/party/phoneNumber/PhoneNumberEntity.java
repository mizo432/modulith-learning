package undecided.association.relationshipMgmt.spi.party.phoneNumber;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 電話番号情報を表すエンティティクラス。
 *
 * <p>このクラスはパーティ（PersonやOrganizationなど）が持つ電話番号の情報を管理します。
 *
 * <p>電話番号と種別（携帯電話、固定電話、FAXなど）の情報を格納し、連絡先として使用されます。
 *
 * <p>用途:
 *
 * <p>電話連絡やFAX送信において、対象となる電話番号を識別するために使用されます。
 */
@Table(schema = SchemaRelationshipMgmt.NAME, name = "phone_numbers")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberEntity {
  /**
   * 電話番号を一意に識別するためのID。
   *
   * <p>このフィールドは電話番号を一意に識別します。
   *
   * <p>用途: 電話番号情報の検索や更新で使用されます。
   */
  @Id private SnowflakeId phoneNumberId = SnowflakeId.EMPTY;

  /**
   * 電話番号。
   *
   * <p>このフィールドは電話番号を格納します。
   *
   * <p>用途: 電話連絡やFAX送信において対象番号を特定するために使用されます。
   */
  private PhoneNumber number;

  /**
   * 電話番号の種別（携帯電話、固定電話、FAXなど）。
   *
   * <p>このフィールドは電話番号の種別を示します。
   *
   * <p>用途: 連絡手段に応じた処理を行うために使用されます。
   */
  @Enumerated(EnumType.STRING)
  private PhoneNumberType type = PhoneNumberType.UNKNOWN;
}
