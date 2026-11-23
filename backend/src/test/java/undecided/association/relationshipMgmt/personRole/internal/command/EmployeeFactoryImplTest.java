package undecided.association.relationshipMgmt.personRole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
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

/**
 * {@code EmployeeFactoryImpl} の単体テストクラスです。
 * <p>
 * Issue #685: EmployeeDtoからSimpleEmployeeを作成する際にpartyIdが正しく設定されることを検証します。
 */
@Tag("small")
@DisplayName("EmployeeFactoryImplの単体テスト")
class EmployeeFactoryImplTest {

  private final EmployeeFactoryImpl factory = new EmployeeFactoryImpl();

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

  // Issue #685: テスト項目5
  @Test
  @DisplayName("partyIdを持つEmployeeDtoからSimpleEmployeeを作成したとき、partyIdが設定される")
  void shouldSetPartyIdWhenCreatingFromDtoWithPartyId() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    EmployeeDto dto = dtoWithPartyId(employeeId, partyId);

    // Act
    SimpleEmployee employee = factory.create(dto);

    // Assert
    assertThat(employee.getPartyId()).isEqualTo(partyId);
  }

  // Issue #685: テスト項目6
  @Test
  @DisplayName("partyIdがnullのEmployeeDtoから作成したとき、partyIdがnullとなる")
  void shouldSetPartyIdToNullWhenCreatingFromDtoWithNullPartyId() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    // partyIdがnullのPersonを作成（initPartyでnullを渡す）
    Person personWithoutPartyId = new Person() {
      @Override
      public SnowflakeId getPartyId() {
        return null;
      }
    };
    EmployeeDto dto = new EmployeeDto(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        personWithoutPartyId,
        EmailAddress.of("taro@example.com"),
        PhoneNo.of("09012345678"));

    // Act
    SimpleEmployee employee = factory.create(dto);

    // Assert
    assertThat(employee.getPartyId()).isNull();
  }
}
