package undecided.association.relationshipMgmt.personRole.spi;

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
 * 所属対応表を表すエンティティクラス。
 *
 * <p>このクラスは、従業員と組織の所属関係を管理します。
 */
@Entity
@Table(name = "affiliation", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Affiliation {
  @Id
  @Column(name = "affiliation_id")
  private SnowflakeId affiliationId;

  /** 従業員ID。 */
  @Column(name = "employee_id", nullable = false)
  private SnowflakeId employeeId;

  /** 組織ID。 */
  @Column(name = "organization_id", nullable = false)
  private SnowflakeId organizationId;

  /** 役職。 */
  @Column(name = "position", length = 50)
  private String position;

  /** 主所属フラグ。 */
  @Column(name = "is_primary", nullable = false)
  private boolean isPrimary;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 所属関係を作成します。
   *
   * @param affiliationId 所属ID
   * @param employeeId 従業員ID
   * @param organizationId 組織ID
   * @param position 役職
   * @param isPrimary 主所属フラグ
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   */
  public Affiliation(
      SnowflakeId affiliationId,
      SnowflakeId employeeId,
      SnowflakeId organizationId,
      String position,
      boolean isPrimary,
      LocalDateTime validFrom,
      LocalDateTime validTo) {
    this.affiliationId = affiliationId;
    this.employeeId = employeeId;
    this.organizationId = organizationId;
    this.position = position;
    this.isPrimary = isPrimary;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
