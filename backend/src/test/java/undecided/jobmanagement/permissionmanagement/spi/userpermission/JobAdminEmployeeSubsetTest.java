package undecided.jobmanagement.permissionmanagement.spi.userpermission;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("JobAdminEmployeeSubsetのテスト")
class JobAdminEmployeeSubsetTest {

  @Test
  @DisplayName("ofに渡した時刻がupdatedAtになる")
  void shouldSetUpdatedAtFromPassedTime() {
    LocalDateTime now = LocalDateTime.of(2026, 1, 1, 12, 0);

    JobAdminEmployeeSubset subset = JobAdminEmployeeSubset.of(SnowflakeId.of(100L), now);

    assertThat(subset.getEmployeeId()).isEqualTo(SnowflakeId.of(100L));
    assertThat(subset.getUpdatedAt()).isEqualTo(now);
  }
}
