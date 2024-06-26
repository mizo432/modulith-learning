package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.city.WardName;

public class WardNameTest {

  @Test
  void shouldReconstructNonNullValue() {
    String inputValue = "Ward 123";
    WardName reconstructedWard = WardName.reconstruct(inputValue);
    assertEquals(inputValue, reconstructedWard.getValue());
  }

  @Test
  void shouldReconstructNullValueAsNullObject() {
    String inputValue = null;
    WardName reconstructedWard = WardName.reconstruct(inputValue);
    assertNull(reconstructedWard.getValue());
  }
}