package undecided.erp.shared.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
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
}
