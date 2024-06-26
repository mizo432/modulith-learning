package undecided.erp.relationship.domain.model.party.partyRelationship;

import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * パーティ関連
 */
public class PartyRelationship {

  private SnowflakeId<PartyRelationship> id;
  private SnowflakeId<Party> from;
  private SnowflakeId<Party> to;

}
