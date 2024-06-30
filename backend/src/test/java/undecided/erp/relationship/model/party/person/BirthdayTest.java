package undecided.erp.relationship.model.party.person;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import undecided.erp.relationship.domain.model.party.person.Birthday;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BirthdayTest {

  @Test
  public void testDateIntegerFromDateFormatYYMMDD() {
    LocalDate localDate = LocalDate.of(2024, 1, 2);
    Birthday birthday = Birthday.of(localDate);
    assertEquals(20240102, birthday.dateInteger());
  }

  @Test
  public void testDateIntegerFromDateFormatWithSingleDigitMonthDay() {
    LocalDate localDate = LocalDate.of(2024, 9, 6);
    Birthday birthday = Birthday.of(localDate);
    assertEquals(20240906, birthday.dateInteger());
  }

  @Test
  public void testDateIntegerFromDateFormatWithZeroLeadingDigitMonthDay() {
    LocalDate localDate = LocalDate.of(2024, 04, 07);
    Birthday birthday = Birthday.of(localDate);
    assertEquals(20240407, birthday.dateInteger());
  }
}