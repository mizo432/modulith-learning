package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Month;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("会計期間")
@Disabled()
class AccountDatePeriodTest {

  @Nested
  @DisplayName("performFrom method tests")
  class PerformFromTest {
    // Test methods to be added here

    @Test
    @DisplayName("Test when target month is before begin month")
    void testPerformFromWhenTargetMonthIsBeforeBeginMonth() {
      // Define some mock data
      AccountPeriodConfig configMock = mock(AccountPeriodConfig.class);
      ApplicableDate targetDate = ApplicableDate.now();
      ApplicableMonth targetMonth = ApplicableMonth.of(Month.FEBRUARY);
      ApplicableMonth beforeTargetMonth = ApplicableMonth.of(Month.JANUARY);

      // When methods are called on the mock, return this data
      when(configMock.beginMonth()).thenReturn(beforeTargetMonth);
      when(targetDate.getMonth()).thenReturn(targetMonth);

      // Call the function we're testing
      AccountDatePeriod result = AccountDatePeriod.performFrom(configMock, targetDate);

      // Check that the results are as we expect
      assertNotNull(result);
      assertTrue(result.getBeginDate().getValue().getMonth()
          == beforeTargetMonth.getValue());
      assertTrue(result.getEndDate().getValue().getMonth() == targetMonth.getValue());
    }

    @Test
    @DisplayName("Test when target month is not before begin month")
    void testPerformFromWhenTargetMonthIsNotBeforeBeginMonth() {
      // Define some mock data
      AccountPeriodConfig configMock = mock(AccountPeriodConfig.class);
      ApplicableDate targetDate = ApplicableDate.now();
      ApplicableMonth targetMonth = ApplicableMonth.of(Month.JANUARY);
      ApplicableMonth afterTargetMonth = ApplicableMonth.of(Month.FEBRUARY);

      // When methods are called on the mock, return this data
      when(configMock.beginMonth()).thenReturn(afterTargetMonth);
      when(targetDate.getMonth()).thenReturn(targetMonth);

      // Call the function we're testing
      AccountDatePeriod result = AccountDatePeriod.performFrom(configMock, targetDate);

      // Check that the results are as we expect
      assertNotNull(result);
      System.out.println(result);
      assertTrue(
          result.getBeginDate().getValue().getMonth() == targetMonth.getValue());
      assertTrue(
          result.getEndDate().getValue().getMonth() == afterTargetMonth.getValue());
    }
  }

}
