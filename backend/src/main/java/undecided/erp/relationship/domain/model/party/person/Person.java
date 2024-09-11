package undecided.erp.relationship.domain.model.party.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Personクラスは、人を表します。
 */
@ToString
@Getter
public class Person {

  private final SnowflakeId id;
  @JsonProperty
  private final PersonNames names;
  private final Birthday birthday;

  Person(SnowflakeId id, PersonNames names, Birthday birthday) {
    this.id = id;
    this.names = names;
    this.birthday = birthday;
  }

  public static Person create(SnowflakeId id, PersonNames names, Birthday birthday) {
    return new Person(id, names, birthday);
  }

  public static Person reconstruct(Long id, PersonNames names, Birthday birthday) {
    return new Person(SnowflakeId.reconstruct(id), names, birthday);

  }

}
