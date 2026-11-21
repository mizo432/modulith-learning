package undecided.association.relationshipMgmt.personRole.internal.query;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseQuery;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeEmailQuery;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
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
    // employeeIdからSimpleEmployeeを取得
    SimpleEmployee employee = employeeRepository.findById(employeeId).orElse(null);
    if (employee == null) {
      return Optional.empty();
    }

    // partyIdがnullの場合はメール解決不可
    SnowflakeId partyId = employee.getPartyId();
    if (partyId == null) {
      return Optional.empty();
    }

    // partyIdからContactUse一覧を取得
    List<ContactUse> contactUses = contactUseQuery.findByPartyId(partyId);
    if (contactUses == null || contactUses.isEmpty()) {
      return Optional.empty();
    }

    // EMAIL型のContactUseにフィルタし、whenOccuredが最新のものを選択
    return contactUses.stream()
        .filter(cu -> cu.getType() == ContactUseType.EMAIL)
        .filter(cu -> cu.getEmailAddress() != null && !cu.getEmailAddress().isEmpty())
        .max(Comparator.comparing(cu -> cu.getWhenOccured().value(), Comparator.nullsFirst(Comparator.naturalOrder())))
        .map(ContactUse::getEmailAddress)
        .map(emailAddress -> emailAddress.value())
        .filter(email -> !email.isBlank());
  }
}
