package undecided.erp.relationship.domain.model.party.partyRelationship;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Represents a relationship between parties with attributes such as ID and relationship
 * descriptions.
 * <p>
 * This class implements the IPartyRelationship interface, providing a structure to define the
 * relationship from one party to another within a system.
 */
@AllArgsConstructor
@Getter
public class PartyRelationship implements IPartyRelationship {

  private SnowflakeId id;
  private SnowflakeId from;
  private SnowflakeId to;
  private IPartyRelationshipDesc partyRelationshipDesc;

  /**
   * Represents a collection of party relationships.
   * <p>
   * PartyRelationships is a class that implements the IPartyRelationships interface, providing an
   * immutable list of IPartyRelationship objects. This list represents the relationships associated
   * with a party.
   */
  public static class PartyRelationships implements IPartyRelationships {

    private final List<IPartyRelationship> value = Lists.newArrayList();

    public PartyRelationships(List<IPartyRelationship> partyRelationships) {
      this.value.addAll(partyRelationships);

    }

    public static PartyRelationships reconstruct(List<IPartyRelationship> partyRelationships) {
      return new PartyRelationships(partyRelationships);
    }

    public List<IPartyRelationship> getValue() {
      return Collections.unmodifiableList(value);
    }

  }
}
