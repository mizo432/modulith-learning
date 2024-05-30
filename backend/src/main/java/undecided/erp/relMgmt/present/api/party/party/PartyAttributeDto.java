package undecided.erp.relMgmt.present.api.party.party;

import undecided.erp.relMgmt.model.party.party.GovtAssignedId;
import undecided.erp.relMgmt.model.party.party.PartyAttribute;

public record PartyAttributeDto(GovtAssignedId govtAssignedId) {

  public PartyAttribute toModel() {
    return PartyAttribute.create(govtAssignedId);

  }
}
