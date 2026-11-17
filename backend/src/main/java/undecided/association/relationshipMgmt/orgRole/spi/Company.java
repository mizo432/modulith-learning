package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 会社基本情報を表すエンティティクラス。
 *
 * <p>このクラスは、会社の基本情報（会社名、住所など）を管理します。
 */
@Entity
@Table(name = "company", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
  @Id
  @Column(name = "company_id")
  private SnowflakeId companyId;

  /** 会社名。 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** 住所。 */
  @Column(name = "address", length = 200)
  private String address;

  /** 電話番号。 */
  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  /** 状態（有効/無効）。 */
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 会社を作成します。
   *
   * @param companyId 会社ID
   * @param name 会社名
   * @param address 住所
   * @param phoneNumber 電話番号
   * @param status 状態
   * @param validFrom 有効開始日
   */
  public Company(
      SnowflakeId companyId,
      String name,
      String address,
      String phoneNumber,
      String status,
      LocalDateTime validFrom) {
    this.companyId = companyId;
    this.name = name;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.status = status;
    this.validFrom = validFrom;
  }
}
