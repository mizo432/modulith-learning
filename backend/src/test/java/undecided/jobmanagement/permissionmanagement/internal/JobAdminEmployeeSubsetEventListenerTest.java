package undecided.jobmanagement.permissionmanagement.internal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import undecided.supporting.event.AdminEmployeeChanged;
import undecided.supporting.snowflake.SnowflakeId;

class AdminEmployeeSubsetEventListenerTest {

  @Test
  void 付与を受信したときサブセットに登録される() {
    JobAdminEmployeeSubsetRepository repository = mock(JobAdminEmployeeSubsetRepository.class);
    JobAdminEmployeeSubsetEventListener listener = new JobAdminEmployeeSubsetEventListener(repository);
    SnowflakeId employeeId = SnowflakeId.of(100L);

    listener.on(new AdminEmployeeChanged(employeeId, true));

    verify(repository).save(argThat(subset -> employeeId.equals(subset.getEmployeeId())));
  }

  @Test
  void 剥離を受信したときサブセットから削除される() {
    JobAdminEmployeeSubsetRepository repository = mock(JobAdminEmployeeSubsetRepository.class);
    JobAdminEmployeeSubsetEventListener listener = new JobAdminEmployeeSubsetEventListener(repository);
    SnowflakeId employeeId = SnowflakeId.of(100L);

    listener.on(new AdminEmployeeChanged(employeeId, false));

    verify(repository).deleteById(employeeId);
  }
}
