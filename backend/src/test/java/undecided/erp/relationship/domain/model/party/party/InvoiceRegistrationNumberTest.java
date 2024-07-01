package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InvoiceRegistrationNumberTest {


  @Nested
  class ReconstructTest {

    /**
     * This test verifies the reconstruct method of the InvoiceRegistrationNumber class.
     * <p>
     * Scenario: When the input value is non-null, the function should return
     * InvoiceRegistrationNumber with the input value.
     */
    @Test
    void shouldReturnInvoiceRegistrationNumberWhenValueIsNonNull() {
      String value = "nonNullValue";
      assertThat(InvoiceRegistrationNumber.reconstruct(value, PartyType.PERSON).getValue())
          .isEqualTo(value);
      assertThat(InvoiceRegistrationNumber.reconstruct(value, PartyType.ORGANIZATION).getValue())
          .isEqualTo(value);
    }

    /**
     * This test verifies the reconstruct method of the InvoiceRegistrationNumber class.
     * <p>
     * Scenario: When the input value is null, the function should return EMPTY
     * InvoiceRegistrationNumber.
     */
    @Test
    void shouldReturnEmptyInvoiceRegistrationNumberWhenValueIsNull() {
      String value = null;
      assertThat(InvoiceRegistrationNumber.reconstruct(value, PartyType.PERSON))
          .isSameAs(InvoiceRegistrationNumber.EMPTY);
      assertThat(InvoiceRegistrationNumber.reconstruct(value, PartyType.ORGANIZATION))
          .isSameAs(InvoiceRegistrationNumber.EMPTY);
    }
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

