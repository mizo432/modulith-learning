package undecided.erp.scrum.domain.model.userStory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BusinessValueTest {

  @Test
  void testOfNonNull() {
    Integer testValue = 123;
    BusinessValue result = BusinessValue.of(testValue);
    assertThat(result).isNotNull();
    assertThat(result.getValue()).isEqualTo(testValue);
  }

  @Test
  void testBusinessValuesOfMethod() {
    List<BusinessValue> values = Arrays.asList(BusinessValue.of(100), BusinessValue.of(200));
    BusinessValue.BusinessValues result = BusinessValue.BusinessValues.of(values);
    assertThat(result).isNotNull();
    assertThat(result.toBusinessValue().getValue()).isEqualTo(300);
  }

}
