package undecided.association.relationshipMgmt.personRole.internal.query;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseQuery;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeEmailQuery;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員のメールアドレスを解決するクエリサービスです。
 */
@Service
public class EmployeeEmailQueryService implements EmployeeEmailQuery {

  private final EmployeeRepository employeeRepository;
  private final ContactUseQuery contactUseQuery;

  public EmployeeEmailQueryService(EmployeeRepository employeeRepository, ContactUseQuery contactUseQuery) {
    this.employeeRepository = employeeRepository;
    this.contactUseQuery = contactUseQuery;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<String> resolveEmailByEmployeeId(SnowflakeId employeeId) {
    return Optional.empty();
  }
}
