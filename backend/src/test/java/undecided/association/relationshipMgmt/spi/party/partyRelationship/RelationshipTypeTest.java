package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RelationshipTypeTest {

  @Nested
  @DisplayName("enum定数のテスト")
  class EnumConstantsTest {

    @Test
    @DisplayName("UNKNOWN定数が存在する")
    void shouldHaveUnknownConstant() {
      assertThat(RelationshipType.UNKNOWN).isNotNull();
    }

    @Test
    @DisplayName("EMPLOYED_BY定数が存在する")
    void shouldHaveEmployedByConstant() {
      assertThat(RelationshipType.EMPLOYED_BY).isNotNull();
    }

    @Test
    @DisplayName("valuesメソッドで全定数を取得できる")
    void shouldReturnAllConstants() {
      RelationshipType[] values = RelationshipType.values();

      assertThat(values).hasSize(2);
      assertThat(values).contains(RelationshipType.UNKNOWN, RelationshipType.EMPLOYED_BY);
    }

    @Test
    @DisplayName("valueOfメソッドで名前から定数を取得できる")
    void shouldGetConstantByName() {
      assertThat(RelationshipType.valueOf("UNKNOWN")).isEqualTo(RelationshipType.UNKNOWN);
      assertThat(RelationshipType.valueOf("EMPLOYED_BY")).isEqualTo(RelationshipType.EMPLOYED_BY);
    }
  }
}
