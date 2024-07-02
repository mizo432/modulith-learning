package undecided.erp.relationship.domain.model.orgRole.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CompanyCodeTest {

  @Nested
  class OfTest {

    @Test
    void shouldReturnCompanyCodeWhenInputIsValid() {
      String value = "123";
      CompanyCode result = CompanyCode.of(value);
      assertEquals(value, result.toString());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInputIsNonNumeric() {
      String value = "ABC";
      assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInputLengthIsInvalid() {
      String value = "1234";
      assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
    }

  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenInputIsEmpty() {
    String value = "";
    assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
  }

  @Test
  void shouldThrowNullPointerExceptionWhenInputIsNull() {
    String value = null;
    assertThrows(IllegalArgumentException.class, () -> CompanyCode.of(value));
  }
}
