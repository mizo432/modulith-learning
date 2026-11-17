package undecided.association.relationshipMgmt.spi.party;

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
 * パーティー種類マスターを表すエンティティクラス。
 *
 * <p>このクラスは、パーティーの種類（MASTER, PERSON, ORG）のマスターデータを管理します。
 */
@Entity
@Table(name = "party_type", schema = SchemaRelationshipMgmt.NAME)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyType {
  @Id
  @Column(name = "party_type_code", length = 20)
  private String partyTypeCode;

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
   * パーティー種類マスターを作成します。
   *
   * @param partyTypeCode パーティー種類コード
   * @param name 種類名称
   * @param description 説明
   * @param createdAt 作成日時
   */
  public PartyType(String partyTypeCode, String name, String description, LocalDateTime createdAt) {
    this.partyTypeCode = partyTypeCode;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }
}
