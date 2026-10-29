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
 * 人ロール情報を表すエンティティクラス。
 *
 * <p>このクラスは、パーティーに割り当てられる人ロールの情報を管理します。
 */
@Entity
@Table(name = "person_role", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonRole {
  @Id
  @Column(name = "person_role_id")
  private SnowflakeId personRoleId;

  /** パーティーID。 */
  @Column(name = "party_id", nullable = false)
  private SnowflakeId partyId;

  /** ロールコード。 */
  @Column(name = "role_code", nullable = false, length = 30)
  private String roleCode;

  /** ロール名称。 */
  @Column(name = "role_name", nullable = false, length = 50)
  private String roleName;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  /**
   * 人ロールを作成します。
   *
   * @param personRoleId 人ロールID
   * @param partyId パーティーID
   * @param roleCode ロールコード
   * @param roleName ロール名称
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   */
  public PersonRole(
      SnowflakeId personRoleId,
      SnowflakeId partyId,
      String roleCode,
      String roleName,
      LocalDateTime validFrom,
      LocalDateTime validTo) {
    this.personRoleId = personRoleId;
    this.partyId = partyId;
    this.roleCode = roleCode;
    this.roleName = roleName;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
