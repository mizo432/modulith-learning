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
 * <p>このクラスは、パーティーに割り当てられるロールの定義を管理します。ロールは組織や個人に割り当てられ、
 * 権限や責任を定義するための単位として使用されます。
 *
 * <p>C6修正により{@code @Setter}は削除され、{@code create()}ファクトリメソッドを使用してインスタンスを作成します。
 */
@Entity
@Table(name = "party_role", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

  /** ロールの一意な識別子（Snowflake ID）。 */
  @Id
  @Column(name = "role_id")
  protected SnowflakeId roleId;

  /** ロールコード（一意制約）。システム内での一意な識別に使用されます。 */
  @Column(name = "role_code", nullable = false, unique = true, length = 30)
  protected String roleCode;

  /** ロールの名称。ロールを人間が理解しやすい名前で識別します。 */
  @Column(name = "role_name", nullable = false, length = 50)
  protected String roleName;

  /** ロールの種類コード。ロールの性質を分類します。 */
  @Column(name = "role_type_code", nullable = false, length = 20)
  protected String roleTypeCode;

  /** ロールの説明。ロールの詳細な説明や用途を記述します。 */
  @Column(name = "description", length = 200)
  protected String description;

  /** ロールの有効開始日。この日時からロールが有効となります。 */
  @Column(name = "valid_from", nullable = false)
  protected LocalDateTime validFrom;

  /** ロールの有効終了日。この日時までロールが有効となります。nullの場合は現在も有効です。 */
  @Column(name = "valid_to")
  protected LocalDateTime validTo;

  /** このロールが割り当てられている組織の一覧。 */
  @OneToMany
  @JoinColumn(name = "role_id")
  protected List<RoleAssignmentForOrg> orgsAssigned = new ArrayList<>();

  /** このロールが割り当てられている従業員の一覧。 */
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
