package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.party.repository.PersonRepository;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.personRole.spi.employee.dto.EmployeeDto;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.DateOfDie;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.WhenOccured;

/**
 * {@code UpdateSimpleEmployeeCommandService} の単体テストクラスです。
 * <p>
 * Issue #685: 従業員更新時にpartyIdがSimpleEmployeeに設定されることを検証します。
 */
@Tag("small")
@DisplayName("UpdateSimpleEmployeeCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateSimpleEmployeeCommandServiceTest {

  @Mock private EmployeeRepository employeeRepository;
  @Mock private PersonRepository personRepository;
  @Mock private ContactUseRepository contactUseRepository;

  private UpdateSimpleEmployeeCommandService service;

  @BeforeEach
  void setUp() {
    service = new UpdateSimpleEmployeeCommandService(
        employeeRepository,
        personRepository,
        contactUseRepository);
  }

  private Person personWithPartyId(SnowflakeId partyId) {
    return Person.create(
        partyId,
        PartyTypeCode.of("PERSON"),
        "P001",
        LocalDateTime.now(),
        LegalName.of("田中", "太郎"),
        DateOfBirth.of(LocalDate.of(1990, 1, 1)),
        DateOfDie.empty());
  }

  private EmployeeDto dtoWithPartyId(SnowflakeId employeeId, SnowflakeId partyId) {
    return new EmployeeDto(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        personWithPartyId(partyId),
        EmailAddress.of("taro@example.com"),
        PhoneNo.of("09012345678"));
  }

  private SimpleEmployee existingEmployee(SnowflakeId employeeId, SnowflakeId partyId) {
    return SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("0000000000"),
        SearchName.of("旧名称"),
        FullName.of("旧氏名"),
        partyId);
  }

  private ContactUse emailContactUse(SnowflakeId partyId) {
    return ContactUse.create(
        SnowflakeId.of(6000000001L),
        partyId,
        EmailAddress.of("old@example.com"),
        PhoneNo.empty(),
        ContactUseType.EMAIL,
        WhenOccured.current());
  }

  private ContactUse phoneContactUse(SnowflakeId partyId) {
    return ContactUse.create(
        SnowflakeId.of(6000000002L),
        partyId,
        EmailAddress.empty(),
        PhoneNo.of("09000000000"),
        ContactUseType.PHONE,
        WhenOccured.current());
  }

  // Issue #685: テスト項目9
  @Test
  @DisplayName("従業員を更新したとき、SimpleEmployeeのpartyIdが更新される")
  void shouldUpdatePartyIdWhenUpdatingEmployee() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId oldPartyId = SnowflakeId.of(5000000001L);
    SnowflakeId newPartyId = SnowflakeId.of(5000000002L);
    EmployeeDto dto = dtoWithPartyId(employeeId, newPartyId);
    SimpleEmployee existing = existingEmployee(employeeId, oldPartyId);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existing));
    when(personRepository.findById(newPartyId)).thenReturn(Optional.of(personWithPartyId(newPartyId)));
    when(contactUseRepository.findByPartyIdAndType(newPartyId, ContactUseType.EMAIL))
        .thenReturn(Optional.of(emailContactUse(newPartyId)));
    when(contactUseRepository.findByPartyIdAndType(newPartyId, ContactUseType.PHONE))
        .thenReturn(Optional.of(phoneContactUse(newPartyId)));

    // Act
    service.update(dto);

    // Assert
    ArgumentCaptor<SimpleEmployee> captor = ArgumentCaptor.forClass(SimpleEmployee.class);
    verify(employeeRepository).save(captor.capture());
    assertThat(captor.getValue().getPartyId()).isEqualTo(newPartyId);
  }

  // Issue #685: テスト項目10
  @Test
  @DisplayName("従業員を更新したとき、既存のpartyIdが新しい値で上書きされる")
  void shouldOverwriteExistingPartyIdWithNewValue() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId oldPartyId = SnowflakeId.of(5000000001L);
    SnowflakeId newPartyId = SnowflakeId.of(5000000002L);
    EmployeeDto dto = dtoWithPartyId(employeeId, newPartyId);
    SimpleEmployee existing = existingEmployee(employeeId, oldPartyId);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existing));
    when(personRepository.findById(newPartyId)).thenReturn(Optional.of(personWithPartyId(newPartyId)));
    when(contactUseRepository.findByPartyIdAndType(newPartyId, ContactUseType.EMAIL))
        .thenReturn(Optional.of(emailContactUse(newPartyId)));
    when(contactUseRepository.findByPartyIdAndType(newPartyId, ContactUseType.PHONE))
        .thenReturn(Optional.of(phoneContactUse(newPartyId)));

    // Act
    service.update(dto);

    // Assert
    ArgumentCaptor<SimpleEmployee> captor = ArgumentCaptor.forClass(SimpleEmployee.class);
    verify(employeeRepository).save(captor.capture());
    assertThat(captor.getValue().getPartyId()).isNotEqualTo(oldPartyId);
    assertThat(captor.getValue().getPartyId()).isEqualTo(newPartyId);
  }
}
