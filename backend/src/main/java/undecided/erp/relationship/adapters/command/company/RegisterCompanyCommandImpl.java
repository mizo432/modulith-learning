package undecided.erp.relationship.adapters.command.company;


import org.springframework.stereotype.Service;
import undecided.erp.relationship.application.command.company.RegisterCompanyCommand;
import undecided.erp.relationship.domain.model.orgRole.company.Company;
import undecided.erp.relationship.domain.model.party.organization.Organization;
import undecided.erp.relationship.domain.model.party.party.Party;

@Service
public class RegisterCompanyCommandImpl implements RegisterCompanyCommand {

  @Override
  public void execute(Party party, Company role, Organization organizationModel) {

  }
}
