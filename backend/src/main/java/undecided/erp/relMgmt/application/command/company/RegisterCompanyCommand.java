package undecided.erp.relMgmt.application.command.company;

import undecided.erp.relMgmt.model.orgRole.company.Company;
import undecided.erp.relMgmt.model.party.organization.Organization;
import undecided.erp.relMgmt.model.party.party.Party;

public interface RegisterCompanyCommand {

  void execute(Party party, Company role, Organization organizationModel);

}
