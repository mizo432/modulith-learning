package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class NameTest {

  @Test
  void testReconstruct() {
    String value = "Sample Name";
    Name name = Name.reconstruct(value);
    assertNotNull(name);
    assertEquals(value, name.getValue());
  }

}
