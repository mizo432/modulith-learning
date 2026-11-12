package undecided.association.relationshipMgmt.party.internal.person;

import org.springframework.stereotype.Component;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * Person エンティティのファクトリ実装クラスです。
 *
 * <p>{@link SnowflakeIdGenerator} を利用して一意のパーティ ID を生成し、 入力された Person の情報から新しい Person エンティティを作成します。
 */
@Component
class PersonFactoryImpl implements PersonFactory {

  private final SnowflakeIdGenerator snowflakeIdGenerator;

  /**
   * PersonFactoryImpl を構築します。
   *
   * @param snowflakeIdGenerator Snowflake ID 生成器
   */
  PersonFactoryImpl(SnowflakeIdGenerator snowflakeIdGenerator) {
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  /**
   * 入力された Person の情報から新しい Person エンティティを作成します。
   *
   * <p>生成された Person は、{@link SnowflakeIdGenerator} によって生成された一意の ID と {@link PartyTypeCode#PERSON}
   * タイプコードが設定されます。 また、有効開始日 ({@code validFrom}) には現在時刻が設定され、有効終了日 ({@code validTo}) は
   * null（無期限）となります。
   *
   * @param person 名前、コード、政府発行 ID、検索用名義、法的名義、生年月日などの情報を含む Person
   * @return 一意の ID が設定された新しい Person エンティティ
   */
  public Person create(Person person) {
    SnowflakeId partyId = snowflakeIdGenerator.nextIdValue();
    Person result =
        new Person(
            partyId,
            PartyTypeCode.PERSON,
            person.getName(),
            person.getCode(),
            person.getGovtAssignedId(),
            person.getSearchName(),
            person.getLegalName(),
            person.getDateOfBirth(),
            person.getPartyRelationships());
    return result;
  }
}
