package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureName;

class PrefectureNameTest {

  @Test
  void testReconstruct() {
    String value = "Sample Name";
    PrefectureName prefectureName = PrefectureName.reconstruct(value);
    Assertions.assertNotNull(prefectureName);
    assertEquals(value, prefectureName.getValue());
  }

}
