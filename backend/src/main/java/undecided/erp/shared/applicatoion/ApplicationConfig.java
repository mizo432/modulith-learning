package undecided.erp.shared.applicatoion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import undecided.erp.common.exception.ExceptionLogger;

@Configuration
public class ApplicationConfig {

  private static final String EXCEPTION_LOGGER_NAME = "EXCEPTION_LOGGER_NAME";

  @Bean
  public ExceptionLogger exceptionLogger() {
    return new ExceptionLogger(EXCEPTION_LOGGER_NAME);
  }
}
