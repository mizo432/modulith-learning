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
 * 店舗基本情報を表すエンティティクラス。
 *
 * <p>このクラスは、店舗の基本情報（店舗名、住所など）を管理します。
 */
@Entity
@Table(name = "store", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
  @Id
  @Column(name = "store_id")
  private SnowflakeId storeId;

  /** 店舗名。 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** 所属会社ID。 */
  @Column(name = "company_id", nullable = false)
  private SnowflakeId companyId;

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
   * 店舗を作成します。
   *
   * @param storeId 店舗ID
   * @param name 店舗名
   * @param companyId 所属会社ID
   * @param address 住所
   * @param phoneNumber 電話番号
   * @param status 状態
   * @param validFrom 有効開始日
   */
  public Store(
      SnowflakeId storeId,
      String name,
      SnowflakeId companyId,
      String address,
      String phoneNumber,
      String status,
      LocalDateTime validFrom) {
    this.storeId = storeId;
    this.name = name;
    this.companyId = companyId;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.status = status;
    this.validFrom = validFrom;
  }
}
