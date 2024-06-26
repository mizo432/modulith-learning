package undecided.erp.relMgmt.domain.model.personRole.actor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.relMgmt.domain.model.party.person.Person;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

@AllArgsConstructor
@Getter
public class Actor {

  private SnowflakeId<Party> id;

  private String name;

  public static Actor reconstruct(SnowflakeId<Party> id, String name) {
    return new Actor(id, name);

  }

  public static Actor reconstruct(Person person) {
    return new Actor(person.getId(), person.fullName());

  }
}
