package undecided.erp.relationship.domain.model.party.person;

import lombok.Getter;
import lombok.ToString;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Personクラスは、人を表します。
 */
@ToString
@Getter
public class Person {

  private SnowflakeId<Party> id;
  FirstName firstName;
  LastName lastName;

  public String fullName() {
    return FullName.create(firstName, lastName).getValue();
  }

}
