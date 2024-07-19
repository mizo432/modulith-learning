package undecided.erp.relationship.domain.model.party.organizationUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.primitive.Strings2;

public class OrganizationUnitAttributeTest {

  @Nested
  class createTest {

    @Test
    void shouldReturnOrganizationUnitAttributeWithGivenNameWhenValidNameIsPassed() {
      OrganizationUnitName testName = OrganizationUnitName.of("Test Name");
      OrganizationUnitAttribute returnedAttribute = OrganizationUnitAttribute.create(testName);
      assertThat(returnedAttribute.getName().toString())
          .as("Expected a valid object with the passed name.")
          .isEqualTo("Test Name");
    }

    @Test
    void shouldThrowExceptionWhenPassedNameIsNull() {
      assertThatThrownBy(() -> OrganizationUnitAttribute.create(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("name is marked non-null but is null");
    }
  }


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

