package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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

@Tag("small")
@DisplayName("EmployeeFactoryImplの単体テスト")
class EmployeeFactoryImplTest {

  private final EmployeeFactoryImpl factory = new EmployeeFactoryImpl();

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateTest {

    @Test
    @DisplayName("EmployeeDtoからSimpleEmployeeを正しく作成すること")
    void shouldCreateSimpleEmployeeFromDto() {
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

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotNull();
      assertThat(simpleEmployee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(simpleEmployee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(simpleEmployee.getSearchName()).isEqualTo(searchName);
      assertThat(simpleEmployee.getFullName()).isEqualTo(fullName);
    }

    @Test
    @DisplayName("異なる値を持つEmployeeDtoからSimpleEmployeeを作成すること")
    void shouldCreateSimpleEmployeeWithDifferentValues() {
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

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotNull();
      assertThat(simpleEmployee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(simpleEmployee.getEmployeeNo()).isEqualTo(employeeNo);
      assertThat(simpleEmployee.getSearchName()).isEqualTo(searchName);
      assertThat(simpleEmployee.getFullName()).isEqualTo(fullName);
    }

    @Test
    @DisplayName("作成されたSimpleEmployeeは元のEmployeeDtoと独立したインスタンスであること")
    void shouldCreateIndependentInstance() {
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

      // Act
      SimpleEmployee simpleEmployee = factory.create(dto);

      // Assert
      assertThat(simpleEmployee).isNotSameAs(dto);
    }
  }
}
