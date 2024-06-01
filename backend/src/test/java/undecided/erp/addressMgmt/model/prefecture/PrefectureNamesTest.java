package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrefectureNamesTest {

  @Test
  void shouldReconstructNameFromString() {
    String name = "testName";
    String kana = "testNameKana";
    String roma = "testNameRoma";

    PrefectureNames prefectureNames = PrefectureNames.reconstruct(name, kana, roma);
    assertEquals(name, prefectureNames.getName().toString());
    assertEquals(kana, prefectureNames.getKana().toString());
    assertEquals(roma, prefectureNames.getRoma().toString());
  }

  @Test
  void shouldReturnCorrectStringWhenToStringIsCalled() {
    String name = "anotherTestName";
    String kana = "anotherTestKana";
    String roma = "anotherTestRoma";

    PrefectureNames prefectureNames = new PrefectureNames(PrefectureName.reconstruct(name),
        PrefectureKana.reconstruct(kana),
        PrefectureRoma.reconstruct(roma));

    String expectedToString = "PrefectureNames{" +
        "name=" + prefectureNames.getName() +
        ", kana=" + prefectureNames.getKana() +
        ", roma=" + prefectureNames.getRoma() +
        '}';

    assertEquals(expectedToString, prefectureNames.toString());
  }
}