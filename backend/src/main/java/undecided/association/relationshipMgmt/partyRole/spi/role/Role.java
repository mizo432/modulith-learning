package undecided.association.relationshipMgmt.partyRole.spi.role;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import com.github.mizo432.shared.common.exception.BusinessException;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

  // JPA用パッケージプライベートセッター
  void setRoleId(SnowflakeId roleId) {
    this.roleId = roleId;
  }

  void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  void setRoleTypeCode(String roleTypeCode) {
    this.roleTypeCode = roleTypeCode;
  }

  void setDescription(String description) {
    this.description = description;
  }

  void setValidFrom(LocalDateTime validFrom) {
    this.validFrom = validFrom;
  }

  void setValidTo(LocalDateTime validTo) {
    this.validTo = validTo;
  }

  void setOrgsAssigned(List<RoleAssignmentForOrg> orgsAssigned) {
    this.orgsAssigned = orgsAssigned;
  }

  void setEmpsAssigned(List<RoleAssignmentForEmp> empsAssigned) {
    this.empsAssigned = empsAssigned;
  }

  /**
   * ロール情報を更新します。
   *
   * @param roleCode ロールコード（null不可）
   * @param roleName ロール名称（null不可）
   * @param roleTypeCode ロール種類コード（null不可）
   * @param description 説明（null可）
   */
  public void update(
      @NonNull String roleCode,
      @NonNull String roleName,
      @NonNull String roleTypeCode,
      @Nullable String description) {
    checkNotNull(roleCode, () -> new BusinessException("roleCode must not be null"));
    checkNotNull(roleName, () -> new BusinessException("roleName must not be null"));
    checkNotNull(roleTypeCode, () -> new BusinessException("roleTypeCode must not be null"));
    if (roleCode.trim().isEmpty()) {
      throw new BusinessException("roleCode must not be empty");
    }
    if (roleCode.trim().length() > 30) {
      throw new BusinessException("roleCode must not exceed 30 characters");
    }
    if (roleName.trim().length() > 50) {
      throw new BusinessException("roleName must not exceed 50 characters");
    }
    if (roleTypeCode.trim().length() > 20) {
      throw new BusinessException("roleTypeCode must not exceed 20 characters");
    }
    if (description != null && description.trim().length() > 200) {
      throw new BusinessException("description must not exceed 200 characters");
    }
    // validToが設定されている場合、validFromより後であることを検証する
    if (this.validTo != null && this.validTo.isBefore(this.validFrom)) {
      throw new BusinessException("validTo must be after validFrom");
    }
    this.roleCode = roleCode;
    this.roleName = roleName;
    this.roleTypeCode = roleTypeCode;
    this.description = description;
  }
}
