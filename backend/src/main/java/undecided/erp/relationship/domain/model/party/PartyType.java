package undecided.erp.relationship.domain.model.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

/**
 * PartyTypeは、パーティ(主体)の種類を表現する列挙型です。
 * <p>
 * 各種類は一意の識別子(ID)を持ち、データベースとの相互変換にも使用されます。
 * <p>
 * 以下の種類が定義されています: - PERSON: 個人 - ORGANIZATION: 組織 - ORGANIZATION_UNIT: 組織単位 - OTHER: その他
 * <p>
 * このクラスには、指定されたIDに対応する列挙値を取得するための静的メソッドと、 JPAのAttributeConverterを使用してデータベースの値との変換を提供する内部クラスがあります。
 */
@Getter
public enum PartyType {
  /**
   * PERSONは、PartyType列挙型の一つの値であり、個人を表す定数です。
   * <p>
   * この値は、データベースとの相互変換や分類において、パーティ(主体)が「個人」であることを示すために使用されます。
   * <p>
   * 定数が持つ識別子(ID)は1であり、静的メソッドvalueOfId(int id)やJPAのAttributeConverterを通じて IDと列挙型値間の相互変換をサポートします。
   */
  PERSON(1),
  /**
   * ORGANIZATIONは、PartyType列挙型の一つの値であり、組織を表す定数です。
   * <p>
   * この定数は、パーティ(主体)が「組織」であることを示すために使用されます。 データベースとの相互変換や分類において、組織を識別するために使用されます。
   * <p>
   * 定数が持つ識別子(ID)は2であり、静的メソッドvalueOfId(int id)や JPAのAttributeConverterを通じて、IDと列挙型値間の相互変換をサポートします。
   */
  ORGANIZATION(2),
  /**
   * ORGANIZATION_UNITは、PartyType列挙型の一つの値であり、組織単位を表す定数です。
   * <p>
   * この定数は、パーティ(主体)が「組織単位」であることを示すために使用されます。 データベースとの相互変換や分類において、組織単位を識別するために使用されます。
   * <p>
   * 定数が持つ識別子(ID)は3であり、静的メソッドvalueOfId(int id)や JPAのAttributeConverterを通じて、IDと列挙型値間の相互変換をサポートします。
   */
  ORGANIZATION_UNIT(3),
  /**
   * OTHERは、PartyType列挙型の一つの値であり、その他の分類に該当するパーティ(主体)を表す定数です。
   * <p>
   * この定数は、パーティの種類が既存のいずれの分類にも適合しない場合に使用されます。 データベースとの対応付けや処理において、その他としての分類を示します。
   * <p>
   * 定数が持つ識別子(ID)は4であり、静的メソッドvalueOfId(int id)や JPAのAttributeConverterを通じて、IDと列挙型値間の相互変換をサポートします。
   */
  OTHER(4);

  private final int id;

  /**
   * PartyTypeクラスのコンストラクタ。
   *
   * @param id PartyTypeに関連付けられている一意の識別子(ID)
   */
  PartyType(int id) {
    this.id = id;
  }

  /**
   * 指定されたIDに対応するPartyType列挙値を取得します。
   *
   * @param id PartyTypeに関連付けられている一意の識別子(ID)
   * @return 指定されたIDに対応するPartyType列挙値
   * @throws IllegalArgumentException IDに対応するPartyTypeが存在しない場合
   */
  private static PartyType valueOfId(int id) {
    for (PartyType type : PartyType.values()) {
      if (type.id == id) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid id: " + id);

  }

  /**
   * PartyTypeConverterクラスは、PartyType列挙型とデータベース列で使用される整数値の間の 相互変換を提供するAttributeConverterの実装です。
   * <p>
   * このクラスはJPAのコンバータとして使用され、PartyType列挙型の値を対応する整数の識別子に変換し、 逆に整数値をPartyType列挙型に戻す役割を果たします。
   * <p>
   * 主な機能: - Enum値をデータベース列として永続化する際に使用されます。 - データベース列から読み込まれた値をEnum型の値に変換します。
   * <p>
   * 使用される主なコンテキスト: - Partyエンティティのtypeフィールドのコンバータとして注釈で指定されます。
   * <p>
   * 変換処理の詳細: - convertToDatabaseColumn: PartyTypeインスタンスの`id`プロパティを使用して対応する整数値に変換します。 -
   * convertToEntityAttribute: 与えられた整数値に対応するPartyType列挙型を取得します。
   */
  @Converter
  public static class PartyTypeConverter implements AttributeConverter<PartyType, Integer> {

    /**
     * PartyType列挙型の値をデータベースカラムで使用される整数値に変換します。
     *
     * @param partyType 変換対象のPartyType列挙型オブジェクト
     * @return データベースカラムに対応する整数値。partyTypeがnullの場合はnullを返します。
     */
    @Override
    public Integer convertToDatabaseColumn(PartyType partyType) {
      return partyType.id;
    }

    /**
     * データベースから取得した整数値を対応するPartyType列挙型に変換します。
     *
     * @param id データベースに保存されている整数値。PartyType列挙型にマッピングされる値。
     * @return 整数値に対応するPartyType列挙型の値。idがnullまたは対応するPartyTypeが存在しない場合はnullを返す可能性があります。
     */
    @Override
    public PartyType convertToEntityAttribute(Integer id) {
      return PartyType.valueOfId(id);
    }
  }
}
