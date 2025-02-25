package undecided.erp.relationship.domain.model.party;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.relationship.domain.model.party.PartyType.PartyTypeConverter;

/**
 * Partyクラスは、エンティティとしてシステム内でのパーティ（関係者）を表現します。
 * <p>
 * このクラスは以下の特性を持ちます: - `partyId`: パーティ固有の識別子を表す。SnowflakeIdを使用。 - `searchName`:
 * 検索用の名前情報を保持する。SearchNameを使用。 - `type`: パーティのタイプを示す。PartyTypeを使用。
 * <p>
 * このクラスは@NoArgsConstructorアノテーションにより、デフォルトでprotectedな引数なしコンストラクタを提供します。
 * また、@RequiredArgsConstructorにより、必須フィールドを初期化するためのコンストラクタを生成します。
 * <p>
 * 主にデータベースエンティティとして使用されることを意図しており、@Entityアノテーションによりエンティティとしてマッピングされます。
 * 各フィールドには@EmbeddedIdや@Convertアノテーションが使用され、適切な変換ロジックや組み込み型をサポートしています。
 * <p>
 * オーバーライドされたtoStringメソッドにより、このクラスのインスタンスを文字列表現として簡易的に出力することが可能です。
 */
@AllArgsConstructor
@Entity
@Table(name = "parties", indexes = {
    @Index(columnList = "search_name", name = "idx01_party"),
    @Index(columnList = "type", name = "idx02_party")})
@NoArgsConstructor
public class Party extends PptEntity<Party> implements Serializable {

  /**
   * ユニークなパーティー識別子を表す変数。 アプリケーションにおける各パーティーを一意に識別するために使用されます。 データベース上の "party_id" カラムに対応し、null
   * 値は許可されていません。
   */
  @Id
  @Column(name = "party_id", columnDefinition = "BIGINT", nullable = false)
  //@Convert(converter = SnowflakeIdConverter.class)
  private Long partyId;

  /**
   * searchNameフィールドは、検索用の名前情報を保持します。
   * <p>
   * このフィールドは、SearchName型で定義されており、@Convertアノテーションを使用して
   * SearchName.SearchNameConverterクラスを用いた変換処理が適用されます。
   * <p>
   * 主にデータベースの永続化および取得において、このフィールドが文字列または特定の形式で 保存されるための変換ロジックをサポートします。
   */
//  @Convert(converter = SearchName.SearchNameConverter.class)
  @Getter
  @Column(name = "search_name", nullable = false, length = 100)
  private String searchName;

  /**
   * typeフィールドは、パーティのタイプ情報を保持します。
   * <p>
   * このフィールドはPartyType型で定義されており、@Convertアノテーションを使用して PartyType.PartyTypeConverterクラスによる変換処理が適用されます。
   * <p>
   * 主にデータベースへの永続化および取得時において、このフィールドは適切な形式で 格納・復元されるよう処理されます。
   */
  @Convert(converter = PartyTypeConverter.class)
  @Column(name = "type", nullable = false, length = 2)
  private PartyType type;

  /**
   * このメソッドはPartyクラスの文字列表現を生成します。 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Partyオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Party{" +
        "partyId=" + partyId +
        ", searchName=" + searchName +
        ", type=" + type +
        '}';
  }

}
