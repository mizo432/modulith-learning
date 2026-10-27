package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * パーティ（個人や法人など）を表す抽象エンティティクラス。
 *
 * <p>このクラスは、関係管理において対象となるパーティの共通属性を保持します。
 */
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Party {
  @Id private SnowflakeId partyId;

  /** 政府発行の識別子（例：マイナンバー、法人番号など）。 */
  private GovtAssignedId govtAssignedId;

  /** 検索用名称（全角文字）。 */
  private SearchName searchName;
}
