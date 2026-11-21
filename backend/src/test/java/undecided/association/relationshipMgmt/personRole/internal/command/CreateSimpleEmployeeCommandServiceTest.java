package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.DateOfDie;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * {@code CreateSimpleEmployeeCommandService} の単体テストクラスです。
 * <p>
 * Issue #685: 従業員作成時にpartyIdがSimpleEmployeeに設定されることを検証します。
 */
@Tag("small")
@DisplayName("CreateSimpleEmployeeCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateSimpleEmployeeCommandServiceTest {

  @Mock private EmployeeFactory employeeFactory;
  @Mock private EmployeeRepository employeeRepository;
  @Mock private PersonRepository personRepository;
  @Mock private ContactUseRepository contactUseRepository;
  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  private CreateSimpleEmployeeCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreateSimpleEmployeeCommandService(
        employeeFactory,
        employeeRepository,
        personRepository,
        contactUseRepository,
        snowflakeIdGenerator);
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

  // Issue #685: テスト項目7
  @Test
  @DisplayName("従業員を作成したとき、FactoryがEmployeeDtoからSimpleEmployeeを作成する")
  void shouldCreateSimpleEmployeeViaFactory() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    EmployeeDto dto = dtoWithPartyId(employeeId, partyId);
    SimpleEmployee expectedEmployee = SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        partyId);
    when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(6000000001L), SnowflakeId.of(6000000002L));
    when(employeeFactory.create(dto)).thenReturn(expectedEmployee);

    // Act
    service.create(dto);

    // Assert
    verify(employeeFactory).create(dto);
  }

  // Issue #685: テスト項目8
  @Test
  @DisplayName("従業員を作成したとき、SimpleEmployeeにpartyIdが設定される")
  void shouldSetPartyIdInSimpleEmployeeWhenCreating() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    EmployeeDto dto = dtoWithPartyId(employeeId, partyId);
    SimpleEmployee expectedEmployee = SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        partyId);
    when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(6000000001L), SnowflakeId.of(6000000002L));
    when(employeeFactory.create(dto)).thenReturn(expectedEmployee);

    // Act
    service.create(dto);

    // Assert
    ArgumentCaptor<SimpleEmployee> captor = ArgumentCaptor.forClass(SimpleEmployee.class);
    verify(employeeRepository).save(captor.capture());
    assertThat(captor.getValue().getPartyId()).isEqualTo(partyId);
  }
}
