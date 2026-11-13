package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import com.github.mizo432.shared.value.datetime.WhenOccured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        employeeFactory, employeeRepository, personRepository, contactUseRepository, snowflakeIdGenerator);
  }

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateTest {

    @Test
    @DisplayName("EmployeeDtoからSimpleEmployeeを作成してRepositoryに保存すること")
    void shouldCreateAndSaveEmployee() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      Person person = Person.create(
          SnowflakeId.of(5000000001L),
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee simpleEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      when(employeeFactory.create(dto)).thenReturn(simpleEmployee);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(
          SnowflakeId.of(9000000001L), // emailContactUseId
          SnowflakeId.of(9000000002L)); // phoneContactUseId

      // Act
      service.create(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository, times(2)).save(any());
      verify(employeeFactory).create(dto);
      verify(employeeRepository).save(simpleEmployee);
    }

    @Test
    @DisplayName("Factoryで作成されたSimpleEmployeeがRepositoryに保存されること")
    void shouldSaveEmployeeCreatedByIdFactory() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(9876543210L);
      EmployeeNo employeeNo = EmployeeNo.of("0987654321");
      SearchName searchName = SearchName.of("佐藤花子");
      FullName fullName = FullName.of("佐藤花子");
      Person person = Person.create(
          SnowflakeId.of(5000000002L),
          PartyTypeCode.of("PERSON"),
          "P002",
          LocalDateTime.now(),
          LegalName.of("佐藤", "花子"),
          DateOfBirth.of(LocalDate.of(1992, 5, 15)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("hanako.sato@example.com");
      PhoneNo phoneNo = PhoneNo.of("08098765432");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee simpleEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      when(employeeFactory.create(dto)).thenReturn(simpleEmployee);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(
          SnowflakeId.of(9000000005L), // emailContactUseId
          SnowflakeId.of(9000000006L)); // phoneContactUseId

      // Act
      service.create(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository, times(2)).save(any());
      verify(employeeRepository).save(any(SimpleEmployee.class));
    }

    @Test
    @DisplayName("employeeDtoがnullの場合IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.create(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("employeeDto must not be null");
    }

    @Test
    @DisplayName("ContactUseに正しいメールアドレスと電話番号が設定されること")
    void shouldSaveContactUseWithCorrectEmailAddressAndPhoneNo() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1111111111L);
      EmployeeNo employeeNo = EmployeeNo.of("1111111111");
      SearchName searchName = SearchName.of("テスト太郎");
      FullName fullName = FullName.of("テスト太郎");
      Person person = Person.create(
          SnowflakeId.of(5000000003L),
          PartyTypeCode.of("PERSON"),
          "P003",
          LocalDateTime.now(),
          LegalName.of("テスト", "太郎"),
          DateOfBirth.of(LocalDate.of(1995, 3, 20)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("test@example.com");
      PhoneNo phoneNo = PhoneNo.of("07012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee simpleEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      when(employeeFactory.create(dto)).thenReturn(simpleEmployee);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(
          SnowflakeId.of(9000000010L), // emailContactUseId
          SnowflakeId.of(9000000011L)); // phoneContactUseId

      // Act
      service.create(dto);

      // Assert
      verify(personRepository).save(person);

      ArgumentCaptor<undecided.association.relationshipMgmt.spi.party.contact.ContactUse> contactUseCaptor = forClass(undecided.association.relationshipMgmt.spi.party.contact.ContactUse.class);
      verify(contactUseRepository, times(2)).save(contactUseCaptor.capture());

      java.util.List<undecided.association.relationshipMgmt.spi.party.contact.ContactUse> capturedContactUses = contactUseCaptor.getAllValues();
      assertThat(capturedContactUses).hasSize(2);

      undecided.association.relationshipMgmt.spi.party.contact.ContactUse emailContactUse = capturedContactUses.get(0);
      assertThat(emailContactUse.getType()).isEqualTo(undecided.association.relationshipMgmt.spi.party.contact.ContactUseType.EMAIL);
      assertThat(emailContactUse.getEmailAddress().value()).isEqualTo("test@example.com");
      assertThat(emailContactUse.getPhoneNo()).isEqualTo(PhoneNo.empty());

      undecided.association.relationshipMgmt.spi.party.contact.ContactUse phoneContactUse = capturedContactUses.get(1);
      assertThat(phoneContactUse.getType()).isEqualTo(undecided.association.relationshipMgmt.spi.party.contact.ContactUseType.PHONE);
      assertThat(phoneContactUse.getPhoneNo().value()).isEqualTo("07012345678");
      assertThat(phoneContactUse.getEmailAddress()).isEqualTo(EmailAddress.empty());

      verify(employeeRepository).save(simpleEmployee);
    }
  }
}
