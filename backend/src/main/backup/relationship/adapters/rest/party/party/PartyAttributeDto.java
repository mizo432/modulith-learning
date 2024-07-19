package undecided.erp.relationship.adapters.rest.party.party;

import undecided.erp.relationship.domain.model.party.party.GovtAssignedId;
import undecided.erp.relationship.domain.model.party.party.PartyAttribute;

public record PartyAttributeDto(GovtAssignedId govtAssignedId) {

  public PartyAttribute toModel() {
    return PartyAttribute.create(govtAssignedId);

  }
}
