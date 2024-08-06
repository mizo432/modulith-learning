package undecided.erp.relationship.domain.model.personRole.actor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.relationship.domain.model.party.person.Person;
import undecided.erp.shared.entity.SnowflakeId;

@AllArgsConstructor
@Getter
public class Actor {

  private SnowflakeId id;

  private String name;

  public static Actor reconstruct(SnowflakeId id, String name) {
    return new Actor(id, name);

  }

  public static Actor reconstruct(Person person) {
    return new Actor(person.getId(), person.getNames().fullName().getValue());

  }
}
