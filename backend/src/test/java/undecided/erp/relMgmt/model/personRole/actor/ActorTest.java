package undecided.erp.relMgmt.model.personRole.actor;

import org.junit.jupiter.api.Test;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.party.person.Person;
import undecided.erp.relMgmt.domain.model.personRole.actor.Actor;
import undecided.erp.shared.entity.SnowflakeId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActorTest {

  @Test
  public void testReconstruct_withIdAndName() {
    // Arrange
    SnowflakeId<Party> id = new SnowflakeId<>();
    String name = "Test Name";

    // Act
    Actor actor = Actor.reconstruct(id, name);

    // Assert
    assertEquals(id, actor.getId());
    assertEquals(name, actor.getName());
  }

  @Test
  public void testReconstruct_withPerson() {
    // Arrange
    SnowflakeId<Party> id = new SnowflakeId<>();
    Person person = new Person() {
      public String fullName() {
        return "Test Name";
      }

      @Override
      public SnowflakeId<Party> getId() {
        return id;
      }
    };

    // Act
    Actor actor = Actor.reconstruct(person);

    // Assert
    assertEquals(person.getId(), actor.getId());
    assertEquals(person.fullName(), actor.getName());
  }
}