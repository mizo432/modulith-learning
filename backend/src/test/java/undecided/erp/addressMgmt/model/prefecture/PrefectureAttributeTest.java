package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PrefectureAttributeTest {

  @Test
  public void testReconstructWithLgCodeAndPerfName() {
    PrefectureName testName = new PrefectureName(Name.reconstruct("test"),
        NameKana.reconstruct("test_kana"),
        NameRoma.reconstruct("test_roma"));
    PrefectureLgCode testLgCode = new PrefectureLgCode("test");

    PrefectureAttribute testAttribute = new PrefectureAttribute(testLgCode, testName);
    assertTrue(testAttribute.getLgCode().equals(testLgCode));
    assertTrue(testAttribute.getName().equals(testName));
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
    assertTrue(testAttribute.getName().getName().toString().equals(testName));
    assertTrue(testAttribute.getName().getKana().toString().equals(testNameKana));
    assertTrue(testAttribute.getName().getRoma().toString().equals(testNameRoma));
  }
}
