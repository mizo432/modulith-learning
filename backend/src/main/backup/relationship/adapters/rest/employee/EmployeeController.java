package undecided.erp.relationship.adapters.rest.employee;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.relationship.application.command.employee.DeleteEmployeeCommand;
import undecided.erp.relationship.application.command.employee.InsertEmployeeCommand;
import undecided.erp.relationship.application.command.employee.UpdateEmployeeCommand;
import undecided.erp.relationship.application.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeNo;

@RestController
@RequestMapping(path = "/relationship")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeQuery employeeQuery;
  private final InsertEmployeeCommand insertEmployeeCommand;
  private final UpdateEmployeeCommand updateEmployeeCommand;
  private final DeleteEmployeeCommand deleteEmployeeCommand;

  @GetMapping(path = "/api/v1/companies/{companyCode}/employees/{employeeNo}")
  EmployeeDTO findOneByCompanyCodeAndEmployeeNo(
      @PathVariable("companyCode") CompanyCode companyCode,
      @PathVariable("employeeNo") EmployeeNo employeeNo) {
    Optional<Employee> employee = employeeQuery.findByCompanyCodeAndEmployeeNo(
        companyCode, employeeNo);
    return EmployeeDTO
        .convertFromModel(employee.orElseThrow(() -> new OpenApiResourceNotFoundException(
            "対象の従業員は存在しません employeeNo: " + employeeNo))
        );

  }

  @GetMapping(path = "/api/v1/companies/{companyCode}/employees")
  EmployeeDTO[] findAnyByCompanyCode(
      @PathVariable("companyCode") CompanyCode companyCode) {
    return EmployeeDTO.convertFromModelsToDTOs(
        employeeQuery.selectByCompanyCode(companyCode));

  }

  @PostMapping(path = "/api/v1/companies/{companyCode}/employees")
  void post(@PathVariable("companyCode") CompanyCode companyCode,
      @Validated @RequestBody EmployeeDTO employee, BindingResult bindingResult) {
    insertEmployeeCommand.execute(companyCode, employee.convertToModel());

  }

  @PutMapping(path = "/api/v1/companies/{companyCode}/employees/{employeeNo}")
  void put(@PathVariable("companyCode") CompanyCode companyCode,
      @PathVariable("employeeNo") EmployeeNo employeeNo,
      @Validated @RequestBody EmployeeDTO employee, BindingResult bindingResult) {
    updateEmployeeCommand.execute(companyCode, employeeNo, employee.convertToModel());

  }

  @DeleteMapping(path = "/api/v1/companies/{companyCode}/employees/{employeeNo}")
  void delete(@PathVariable("companyCode") CompanyCode companyCode,
      @PathVariable("employeeNo") EmployeeNo employeeNo) {
    deleteEmployeeCommand.execute(companyCode, employeeNo);

  }
}
