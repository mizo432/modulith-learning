package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InvoiceRegistrationNumberTest {


  @Nested
  class ReconstructTest {


  }

  @Nested
  class ToStringMethodTest {

    /**
     * This test verifies the toString method of the InvoiceRegistrationNumber class.
     * <p>
     * Scenario: When the value of InvoiceRegistrationNumber is non-null, the function should return
     * the same string.
     */
    @Test
    void shouldReturnSameStringWhenValueIsNonNull() {
      String value = "nonNullValue";
      assertThat(InvoiceRegistrationNumber.of(value).toString()).isEqualTo(value);
    }

    /**
     * This test verifies the toString method of the InvoiceRegistrationNumber class.
     * <p>
     * Scenario: When the value of InvoiceRegistrationNumber is null, the function should return
     * null.
     */
    @Test
    void shouldReturnNullWhenValueIsNull() {
      assertThat(InvoiceRegistrationNumber.EMPTY.toString()).isEqualTo("null");
    }
  }
}

