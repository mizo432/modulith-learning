package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
class PartyRelationshipTest {

  private static final SnowflakeId relationshipId = SnowflakeId.of(1L);
  private static final SnowflakeId partyId = SnowflakeId.of(2L);
  private static final SnowflakeId relatedPartyId = SnowflakeId.of(3L);
  private static final RelationshipType relationshipType = RelationshipType.EMPLOYED_BY;
  private static final WhenAssociated whenAssociated =
      WhenAssociated.of(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
  private static final WhenDisassociated whenDisassociated =
      WhenDisassociated.of(LocalDateTime.of(2024, 6, 20, 14, 45, 0));

  private Person createPerson(SnowflakeId id) {
    return new Person(
        id,
        PartyTypeCode.PERSON,
        "テスト",
        "CODE" + id.value(),
        GovtAssignedId.of(String.valueOf(id.value())),
        SearchName.of("テスト"),
        LegalName.create("山田", "太郎"),
        DateOfBirth.of(LocalDate.of(1990, 1, 1)),
        Collections.emptyList());
  }

  private Party party() {
    return createPerson(partyId);
  }

  private Party relatedParty() {
    return createPerson(relatedPartyId);
  }

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("有効なパラメータでインスタンスが作成される")
    void shouldCreateInstanceWithValidParameters() {
      PartyRelationship partyRelationship =
          new PartyRelationship(
              relationshipId,
              party(),
              relatedParty(),
              relationshipType,
              whenAssociated,
              whenDisassociated);

      assertThat(partyRelationship).isNotNull();
      assertThat(partyRelationship.getRelationshipId()).isEqualTo(relationshipId);
      assertThat(partyRelationship.getPartyId()).isEqualTo(partyId);
      assertThat(partyRelationship.getRelatedPartyId()).isEqualTo(relatedPartyId);
      assertThat(partyRelationship.getRelationshipType()).isEqualTo(relationshipType);
      assertThat(partyRelationship.getWhenAssociated()).isEqualTo(whenAssociated);
      assertThat(partyRelationship.getWhenDisassociated()).isEqualTo(whenDisassociated);
    }

    @Test
    @DisplayName("relationshipIdがnullの場合NullPointerExceptionがスローされる")
    void shouldThrowExceptionWhenRelationshipIdIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      null,
                      party(),
                      relatedParty(),
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("relationshipId must not be null");
    }

    @Test
    @DisplayName("partyがnullの場合NullPointerExceptionがスローされる")
    void shouldThrowExceptionWhenPartyIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      null,
                      relatedParty(),
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("party must not be null");
    }

    @Test
    @DisplayName("relatedPartyがnullの場合NullPointerExceptionがスローされる")
    void shouldThrowExceptionWhenRelatedPartyIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      party(),
                      null,
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("relatedParty must not be null");
    }

    @Test
    @DisplayName("relationshipTypeがnullの場合NullPointerExceptionがスローされる")
    void shouldThrowExceptionWhenRelationshipTypeIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      party(),
                      relatedParty(),
                      null,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("relationshipType must not be null");
    }

    @Test
    @DisplayName("whenAssociatedがnullの場合でもインスタンスが作成される")
    void shouldCreateInstanceWhenWhenAssociatedIsNull() {
      PartyRelationship partyRelationship =
          new PartyRelationship(
              relationshipId, party(), relatedParty(), relationshipType, null, whenDisassociated);

      assertThat(partyRelationship).isNotNull();
      assertThat(partyRelationship.getWhenAssociated()).isNull();
    }

    @Test
    @DisplayName("whenDisassociatedがnullの場合でもインスタンスが作成される")
    void shouldCreateInstanceWhenWhenDisassociatedIsNull() {
      PartyRelationship partyRelationship =
          new PartyRelationship(
              relationshipId, party(), relatedParty(), relationshipType, whenAssociated, null);

      assertThat(partyRelationship).isNotNull();
      assertThat(partyRelationship.getWhenDisassociated()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じrelationshipIdを持つインスタンスは等しい")
    void shouldBeEqualWhenSameRelationshipId() {
      PartyRelationship first =
          new PartyRelationship(
              relationshipId,
              party(),
              relatedParty(),
              relationshipType,
              whenAssociated,
              whenDisassociated);
      PartyRelationship second =
          new PartyRelationship(
              relationshipId,
              createPerson(SnowflakeId.of(4L)),
              createPerson(SnowflakeId.of(5L)),
              RelationshipType.UNKNOWN,
              WhenAssociated.empty(),
              WhenDisassociated.empty());

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なるrelationshipIdを持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentRelationshipId() {
      PartyRelationship first =
          new PartyRelationship(
              relationshipId,
              party(),
              relatedParty(),
              relationshipType,
              whenAssociated,
              whenDisassociated);
      PartyRelationship second =
          new PartyRelationship(
              SnowflakeId.of(100L),
              party(),
              relatedParty(),
              relationshipType,
              whenAssociated,
              whenDisassociated);

      assertThat(first).isNotEqualTo(second);
    }
  }
}
