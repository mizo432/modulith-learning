package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

class PartyRelationshipTest {

  private static final SnowflakeId relationshipId = SnowflakeId.of(1L);
  private static final SnowflakeId partyId = SnowflakeId.of(2L);
  private static final SnowflakeId relatedPartyId = SnowflakeId.of(3L);
  private static final RelationshipType relationshipType = RelationshipType.EMPLOYED_BY;
  private static final WhenAassociated whenAssociated =
      WhenAassociated.of(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
  private static final WhenDisassociated whenDisassociated =
      WhenDisassociated.of(LocalDateTime.of(2024, 6, 20, 14, 45, 0));

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("有効なパラメータでインスタンスが作成される")
    void shouldCreateInstanceWithValidParameters() {
      PartyRelationship partyRelationship =
          new PartyRelationship(
              relationshipId,
              partyId,
              relatedPartyId,
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
    @DisplayName("relationshipIdがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenRelationshipIdIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      null,
                      partyId,
                      relatedPartyId,
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("relationshipId must not be null");
    }

    @Test
    @DisplayName("partyIdがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenPartyIdIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      null,
                      relatedPartyId,
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("partyId must not be null");
    }

    @Test
    @DisplayName("relatedPartyIdがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenRelatedPartyIdIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      partyId,
                      null,
                      relationshipType,
                      whenAssociated,
                      whenDisassociated))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("relatedPartyId must not be null");
    }

    @Test
    @DisplayName("relationshipTypeがnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenRelationshipTypeIsNull() {
      assertThatThrownBy(
              () ->
                  new PartyRelationship(
                      relationshipId,
                      partyId,
                      relatedPartyId,
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
              relationshipId, partyId, relatedPartyId, relationshipType, null, whenDisassociated);

      assertThat(partyRelationship).isNotNull();
      assertThat(partyRelationship.getWhenAssociated()).isNull();
    }

    @Test
    @DisplayName("whenDisassociatedがnullの場合でもインスタンスが作成される")
    void shouldCreateInstanceWhenWhenDisassociatedIsNull() {
      PartyRelationship partyRelationship =
          new PartyRelationship(
              relationshipId, partyId, relatedPartyId, relationshipType, whenAssociated, null);

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
              partyId,
              relatedPartyId,
              relationshipType,
              whenAssociated,
              whenDisassociated);
      PartyRelationship second =
          new PartyRelationship(
              relationshipId,
              SnowflakeId.of(4L),
              SnowflakeId.of(5L),
              RelationshipType.UNKNOWN,
              WhenAassociated.empty(),
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
              partyId,
              relatedPartyId,
              relationshipType,
              whenAssociated,
              whenDisassociated);
      PartyRelationship second =
          new PartyRelationship(
              SnowflakeId.of(100L),
              partyId,
              relatedPartyId,
              relationshipType,
              whenAssociated,
              whenDisassociated);

      assertThat(first).isNotEqualTo(second);
    }
  }
}
