package undecided.supporting.snowflake.internal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SnowflakeNodeLeaseSchedulerのテスト")
class SnowflakeNodeLeaseSchedulerTest {

  private SnowflakeNodeLeaseManager leaseManager;
  private SnowflakeNodeLeaseScheduler scheduler;

  @BeforeEach
  void setUp() {
    leaseManager = mock(SnowflakeNodeLeaseManager.class);
    scheduler = new SnowflakeNodeLeaseScheduler(leaseManager);
  }

  @Nested
  @DisplayName("renewメソッドのテスト")
  class RenewMethodTest {

    @Test
    @DisplayName("更新が成功した場合、例外がスローされないこと")
    void shouldNotThrowExceptionWhenRenewSucceeds() {
      when(leaseManager.renew()).thenReturn(true);

      assertThatCode(() -> scheduler.renew()).doesNotThrowAnyException();
      verify(leaseManager).renew();
    }

    @Test
    @DisplayName("更新が失敗した場合、IllegalStateExceptionがスローされること")
    void shouldThrowIllegalStateExceptionWhenRenewFails() {
      when(leaseManager.renew()).thenReturn(false);

      assertThatThrownBy(() -> scheduler.renew())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Snowflake node lease was lost.");
      verify(leaseManager).renew();
    }
  }
}
