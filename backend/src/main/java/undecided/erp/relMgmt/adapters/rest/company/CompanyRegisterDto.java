package undecided.erp.relMgmt.adapters.rest.company;

import undecided.erp.relMgmt.adapters.rest.party.organization.OrganizationAttributeDto;
import undecided.erp.relMgmt.adapters.rest.party.party.PartyAttributeDto;
import undecided.erp.relMgmt.model.orgRole.company.Company;
import undecided.erp.relMgmt.model.party.organization.Organization;
import undecided.erp.relMgmt.model.party.party.Party;
import undecided.erp.relMgmt.model.party.party.PartyType;
import undecided.erp.shared.entity.SnowflakeId;

public record CompanyRegisterDto(CompanyAttributeDto attribute,
                                 OrganizationAttributeDto organizationAttribute,
                                 PartyAttributeDto partyAttribute) {

  public Company toRoleModel(SnowflakeId<Party> id) {
    return Company.create(id, attribute.toModel());
  }

  public Party toPartyModel() {
    return Party.createForInsert(PartyType.ORGANIZATION, partyAttribute.toModel());
  }

  public Organization toOrganizationModel(SnowflakeId<Party> partyId) {
    return Organization.createForInsert(partyId, organizationAttribute.toModel());
  }
}
