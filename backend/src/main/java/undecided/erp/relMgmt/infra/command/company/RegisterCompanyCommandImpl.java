package undecided.erp.relMgmt.infra.command.company;


import org.springframework.stereotype.Service;
import undecided.erp.relMgmt.appl.command.company.RegisterCompanyCommand;
import undecided.erp.relMgmt.model.orgRole.company.Company;
import undecided.erp.relMgmt.model.party.organization.Organization;
import undecided.erp.relMgmt.model.party.party.Party;

@Service
public class RegisterCompanyCommandImpl implements RegisterCompanyCommand {

  @Override
  public void execute(Party party, Company role, Organization organizationModel) {
    
  }
}
