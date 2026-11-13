package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.DateOfDie;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

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
    service = new UpdateSimpleEmployeeCommandService(employeeRepository, personRepository, contactUseRepository);
  }

  @Nested
  @DisplayName("update メソッドのテスト")
  class UpdateTest {

    @Test
    @DisplayName("EmployeeDtoからSimpleEmployeeを更新してRepositoryに保存すること")
    void shouldUpdateAndSaveEmployee() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, EmployeeNo.of("0000000000"), SearchName.of("旧名称"), FullName.of("旧氏名"));

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, EmailAddress.of("old@example.com"), PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), PhoneNo.of("0000000000"), ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      // 更新後の値を確認
      assertThat(existingEmployee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(existingEmployee.getSearchName()).isEqualTo(searchName);
      assertThat(existingEmployee.getFullName()).isEqualTo(fullName);
      assertThat(emailContactUse.getEmailAddress()).isEqualTo(emailAddress);
      assertThat(phoneContactUse.getPhoneNo()).isEqualTo(phoneNo);
    }

    @Test
    @DisplayName("employeeDtoがnullの場合IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.update(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("employeeDto must not be null");
    }

    @Test
    @DisplayName("従業員が見つからない場合IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionWhenEmployeeNotFound() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(9999999999L);
      EmployeeNo employeeNo = EmployeeNo.of("9999999999");
      SearchName searchName = SearchName.of("存在しない");
      FullName fullName = FullName.of("存在しない");
      Person person = Person.create(
          SnowflakeId.of(5000000099L),
          PartyTypeCode.of("PERSON"),
          "P099",
          LocalDateTime.now(),
          LegalName.of("存在", "しない"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, EmailAddress.empty(), PhoneNo.empty());

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.update(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Employee not found: " + employeeId);
    }

    @Test
    @DisplayName("メールアドレスのContactUseが見つからない場合IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionWhenEmailContactUseNotFound() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmployeeDto dto = new EmployeeDto(
          employeeId,
          EmployeeNo.of("1234567890"),
          SearchName.of("田中太郎"),
          FullName.of("田中太郎"),
          person,
          EmailAddress.of("taro@example.com"),
          PhoneNo.of("09012345678"));

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, EmployeeNo.of("1234567890"), SearchName.of("田中太郎"), FullName.of("田中太郎"));

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.update(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Email ContactUse not found for party: " + partyId);
    }

    @Test
    @DisplayName("電話番号のContactUseが見つからない場合IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionWhenPhoneContactUseNotFound() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmployeeDto dto = new EmployeeDto(
          employeeId,
          EmployeeNo.of("1234567890"),
          SearchName.of("田中太郎"),
          FullName.of("田中太郎"),
          person,
          EmailAddress.of("taro@example.com"),
          PhoneNo.of("09012345678"));

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, EmployeeNo.of("1234567890"), SearchName.of("田中太郎"), FullName.of("田中太郎"));

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, EmailAddress.of("old@example.com"), PhoneNo.empty(), ContactUseType.EMAIL, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.update(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Phone ContactUse not found for party: " + partyId);
    }

    @Test
    @DisplayName("何も変わらない場合でも更新処理が正しく実行されること")
    void shouldUpdateWhenNothingChanges() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, emailAddress, PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), phoneNo, ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);
    }

    @Test
    @DisplayName("電話番号が変わった場合新しい電話番号が保存されること")
    void shouldUpdateWhenPhoneNoChanges() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo newPhoneNo = PhoneNo.of("08012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, newPhoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, emailAddress, PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), PhoneNo.of("09012345678"), ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(phoneContactUse.getPhoneNo()).isEqualTo(newPhoneNo);
    }

    @Test
    @DisplayName("メールアドレスが変わった場合新しいメールアドレスが保存されること")
    void shouldUpdateWhenEmailAddressChanges() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress newEmailAddress = EmailAddress.of("new.taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, newEmailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, EmailAddress.of("old.taro.tanaka@example.com"), PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), phoneNo, ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(emailContactUse.getEmailAddress()).isEqualTo(newEmailAddress);
    }

    @Test
    @DisplayName("電話とメールの両方が変わった場合両方とも新しい値が保存されること")
    void shouldUpdateWhenBothPhoneAndEmailChange() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress newEmailAddress = EmailAddress.of("new.taro.tanaka@example.com");
      PhoneNo newPhoneNo = PhoneNo.of("08012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, newEmailAddress, newPhoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, EmailAddress.of("old.taro.tanaka@example.com"), PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), PhoneNo.of("09012345678"), ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(emailContactUse.getEmailAddress()).isEqualTo(newEmailAddress);
      assertThat(phoneContactUse.getPhoneNo()).isEqualTo(newPhoneNo);
    }

    @Test
    @DisplayName("結婚してラストネームが変わった場合新しい氏名が保存されること")
    void shouldUpdateWhenLastNameChangesDueToMarriage() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName newSearchName = SearchName.of("山田太郎");
      FullName newFullName = FullName.of("山田太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("山田", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, newSearchName, newFullName, person, emailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, SearchName.of("田中太郎"), FullName.of("田中太郎"));

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, emailAddress, PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), phoneNo, ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(existingEmployee.getSearchName()).isEqualTo(newSearchName);
      assertThat(existingEmployee.getFullName()).isEqualTo(newFullName);
      assertThat(person.getLegalName()).isEqualTo(LegalName.of("山田", "太郎"));
    }

    @Test
    @DisplayName("誕生日を設定した場合新しい誕生日が保存されること")
    void shouldUpdateWhenDateOfBirthIsSet() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      LocalDate newDateOfBirth = LocalDate.of(1990, 5, 15);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(newDateOfBirth),
          DateOfDie.empty());

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, emailAddress, PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), phoneNo, ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(person.getDateOfBirth()).isEqualTo(DateOfBirth.of(newDateOfBirth));
    }

    @Test
    @DisplayName("死亡日を設定した場合新しい死亡日が保存されること")
    void shouldUpdateWhenDateOfDieIsSet() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1234567890L);
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");
      SearchName searchName = SearchName.of("田中太郎");
      FullName fullName = FullName.of("田中太郎");
      SnowflakeId partyId = SnowflakeId.of(5000000001L);
      LocalDate newDateOfDie = LocalDate.of(2025, 12, 31);
      Person person = Person.create(
          partyId,
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.of(newDateOfDie));

      EmailAddress emailAddress = EmailAddress.of("taro.tanaka@example.com");
      PhoneNo phoneNo = PhoneNo.of("09012345678");

      EmployeeDto dto = new EmployeeDto(employeeId, employeeNo, searchName, fullName, person, emailAddress, phoneNo);

      SimpleEmployee existingEmployee = SimpleEmployee.create(employeeId, employeeNo, searchName, fullName);

      ContactUse emailContactUse = ContactUse.create(
          SnowflakeId.of(9000000001L), partyId, emailAddress, PhoneNo.empty(), ContactUseType.EMAIL, null);
      ContactUse phoneContactUse = ContactUse.create(
          SnowflakeId.of(9000000002L), partyId, EmailAddress.empty(), phoneNo, ContactUseType.PHONE, null);

      when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.EMAIL)).thenReturn(Optional.of(emailContactUse));
      when(contactUseRepository.findByPartyIdAndType(partyId, ContactUseType.PHONE)).thenReturn(Optional.of(phoneContactUse));

      // Act
      service.update(dto);

      // Assert
      verify(personRepository).save(person);
      verify(contactUseRepository).save(emailContactUse);
      verify(contactUseRepository).save(phoneContactUse);
      verify(employeeRepository).save(existingEmployee);

      assertThat(person.getDateOfDie()).isEqualTo(DateOfDie.of(newDateOfDie));
    }
  }
}
