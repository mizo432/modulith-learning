package undecided.erp.relMgmt.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.date.Today;

class AgeDateStdTest {

  /**
   * Method under test: {@link AgeDateStd#calculateFrom(Birthday, Today)}
   */
  @Test
  void calculateFrom_shouldReturnCorrectAge() {

    // Arrange
    LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
    Birthday birthday = Birthday.of(dateOfBirth);
    LocalDate currentDate = LocalDate.of(2024, 6, 1);
    Today today = Today.of(currentDate);
    // Act
    AgeDateStd actualCalculateFromResult = AgeDateStd.calculateFrom(birthday, today);

    // Assert
    // Assert
    assertThat(actualCalculateFromResult).isEqualTo(new AgeDateStd(34));
  }

  @Test
  void calculateFrom_shouldThrowExceptionForInvalidInputs() {
    // Arrange
    Birthday birthday = null;
    Today today = null;

    // Assert
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(
        () -> AgeDateStd.calculateFrom(birthday, today));

  }
}
