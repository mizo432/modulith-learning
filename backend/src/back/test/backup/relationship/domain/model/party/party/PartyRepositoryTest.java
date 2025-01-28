package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Disabled
class PartyRepositoryTest {

  @Autowired
  PartyRepository partyRepository;

  @Nested
  class FindByIdTest {

    @Test
    void shouldFindPartyById() {
      assertThat(partyRepository).isNotNull();
    }

  }

}