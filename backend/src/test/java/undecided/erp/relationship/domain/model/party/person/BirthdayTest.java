package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BirthdayTest {

  @Test
  void testBirthdayOfNonNullValue() {
    LocalDate testDate = LocalDate.of(1995, 5, 28);
    Birthday testBirthday = Birthday.of(testDate);

    assertThat(testBirthday.value()).isEqualTo(testDate);
  }

  @Test
  void testBirthdayOfNullValue() {
    Birthday testBirthday = Birthday.of(null);

    assertThat(testBirthday.value()).isNull();
  }

  @Test
  void testReconstructNonNullValue() {
    LocalDate testDate = LocalDate.of(1995, 5, 28);
    Birthday testBirthday = Birthday.reconstruct(testDate);

    assertThat(testBirthday.value()).isEqualTo(testDate);
  }

  @Test
  void testReconstructNullValue() {
    Birthday testBirthday = Birthday.reconstruct(null);

    assertThat(testBirthday).isEqualTo(Birthday.EMPTY);
  }

  @Test
  void testDateIntegerNonNullValue() {
    LocalDate testDate = LocalDate.of(1995, 5, 28);
    Birthday testBirthday = Birthday.of(testDate);

    assertThat(testBirthday.dateInteger()).isEqualTo(19950528);
  }

  @Test
  void testDateIntegerNullValue() {
    Birthday testBirthday = Birthday.of(null);

    assertThat(testBirthday.dateInteger()).isNull();
  }

  @Test
  void testToStringNonNullValue() {
    LocalDate testDate = LocalDate.of(1995, 5, 28);
    Birthday testBirthday = Birthday.of(testDate);

    assertThat(testBirthday.toString()).isEqualTo("1995-05-28");
  }

  @Test
  void testToStringNullValue() {
    Birthday testBirthday = Birthday.of(null);

    assertThat(testBirthday.toString()).isEqualTo("null");
  }
}
