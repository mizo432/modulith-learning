package undecided.erp.shared.supporting.snowflake.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnowflakeNodeLeaseScheduler {

  private final SnowflakeNodeLeaseManager leaseManager;

  @Scheduled(
      fixedRate = 10_000,
      initialDelay = 10_000
  )
  public void renew() {
    boolean success =
        leaseManager.renew();
    if (!success) {
      throw new IllegalStateException("Snowflake node lease was lost.");
    }
  }
}
