package undecided.erp.relationship.application.employee;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyRepository;
import undecided.erp.relationship.domain.model.party.person.Person;
import undecided.erp.relationship.domain.model.party.person.PersonRepository;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.domain.model.personRole.employee.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeCommand {

  private final EmployeeRepository employeeRepository;
  private final PartyRepository partyRepository;
  private final PersonRepository personRepository;

  void createEmployee(Employee employee, Party party, Person person) {
    employeeRepository.insert(employee);
    partyRepository.insert(party);
    personRepository.insert(person);

  }

  void updateEmployee(Employee employee, Party party, Person person) {
    employeeRepository.update(employee);
    partyRepository.update(party);
    personRepository.update(person);

  }

}
