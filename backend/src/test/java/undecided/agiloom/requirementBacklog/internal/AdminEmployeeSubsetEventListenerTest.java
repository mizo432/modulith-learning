package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.supporting.event.AdminEmployeeChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("AdminEmployeeSubsetEventListenerの単体テスト")
@ExtendWith(MockitoExtension.class)
class AdminEmployeeSubsetEventListenerTest {

  private static final long EMPLOYEE_ID_VALUE = 100L;
  private static final SnowflakeId EMPLOYEE_ID = SnowflakeId.of(EMPLOYEE_ID_VALUE);

  @Mock private AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  private AdminEmployeeSubsetEventListener listener;

  @BeforeEach
  void setUp() {
    listener = new AdminEmployeeSubsetEventListener(adminEmployeeSubsetRepository, Clock.systemDefaultZone());
  }

  @Test
  @DisplayName("isAdmin=trueのイベントを受信するとサブセットが保存されること")
  void shouldSaveWhenIsAdminTrue() {
    // Arrange
    AdminEmployeeChanged event = new AdminEmployeeChanged(EMPLOYEE_ID, true);

    // Act
    listener.on(event);

    // Assert
    verify(adminEmployeeSubsetRepository).save(any(AdminEmployeeSubset.class));
  }

  @Test
  @DisplayName("isAdmin=falseのイベントを受信するとサブセットが削除されること")
  void shouldDeleteWhenIsAdminFalse() {
    // Arrange
    AdminEmployeeChanged event = new AdminEmployeeChanged(EMPLOYEE_ID, false);

    // Act
    listener.on(event);

    // Assert
    verify(adminEmployeeSubsetRepository).deleteById(EMPLOYEE_ID);
  }
}
