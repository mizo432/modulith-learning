package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.prefecture.Prefecture;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureAttribute;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureLgCode;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureNames;

class PrefectureTest {

  @Test
  void testReconstruct() {
    Long id = 1L;
    PrefectureAttribute attribute = PrefectureAttribute.create(PrefectureLgCode.of("01"),
        PrefectureNames.create("name", "kana", "roma"));
    LocalDate efctDate = LocalDate.now();
    LocalDate abltDate = LocalDate.now();
    String remarks = "remarks";

    Prefecture result = Prefecture.reconstruct(id, attribute, efctDate, abltDate, remarks);

    assertEquals(id, result.getId().getValue());
    assertEquals(attribute, result.getAttribute());
    assertEquals(efctDate, result.getEffectiveDate().getValue().getValue());
    assertEquals(abltDate, result.getAbolitionDate().getValue().getValue());
    assertEquals(remarks, result.getRemarks());
  }
}
