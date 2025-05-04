package undecided.erp.relationship.domain.model.party.organizationUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.primitive.Strings2;

class OrganizationUnitNameTest {

  @Nested
  class OfTest {


    @Test
    void shouldReturnObjectNameWithNonEmptyValueWhenValueIsNotNull() {
      String testValue = "Test Value";
      OrganizationUnitName organizationUnitName = OrganizationUnitName.of(testValue);
      assertThat(organizationUnitName.toString())
          .as("Expected object with non empty value")
          .isEqualTo(testValue);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatThrownBy(() -> OrganizationUnitName.of(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("value is marked non-null but is null");
      
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
      assertThatThrownBy(() -> OrganizationUnitName.of(Strings2.EMPTY))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value cannot be null or empty");

    }
  }
}