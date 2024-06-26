package undecided.erp.relMgmt.adapters.rest.party.party;

import undecided.erp.relMgmt.domain.model.party.party.GovtAssignedId;
import undecided.erp.relMgmt.domain.model.party.party.PartyAttribute;

public record PartyAttributeDto(GovtAssignedId govtAssignedId) {

  public PartyAttribute toModel() {
    return PartyAttribute.create(govtAssignedId);

  }
}
