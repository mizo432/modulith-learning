package undecided.association.relationshipMgmt.personRole.internal.command;

import com.github.mizo432.shared.common.precondition.ObjectPrecondition;
import com.github.mizo432.shared.exception.NotFoundBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.party.repository.PersonRepository;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.UpdateSimpleEmployeeCommand;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/** 従業員更新コマンドサービスです。 EmployeeDtoからPerson、ContactUse（メールアドレス・電話番号）、SimpleEmployeeエンティティを更新し、データベースに保存します。 */
@RequiredArgsConstructor
@Service
public class UpdateSimpleEmployeeCommandService implements UpdateSimpleEmployeeCommand {
  private final EmployeeRepository employeeRepository;
  private final PersonRepository personRepository;
  private final ContactUseRepository contactUseRepository;

  /**
   * 従業員情報を更新します。
   *
   * <p>Personを更新し、メールアドレスと電話番号のContactUseを更新し、SimpleEmployeeを更新します。
   *
   * @param employeeDto 従業員情報のDTO。employeeId、employeeNo、searchName、fullName、person、emailAddress、phoneNoを含みます。
   */
  @Override
  @Transactional
  public void update(EmployeeDto employeeDto) {
    ObjectPrecondition.checkNotNull(employeeDto, () -> new IllegalArgumentException("employeeDto must not be null"));

    // SimpleEmployeeを検索
    SimpleEmployee simpleEmployee = employeeRepository.findById(employeeDto.employeeId())
        .orElseThrow(() -> new NotFoundBusinessException("Employee", "employeeId", String.valueOf(employeeDto.employeeId())));

    // Personを検索して更新
    SnowflakeId partyId = employeeDto.person().getPartyId();
    Person person = personRepository.findById(partyId)
        .orElseThrow(() -> new NotFoundBusinessException("Person", "partyId", String.valueOf(partyId)));
    person.update(employeeDto.person().getLegalName(), employeeDto.person().getDateOfBirth(), employeeDto.person().getDateOfDie());
    personRepository.save(person);

    // メールアドレスのContactUseを更新
    ContactUse emailContactUse = contactUseRepository.findByPartyIdAndType(employeeDto.person().getPartyId(), ContactUseType.EMAIL)
        .orElseThrow(() -> new NotFoundBusinessException("ContactUse", "partyId+type", employeeDto.person().getPartyId() + "+EMAIL"));
    emailContactUse.updateEmailAddress(employeeDto.emailAddress());
    contactUseRepository.save(emailContactUse);

    // 電話番号のContactUseを更新
    ContactUse phoneContactUse = contactUseRepository.findByPartyIdAndType(employeeDto.person().getPartyId(), ContactUseType.PHONE)
        .orElseThrow(() -> new NotFoundBusinessException("ContactUse", "partyId+type", employeeDto.person().getPartyId() + "+PHONE"));
    phoneContactUse.updatePhoneNo(employeeDto.phoneNo());
    contactUseRepository.save(phoneContactUse);

    // SimpleEmployeeを更新
    simpleEmployee.update(employeeDto.employeeNo(), employeeDto.searchName(), employeeDto.fullName());
    employeeRepository.save(simpleEmployee);
  }
}
