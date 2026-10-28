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
import lombok.Setter;
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
@Setter
public class Role {
  @Id
  @Column(name = "role_id")
  private SnowflakeId roleId;

  /** ロールコード（一意制約）。 */
  @Column(name = "role_code", nullable = false, unique = true, length = 30)
  private String roleCode;

  /** ロール名称。 */
  @Column(name = "role_name", nullable = false, length = 50)
  private String roleName;

  /** ロール種類コード。 */
  @Column(name = "role_type_code", nullable = false, length = 20)
  private String roleTypeCode;

  /** 説明。 */
  @Column(name = "description", length = 200)
  private String description;

  /** 有効開始日。 */
  @Column(name = "valid_from", nullable = false)
  private LocalDateTime validFrom;

  /** 有効終了日。 */
  @Column(name = "valid_to")
  private LocalDateTime validTo;

  @OneToMany
  @JoinColumn(name = "role_id")
  private List<RoleAssignmentForOrg> orgsAssigned = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "role_id")
  private List<RoleAssignmentForEmp> empsAssigned = new ArrayList<>();
}
