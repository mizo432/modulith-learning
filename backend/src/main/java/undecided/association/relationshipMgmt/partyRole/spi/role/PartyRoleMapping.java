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
 * パーティーとロールの対応関係を表すエンティティクラス。
 *
 * <p>このクラスは、パーティーに割り当てられるロールの対応関係を管理します。
 */
@Entity
@Table(name = "party_role_mapping", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyRoleMapping {
  @Id
  @Column(name = "mapping_id")
  private SnowflakeId mappingId;

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

  /**
   * パーティーとロールの対応関係を作成します。
   *
   * @param mappingId マッピングID
   * @param partyId パーティーID
   * @param roleId ロールID
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   */
  public PartyRoleMapping(
      SnowflakeId mappingId,
      SnowflakeId partyId,
      SnowflakeId roleId,
      LocalDateTime validFrom,
      LocalDateTime validTo) {
    this.mappingId = mappingId;
    this.partyId = partyId;
    this.roleId = roleId;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
