package undecided.association.relationshipMgmt.party.internal.person;

import undecided.association.relationshipMgmt.spi.party.person.Person;

public interface PersonFactory {

  Person create(Person person);
}
