package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureAttribute;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureKana;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureLgCode;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureName;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureNames;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureRoma;

public class PrefectureAttributeTest {

  @Test
  public void testReconstructWithLgCodeAndPerfName() {
    PrefectureNames testName = new PrefectureNames(PrefectureName.reconstruct("test"),
        PrefectureKana.reconstruct("test_kana"),
        PrefectureRoma.reconstruct("test_roma"));
    PrefectureLgCode testLgCode = new PrefectureLgCode("test");

    PrefectureAttribute testAttribute = new PrefectureAttribute(testLgCode, testName);
    assertTrue(testAttribute.getLgCode().equals(testLgCode));
    assertTrue(testAttribute.getNames().equals(testName));
  }

  @Test
  public void testReconstructWithStringArgs() {
    String testLgCode = "test";
    String testName = "test";
    String testNameKana = "test_kana";
    String testNameRoma = "test_roma";
    PrefectureAttribute testAttribute = PrefectureAttribute.reconstruct(testLgCode, testName,
        testNameKana, testNameRoma);
    assertTrue(testAttribute.getLgCode().getValue().equals(testLgCode));
    assertTrue(testAttribute.getNames().getName().toString().equals(testName));
    assertTrue(testAttribute.getNames().getKana().toString().equals(testNameKana));
    assertTrue(testAttribute.getNames().getRoma().toString().equals(testNameRoma));
  }
}
