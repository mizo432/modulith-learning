package undecided.association.relationshipMgmt.partyRole.spi.role;

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
 * ロール有効期間を表すエンティティクラス。
 *
 * <p>このクラスは、パーティーに割り当てられたロールの有効期間を管理します。
 */
@Entity
@Table(name = "role_validity_period", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleValidityPeriod {
  @Id
  @Column(name = "validity_id")
  private SnowflakeId validityId;

  /** パーティーID。 */
  @Column(name = "party_id", nullable = false)
  private SnowflakeId partyId;

  /** ロールID。 */
  @Column(name = "role_id", nullable = false)
  private SnowflakeId roleId;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /** 状態（有効/無効）。 */
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /**
   * ロール有効期間を作成します。
   *
   * @param validityId 有効期間ID
   * @param partyId パーティーID
   * @param roleId ロールID
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @param status 状態
   */
  public RoleValidityPeriod(
      SnowflakeId validityId,
      SnowflakeId partyId,
      SnowflakeId roleId,
      LocalDateTime validFrom,
      LocalDateTime validTo,
      String status) {
    this.validityId = validityId;
    this.partyId = partyId;
    this.roleId = roleId;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.status = status;
  }
}
