package undecided.erp.relationship.infra.query.employee;

import java.util.Optional;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.business.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.partyRole.employee.Employee;
import undecided.erp.relationship.domain.model.partyRole.employee.PagingAndSortingEmployeeRepository;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  private final PagingAndSortingEmployeeRepository pagingAndSortingEmployeeRepository;

  public EmployeeQueryImpl(PagingAndSortingEmployeeRepository pagingAndSortingEmployeeRepository) {
    this.pagingAndSortingEmployeeRepository = pagingAndSortingEmployeeRepository;
  }

  @Override
  public Optional<Employee> findByEmployeeId(Long employeeId) {
    return Optional.ofNullable(
        pagingAndSortingEmployeeRepository.findEmployeeByEmployeeId(employeeId));
  }

  @Override
  public Iterable<Employee> findByInitials(String initials) {
    return pagingAndSortingEmployeeRepository.findEmployeesByInitials(initials);
  }
}
