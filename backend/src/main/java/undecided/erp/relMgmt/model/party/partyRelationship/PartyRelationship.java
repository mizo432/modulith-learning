package undecided.erp.relMgmt.model.party.partyRelationship;

import undecided.erp.relMgmt.model.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * パーティ関連
 */
public class PartyRelationship {

  private SnowflakeId<PartyRelationship> id;
  private SnowflakeId<Party> from;
  private SnowflakeId<Party> to;

}
