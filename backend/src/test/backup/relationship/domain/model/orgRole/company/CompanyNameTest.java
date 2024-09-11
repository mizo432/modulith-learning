package undecided.erp.relationship.domain.model.orgRole.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CompanyNameTest {

  @Nested
  class OfTest {

    @Test
    void shouldReturnCompanyNameWithNonNullValue() {
      String testValue = "TestCompanyName";
      CompanyName companyName = CompanyName.of(testValue);
      assertEquals(testValue, companyName.getValue());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNullValue() {
      assertThrows(IllegalArgumentException.class, () -> CompanyName.of(null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForLengthExceeded() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i <= 128; i++) {
        sb.append('1');
      }
      assertThrows(IllegalArgumentException.class, () -> CompanyName.of(sb.toString()));
    }

    @Test
    void shouldReturnCompanyNameForMaxLengthString() {
      String maxVal = String.join("", Collections.nCopies(128, "a"));
      CompanyName.of(maxVal);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNameExceedsMaxLength() {
      String overMaxVal = String.join("", Collections.nCopies(129, "1"));
      assertThrows(IllegalArgumentException.class, () -> CompanyName.of(overMaxVal));
    }
  }

  @Nested
  class ToStringTest {


    @Test
    void shouldReturnOriginalValueWhenToStringIsCalled() {
      String testValue = "TestCompanyName";
      CompanyName companyName = CompanyName.of(testValue);
      assertEquals(testValue, companyName.toString());
    }

  }


}