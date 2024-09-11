package undecided.erp.shared.date;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class DateIntervalTest {

  @Test
  void shouldCreateWithBeginAndEndDate() {
    ApplicableDate beginDate = ApplicableDate.of(LocalDate.now());
    ApplicableDate endDate = ApplicableDate.of(LocalDate.of(3000, 1, 1));
    DateInterval interval = DateInterval.create(beginDate, endDate);
    assertThat(interval).isNotNull();
    assertThat(interval.getBeginDate()).isEqualTo(beginDate);
    assertThat(interval.getEndDate()).isEqualTo(endDate);
  }

  @Test
  void shouldCreateWithBeginDateOnly() {
    ApplicableDate beginDate = ApplicableDate.of(LocalDate.now());
    DateInterval interval = DateInterval.create(beginDate);
    assertThat(interval).isNotNull();
    assertThat(interval.getBeginDate()).isEqualTo(beginDate);
    assertThat(interval.getEndDate()).isEqualTo(ApplicableDate.MAX);
  }

  @Nested
  class JsonProcessingTests {

    @Test
    void shouldCorrectlyProcessAndMatchApplicableDateInJson() throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      DateInterval sourceDateInterval = DateInterval.create(ApplicableDate.now(),
          ApplicableDate.now().plusDays(1));
      String json = objectMapper.writeValueAsString(sourceDateInterval);
      System.out.println(json);
      //     log.debug(json);
      DateInterval actualDateInterval = objectMapper.readValue(json, DateInterval.class);
      assertThat(actualDateInterval).isEqualTo(sourceDateInterval);


    }
  }
}
