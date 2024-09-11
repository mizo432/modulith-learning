package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.entity.SnowflakeId;

public class PersonTest {

  @Test
  void testCreate() {
    // Initialize the test data
    SnowflakeId id = SnowflakeId.of(123L);
    PersonNames names = PersonNames.create(FirstName.of("First"), LastName.of("Last"));
    Birthday birthday = Birthday.of(LocalDate.now());

    // Call the method under test
    Person person = Person.create(id, names, birthday);

    // Assert the results
    assertThat(person.getId()).isEqualTo(id);
    assertThat(person.getNames()).isEqualTo(names);
    assertThat(person.getBirthday()).isEqualTo(birthday);
  }

  @Test
  void testReconstruct() {
    // Initialize the test data
    Long idValue = 123L;
    PersonNames names = PersonNames.create(FirstName.of("First"), LastName.of("Last"));
    Birthday birthday = Birthday.of(LocalDate.now());

    // Call the method under test
    Person person = Person.reconstruct(idValue, names, birthday);

    // Assert the results
    assertThat(person.getId()).isEqualTo(SnowflakeId.reconstruct(idValue));
    assertThat(person.getNames()).isEqualTo(names);
    assertThat(person.getBirthday()).isEqualTo(birthday);
  }
}
