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

/**
 * 組織種類マスターを表すエンティティクラス。
 *
 * <p>このクラスは、組織の種類（例：COMPANY, STORE, DEPARTMENT）のマスターデータを管理します。
 */
@Entity
@Table(name = "organization_type", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationType {
  @Id
  @Column(name = "organization_type_code", length = 20)
  private String organizationTypeCode;

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
   * 組織種類マスターを作成します。
   *
   * @param organizationTypeCode 組織種類コード
   * @param name 種類名称
   * @param description 説明
   * @param createdAt 作成日時
   */
  public OrganizationType(
      String organizationTypeCode, String name, String description, LocalDateTime createdAt) {
    this.organizationTypeCode = organizationTypeCode;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
