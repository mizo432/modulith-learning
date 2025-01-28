package undecided.erp.relationship.adapters.dataSource.person;

import org.springframework.stereotype.Component;
import undecided.erp.relationship.domain.model.party.person.Person;
import undecided.erp.relationship.domain.model.party.person.PersonRepository;

@Component
public class PersonDaraSource implements PersonRepository {

  @Override
  public void insert(Person person) {
    // TODO atdk
  }

  @Override
  public void update(Person person) {
    // TODO atdk
  }
}
