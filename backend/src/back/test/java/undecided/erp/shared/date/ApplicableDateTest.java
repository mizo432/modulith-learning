package undecided.erp.shared.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicableDateTest {

  @Test
  void ofShouldReturnCorrectApplicableDateForValidInput() {
    LocalDate input = LocalDate.of(1999, 12, 31);
    ApplicableDate applicableDate = ApplicableDate.of(input);
    assertThat(applicableDate.toString()).isEqualTo(input.toString());
  }

  @Test
  void ofShouldThrowExceptionWhenInputIsAfterMaxDate() {
    LocalDate input = ApplicableDate.MAX_DATE.plusDays(1);
    assertThatThrownBy(() -> ApplicableDate.of(input))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Value cannot be after MAX_DATE");
  }

  @Test
  void toStringShouldReturnCorrectFormat() {
    LocalDate input = LocalDate.of(2022, 12, 31);
    ApplicableDate applicableDate = ApplicableDate.of(input);
    assertThat(applicableDate.toString()).isEqualTo("2022-12-31");
  }

  @Test
  void toStringShouldHandleMaxDateCorrectly() {
    assertThat(ApplicableDate.MAX.toString()).isEqualTo("9999-12-30");
  }

  @Nested
  class JsonProcessingTests {

    @Test
    void shouldCorrectlyProcessAndMatchApplicableDateInJson() throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      ApplicableDate sourceDate = ApplicableDate.of(LocalDate.now());
      String json = objectMapper.writeValueAsString(sourceDate);
      System.out.println(json);
      //     log.debug(json);
      ApplicableDate actualDate = objectMapper.readValue(json, ApplicableDate.class);
      assertThat(actualDate).isEqualTo(sourceDate);


    }
  }

}
