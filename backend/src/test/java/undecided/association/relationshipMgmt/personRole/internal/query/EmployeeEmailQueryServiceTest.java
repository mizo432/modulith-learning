package undecided.association.relationshipMgmt.personRole.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeNo;
import undecided.association.relationshipMgmt.personRole.spi.employee.FullName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SearchName;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseQuery;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.EmailAddress;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.value.datetime.WhenOccured;

/**
 * {@code EmployeeEmailQueryService} の単体テストクラスです。
 * <p>
 * Issue #685: 従業員IDからメールアドレスを解決する処理を検証します。
 */
@Tag("small")
@DisplayName("EmployeeEmailQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class EmployeeEmailQueryServiceTest {

  @Mock private EmployeeRepository employeeRepository;
  @Mock private ContactUseQuery contactUseQuery;

  private EmployeeEmailQueryService service;

  @BeforeEach
  void setUp() {
    service = new EmployeeEmailQueryService(employeeRepository, contactUseQuery);
  }

  private SimpleEmployee employeeWithPartyId(SnowflakeId employeeId, SnowflakeId partyId) {
    return SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        partyId);
  }

  private SimpleEmployee employeeWithoutPartyId(SnowflakeId employeeId) {
    return SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        null);
  }

  private WhenOccured whenOccuredOf(LocalDateTime dateTime) throws Exception {
    Constructor<WhenOccured> ctor = WhenOccured.class.getDeclaredConstructor(LocalDateTime.class);
    ctor.setAccessible(true);
    return ctor.newInstance(dateTime);
  }

  private ContactUse emailContactUse(SnowflakeId partyId, String email, LocalDateTime when) throws Exception {
    return ContactUse.create(
        SnowflakeId.of(6000000001L),
        partyId,
        EmailAddress.of(email),
        PhoneNo.empty(),
        ContactUseType.EMAIL,
        whenOccuredOf(when));
  }

  private ContactUse phoneContactUse(SnowflakeId partyId) {
    return ContactUse.create(
        SnowflakeId.of(6000000002L),
        partyId,
        EmailAddress.empty(),
        PhoneNo.of("09012345678"),
        ContactUseType.PHONE,
        WhenOccured.current());
  }

  // Issue #685: テスト項目11
  @Test
  @DisplayName("partyIdとEMAIL型のContactUseが存在する従業員でメール解決したとき、メールアドレスが返される")
  void shouldResolveEmailWhenPartyIdAndEmailContactUseExist() throws Exception {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    SimpleEmployee employee = employeeWithPartyId(employeeId, partyId);
    ContactUse emailContact = emailContactUse(partyId, "taro@example.com", LocalDateTime.of(2025, 6, 1, 10, 0));

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(contactUseQuery.findByPartyId(partyId)).thenReturn(List.of(emailContact));

    // Act
    Optional<String> result = service.resolveEmailByEmployeeId(employeeId);

    // Assert
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("taro@example.com");
  }

  // Issue #685: テスト項目12
  @Test
  @DisplayName("複数のEMAIL型ContactUseが存在する従業員でメール解決したとき、whenOccuredが最新のものが返される")
  void shouldReturnLatestEmailWhenMultipleEmailContactUsesExist() throws Exception {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    SimpleEmployee employee = employeeWithPartyId(employeeId, partyId);
    ContactUse oldEmail = emailContactUse(partyId, "old@example.com", LocalDateTime.of(2025, 1, 1, 10, 0));
    ContactUse newEmail = ContactUse.create(
        SnowflakeId.of(6000000003L),
        partyId,
        EmailAddress.of("new@example.com"),
        PhoneNo.empty(),
        ContactUseType.EMAIL,
        whenOccuredOf(LocalDateTime.of(2025, 6, 1, 10, 0)));

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(contactUseQuery.findByPartyId(partyId)).thenReturn(List.of(oldEmail, newEmail));

    // Act
    Optional<String> result = service.resolveEmailByEmployeeId(employeeId);

    // Assert
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("new@example.com");
  }

  // Issue #685: テスト項目13
  @Test
  @DisplayName("partyIdがnullの従業員でメール解決したとき、Optional.empty()が返される")
  void shouldReturnEmptyWhenPartyIdIsNull() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SimpleEmployee employee = employeeWithoutPartyId(employeeId);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

    // Act
    Optional<String> result = service.resolveEmailByEmployeeId(employeeId);

    // Assert
    assertThat(result).isEmpty();
  }

  // Issue #685: テスト項目14
  @Test
  @DisplayName("EMAIL型のContactUseが存在しない従業員でメール解決したとき、Optional.empty()が返される")
  void shouldReturnEmptyWhenNoEmailContactUseExists() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    SimpleEmployee employee = employeeWithPartyId(employeeId, partyId);
    ContactUse phoneContact = phoneContactUse(partyId);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(contactUseQuery.findByPartyId(partyId)).thenReturn(List.of(phoneContact));

    // Act
    Optional<String> result = service.resolveEmailByEmployeeId(employeeId);

    // Assert
    assertThat(result).isEmpty();
  }

  // Issue #685: テスト項目15
  @Test
  @DisplayName("存在しないemployeeIdでメール解決したとき、Optional.empty()が返される")
  void shouldReturnEmptyWhenEmployeeIdNotFound() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(4000000001L);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

    // Act
    Optional<String> result = service.resolveEmailByEmployeeId(employeeId);

    // Assert
    assertThat(result).isEmpty();
  }
}
