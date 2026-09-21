package undecided.erp.shared.supporting.snowflake.internal;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfiguation {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
