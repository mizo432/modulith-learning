package undecided.association.relationshipMgmt.personRole.internal.command;

import com.github.mizo432.shared.value.datetime.WhenOccured;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.party.repository.PersonRepository;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.CreateSimpleEmployeeCommand;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;
import undecided.association.relationshipMgmt.spi.party.contact.Address;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/** 従業員の新規作成コマンドサービスです。 EmployeeDtoからSimpleEmployeeエンティティを作成し、データベースに保存します。 */
@RequiredArgsConstructor
@Service
public class CreateSimpleEmployeeCommandService implements CreateSimpleEmployeeCommand {
  private final EmployeeFactory employeeFactory;
  private final EmployeeRepository employeeRepository;
  private final PersonRepository personRepository;
  private final ContactUseRepository contactUseRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  /**
   * 従業員情報を新規作成します。
   *
   * <p>Personを保存し、メールアドレスと電話番号のContactUseを作成し、SimpleEmployeeを保存します。
   *
   * @param employeeDto 従業員情報のDTO。employeeId、employeeNo、searchName、fullName、person、emailAddress、phoneNoを含みます。
   */
  @Override
  @Transactional
  public void create(EmployeeDto employeeDto) {
    // Personを保存
    personRepository.save(employeeDto.person());

    // メールアドレスのContactUseを作成
    SnowflakeId emailContactUseId = snowflakeIdGenerator.nextIdValue();
    ContactUse emailContactUse = ContactUse.create(
        emailContactUseId,
        employeeDto.person().getPartyId(),
        employeeDto.emailAddress(),
        PhoneNo.empty(),
        Address.empty(),
        ContactUseType.EMAIL,
        WhenOccured.current());
    contactUseRepository.save(emailContactUse);

    // 電話番号のContactUseを作成
    SnowflakeId phoneContactUseId = snowflakeIdGenerator.nextIdValue();
    ContactUse phoneContactUse = ContactUse.create(
        phoneContactUseId,
        employeeDto.person().getPartyId(),
        EmailAddress.empty(),
        employeeDto.phoneNo(),
        Address.empty(),
        ContactUseType.PHONE,
        WhenOccured.current());
    contactUseRepository.save(phoneContactUse);

    // SimpleEmployeeを作成して保存
    SimpleEmployee simpleEmployee = employeeFactory.create(employeeDto);
    employeeRepository.save(simpleEmployee);
  }
}
