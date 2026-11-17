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

/**
 * ロール種類マスターを表すエンティティクラス。
 *
 * <p>このクラスは、ロールの種類（例：EMPLOYEE, MANAGER, ADMIN）のマスターデータを管理します。
 */
@Entity
@Table(name = "role_type", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleType {
  @Id
  @Column(name = "role_type_code", length = 20)
  private String roleTypeCode;

  /** 種類名称。 */
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  /** 説明。 */
  @Column(name = "description", length = 200)
  private String description;

  /** 作成日時。 */
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  /**
   * ロール種類マスターを作成します。
   *
   * @param roleTypeCode ロール種類コード
   * @param name 種類名称
   * @param description 説明
   * @param createdAt 作成日時
   */
  public RoleType(String roleTypeCode, String name, String description, LocalDateTime createdAt) {
    this.roleTypeCode = roleTypeCode;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
