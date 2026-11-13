package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
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

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("CreateSimpleEmployeeCommandServiceの統合テスト")
class CreateSimpleEmployeeCommandServiceMediumTest {

  @Autowired private CreateSimpleEmployeeCommandService service;

  @Autowired private PersonRepository personRepository;

  @Autowired private ContactUseRepository contactUseRepository;

  @Autowired private EmployeeRepository employeeRepository;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();
    contactUseRepository.deleteAll();
    personRepository.deleteAll();
  }

  @Nested
  @DisplayName("createメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("Employee保存時にPersonとContactUseが保存され、履歴件数が増加すること")
    void shouldSavePersonAndContactUseAndIncreaseHistoryCount() {
      // Arrange
      long initialPersonCount = personRepository.count();
      long initialContactUseCount = contactUseRepository.count();
      long initialEmployeeCount = employeeRepository.count();

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

      EmployeeDto dto = new EmployeeDto(
          employeeId,
          employeeNo,
          searchName,
          fullName,
          person,
          emailAddress,
          phoneNo);

      // Act
      service.create(dto);

      // Assert - 履歴件数が増加したことを確認
      long finalPersonCount = personRepository.count();
      long finalContactUseCount = contactUseRepository.count();
      long finalEmployeeCount = employeeRepository.count();

      assertThat(finalPersonCount).isEqualTo(initialPersonCount + 1);
      assertThat(finalContactUseCount).isEqualTo(initialContactUseCount + 2);
      assertThat(finalEmployeeCount).isEqualTo(initialEmployeeCount + 1);

      // Personが正しく保存されたことを確認
      List<Person> savedPersons = personRepository.findAll();
      assertThat(savedPersons).hasSize(1);
      Person savedPerson = savedPersons.get(0);
      assertThat(savedPerson.getPartyId().value()).isEqualTo(person.getPartyId().value());
      assertThat(savedPerson.getPartyTypeCode()).isEqualTo(PartyTypeCode.of("PERSON"));

      // ContactUseが正しく保存されたことを確認（メールアドレスと電話番号の2件）
      List<ContactUse> savedContactUses = contactUseRepository.findByPartyId(person.getPartyId());
      assertThat(savedContactUses).hasSize(2);
      assertThat(savedContactUses).extracting("type").containsExactlyInAnyOrder(ContactUseType.EMAIL, ContactUseType.PHONE);

      // SimpleEmployeeが正しく保存されたことを確認
      List<SimpleEmployee> savedEmployees = employeeRepository.findAll();
      assertThat(savedEmployees).hasSize(1);
      SimpleEmployee savedEmployee = savedEmployees.get(0);
      assertThat(savedEmployee.getEmployeeId().value()).isEqualTo(employeeId.value());
      assertThat(savedEmployee.getEmployeeNo()).isEqualTo(employeeNo);
    }

    @Test
    @DisplayName("複数のEmployee保存時に履歴件数が正しく増加すること")
    void shouldIncreaseHistoryCountForMultipleEmployees() {
      // Arrange
      long initialPersonCount = personRepository.count();
      long initialContactUseCount = contactUseRepository.count();
      long initialEmployeeCount = employeeRepository.count();

      // 1人目のEmployee
      Person person1 = Person.create(
          SnowflakeId.of(5000000001L),
          PartyTypeCode.of("PERSON"),
          "P001",
          LocalDateTime.now(),
          LegalName.of("田中", "太郎"),
          DateOfBirth.of(LocalDate.of(1990, 1, 1)),
          DateOfDie.empty());
      EmployeeDto dto1 = new EmployeeDto(
          SnowflakeId.of(1234567890L),
          EmployeeNo.of("1234567890"),
          SearchName.of("田中太郎"),
          FullName.of("田中太郎"),
          person1,
          EmailAddress.of("taro.tanaka@example.com"),
          PhoneNo.of("09012345678"));

      // 2人目のEmployee
      Person person2 = Person.create(
          SnowflakeId.of(5000000002L),
          PartyTypeCode.of("PERSON"),
          "P002",
          LocalDateTime.now(),
          LegalName.of("佐藤", "花子"),
          DateOfBirth.of(LocalDate.of(1992, 5, 15)),
          DateOfDie.empty());
      EmployeeDto dto2 = new EmployeeDto(
          SnowflakeId.of(9876543210L),
          EmployeeNo.of("0987654321"),
          SearchName.of("佐藤花子"),
          FullName.of("佐藤花子"),
          person2,
          EmailAddress.of("hanako.sato@example.com"),
          PhoneNo.of("08098765432"));

      // Act
      service.create(dto1);
      service.create(dto2);

      // Assert - 履歴件数が2ずつ増加したことを確認
      long finalPersonCount = personRepository.count();
      long finalContactUseCount = contactUseRepository.count();
      long finalEmployeeCount = employeeRepository.count();

      assertThat(finalPersonCount).isEqualTo(initialPersonCount + 2);
      assertThat(finalContactUseCount).isEqualTo(initialContactUseCount + 4);
      assertThat(finalEmployeeCount).isEqualTo(initialEmployeeCount + 2);

      // 各PersonのContactUseが2件ずつ保存されたことを確認（メールアドレスと電話番号）
      List<ContactUse> contactUses1 = contactUseRepository.findByPartyId(person1.getPartyId());
      List<ContactUse> contactUses2 = contactUseRepository.findByPartyId(person2.getPartyId());
      assertThat(contactUses1).hasSize(2);
      assertThat(contactUses2).hasSize(2);
    }
  }
}
