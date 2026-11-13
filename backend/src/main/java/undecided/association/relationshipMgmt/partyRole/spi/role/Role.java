package undecided.association.relationshipMgmt.partyRole.spi.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティーロール定義を表すエンティティクラス。
 *
 * <p>このクラスは、パーティーに割り当てられるロールの定義を管理します。
 */
@Entity
@Table(name = "party_role", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
  @Id
  @Column(name = "role_id")
  protected SnowflakeId roleId;

  /** ロールコード（一意制約）。 */
  @Column(name = "role_code", nullable = false, unique = true, length = 30)
  protected String roleCode;

  /** ロール名称。 */
  @Column(name = "role_name", nullable = false, length = 50)
  protected String roleName;

  /** ロール種類コード。 */
  @Column(name = "role_type_code", nullable = false, length = 20)
  protected String roleTypeCode;

  /** 説明。 */
  @Column(name = "description", length = 200)
  protected String description;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  protected LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  protected LocalDateTime validTo;

  @OneToMany
  @JoinColumn(name = "role_id")
  protected List<RoleAssignmentForOrg> orgsAssigned = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "role_id")
  protected List<RoleAssignmentForEmp> empsAssigned = new ArrayList<>();

  /**
   * ロールを新規作成します。
   *
   * @param roleId ロールID
   * @param roleCode ロールコード
   * @param roleName ロール名称
   * @param roleTypeCode ロール種類コード
   * @param description 説明
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @return 新規作成されたRoleインスタンス
   */
  public static Role create(
      SnowflakeId roleId,
      String roleCode,
      String roleName,
      String roleTypeCode,
      String description,
      LocalDateTime validFrom,
      LocalDateTime validTo
  ) {
    Role role = new Role();
    role.roleId = roleId;
    role.roleCode = roleCode;
    role.roleName = roleName;
    role.roleTypeCode = roleTypeCode;
    role.description = description;
    role.validFrom = validFrom;
    role.validTo = validTo;
    return role;
  }
}
