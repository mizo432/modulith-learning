package undecided.erp.relMgmt.adapters.rest.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.relMgmt.application.command.company.RegisterCompanyCommand;
import undecided.erp.relMgmt.domain.model.party.party.Party;

@RestController
@RequestMapping(path = "/rel")
@RequiredArgsConstructor
public class CompanyController {

  private final RegisterCompanyCommand registerCompanyCommand;
/*
  private final CompanyQuery companyQuery;

  @GetMapping(path = "/api/v1/companies/{companyId}")
  ResponseEntity<EmployeeDto> findOneById(@PathVariable("companyId") SnowflakeId<Party> id) {
    return ResponseEntity.ok(companyQuery.findById(id));

  }
*/

  @PostMapping(path = "/api/v1/companies")
  ResponseEntity<Void> post(@RequestBody CompanyRegisterDto companyRegisterDto) {
    Party party = companyRegisterDto.toPartyModel();
    registerCompanyCommand.execute(party, companyRegisterDto.toRoleModel(party.getId()),
        companyRegisterDto.toOrganizationModel(party.getId()));
    return ResponseEntity.ok().build();

  }
}
