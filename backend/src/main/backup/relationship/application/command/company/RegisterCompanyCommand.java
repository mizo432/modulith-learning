package undecided.erp.relationship.application.command.company;

import undecided.erp.relationship.domain.model.orgRole.company.Company;
import undecided.erp.relationship.domain.model.party.organization.Organization;
import undecided.erp.relationship.domain.model.party.party.Party;

public interface RegisterCompanyCommand {

  void execute(Party party, Company role, Organization organizationModel);

}
