package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BirthdayTest {

  @Nested
  @DisplayName("Birthdayクラスのofメソッドについて")
  class OfTest {

    @DisplayName("NonNullの値が引数の場合")
    @Test
    void shouldReturnBirthdayObject() {
      LocalDate testDate = LocalDate.of(1995, 5, 28);
      Birthday testBirthday = Birthday.of(testDate);

      assertThat(testBirthday.getValue()).isEqualTo(testDate);
    }

    @DisplayName("Nullが引数の場合")
    @Test
    void shouldReturnBirthdayObjectWithNullValue() {
      Birthday testBirthday = Birthday.of(null);

      assertThat(testBirthday.getValue()).isNull();
    }
  }

  @Nested
  @DisplayName("BirthdayクラスのdateIntegerメソッドについて")
  class DateIntegerTest {

    @DisplayName("NonNullの値が引数の場合")
    @Test
    void shouldReturnDateIntegerValue() {
      LocalDate testDate = LocalDate.of(2024, 11, 11);
      Birthday testBirthday = Birthday.of(testDate);

      assertThat(testBirthday.dateInteger()).isEqualTo(20241111);
    }

    @DisplayName("Nullが引数の場合")
    @Test
    void shouldReturnZeroWhenNullValue() {
      Birthday testBirthday = Birthday.of(null);

      assertThatThrownBy(() -> {
        testBirthday.dateInteger();

      }).isInstanceOf(IllegalStateException.class);

    }
  }

  @Nested
  @DisplayName("BirthdayクラスのtoStringメソッドについて")
  class ToStringTest {

    @DisplayName("NonNullの値が引数の場合")
    @Test
    void shouldReturnStringValue() {
      LocalDate testDate = LocalDate.of(1995, 5, 28);
      Birthday testBirthday = Birthday.of(testDate);

      assertThat(testBirthday.toString()).isEqualTo("1995-05-28");
    }

    @DisplayName("Nullが引数の場合")
    @Test
    void shouldReturnNullStringValue() {
      Birthday testBirthday = Birthday.of(null);

      assertThat(testBirthday.toString()).isEqualTo("null");
    }
  }
}
