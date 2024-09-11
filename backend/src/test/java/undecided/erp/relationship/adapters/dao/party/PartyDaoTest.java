package undecided.erp.relationship.adapters.dao.party;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import undecided.erp.relationship.domain.model.party.party.PartyType;

@DataJpaTest
@Tag("medium")
public class PartyDaoTest {

  @Autowired
  private PartyDao partyDao;

  @Nested
  class FindByTypeTest {

    @Test
    public void shouldReturnValidParties_WhenFindByTypeIsPERSON() {
      //given
      PartyType type = PartyType.PERSON;
      PartyTable expectedParty1 = new PartyTable(1L, type, "party1");
      PartyTable expectedParty2 = new PartyTable(2L, type, "party2");
      partyDao.save(expectedParty1);
      partyDao.save(expectedParty2);

      //when
      List<PartyTable> parties = partyDao.findByType(type);

      //then
      assertThat(parties).containsExactlyInAnyOrder(expectedParty1, expectedParty2);
    }

    @Test
    public void shouldReturnValidParties_WhenFindByTypeIsORGANIZATION() {
      //given
      PartyType type = PartyType.ORGANIZATION;
      PartyTable expectedParty = new PartyTable(3L, type, "party");
      partyDao.save(expectedParty);

      //when
      List<PartyTable> parties = partyDao.findByType(type);

      //then
      assertThat(parties).containsExactly(expectedParty);
    }

    @Test
    public void shouldReturnNoParty_WhenFindByTypeIsUNKNOWN() {
      //given
      PartyType type = PartyType.UNKNOWN;

      //when
      List<PartyTable> parties = partyDao.findByType(type);

      //then
      assertThat(parties).isEmpty();
    }

  }

}
