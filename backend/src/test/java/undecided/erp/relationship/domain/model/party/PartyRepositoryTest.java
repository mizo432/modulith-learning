package undecided.erp.relationship.domain.model.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("パーティリポジトリのテスト")
class PartyRepositoryTest {

  @Autowired
  private PartyRepository partyRepository;

  private Party createParty(Long partyId, String searchName, PartyType type) {
    Party party = new Party(partyId, searchName, type);
    return party;
  }

  @Nested
  @DisplayName("findBySearchNameBeforeメソッドのテスト")
  class FindBySearchNameBeforeTest {

    @Test
    @DisplayName("検索名が指定された場合、該当するエンティティを返す")
    void shouldReturnEntitiesWhenSearchNameMatches() {
      // Arrange
      Party party1 = createParty(1L, "ABC", PartyType.PERSON);
      Party party2 = createParty(2L, "DEF", PartyType.ORGANIZATION);
      partyRepository.saveAll(Arrays.asList(party1, party2));

      // Act
      List<Party> result = partyRepository.findBySearchNameBefore("DEF");

      // Assert
      assertThat(result)
          .hasSize(1)
          .extracting(Party::getSearchName)
          .containsExactly("ABC");
    }

    @Test
    @DisplayName("該当する検索名がない場合、空のリストを返す")
    void shouldReturnEmptyListWhenNoMatchingEntities() {
      // Arrange
      Party party1 = createParty(1L, "XYZ", PartyType.PERSON);
      partyRepository.save(party1);

      // Act
      List<Party> result = partyRepository.findBySearchNameBefore("ABC");

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("検索名がNULLの場合、例外がスローされる")
    void shouldThrowExceptionWhenSearchNameIsNull() {
      // Act and Assert
      assertThatThrownBy(() -> partyRepository.findBySearchNameBefore(null))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
