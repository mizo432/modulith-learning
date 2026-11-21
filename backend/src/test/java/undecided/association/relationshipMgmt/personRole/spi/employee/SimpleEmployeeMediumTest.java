package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.association.relationshipMgmt.personRole.internal.repository.EmployeeRepository;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code SimpleEmployee} の永続化テストクラスです。
 * <p>
 * Issue #685: party_idの保存・読み込みを検証します。
 */
@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("SimpleEmployeeの永続化テスト")
class SimpleEmployeeMediumTest {

  @Autowired private EmployeeRepository employeeRepository;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();
  }

  // Issue #685: テスト項目23
  @Test
  @DisplayName("party_idが設定された従業員を保存・読み込みしたとき、partyIdが正しく復元される")
  void shouldRestorePartyIdAfterSaveAndLoad() {
    // Arrange
    SnowflakeId employeeId = SnowflakeId.of(1234567890L);
    SnowflakeId partyId = SnowflakeId.of(5000000001L);
    SimpleEmployee employee = SimpleEmployee.create(
        employeeId,
        EmployeeNo.of("1234567890"),
        SearchName.of("田中太郎"),
        FullName.of("田中太郎"),
        partyId);

    // Act
    employeeRepository.save(employee);
    employeeRepository.flush();
    SimpleEmployee loaded = employeeRepository.findById(employeeId).orElseThrow();

    // Assert
    assertThat(loaded.getPartyId()).isEqualTo(partyId);
  }
}
