package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrefectureNameTest {

  @Test
  void shouldReconstructNameFromString() {
    String name = "testName";
    String nameKana = "testNameKana";
    String nameRoma = "testNameRoma";

    PrefectureName prefectureName = PrefectureName.reconstruct(name, nameKana, nameRoma);
    assertEquals(name, prefectureName.getName().toString());
    assertEquals(nameKana, prefectureName.getKana().toString());
    assertEquals(nameRoma, prefectureName.getRoma().toString());
  }

  @Test
  void shouldReturnCorrectStringWhenToStringIsCalled() {
    String name = "anotherTestName";
    String kana = "anotherTestKana";
    String roma = "anotherTestRoma";

    PrefectureName prefectureName = new PrefectureName(Name.reconstruct(name),
        NameKana.reconstruct(kana),
        NameRoma.reconstruct(roma));

    String expectedToString = "PerfName{" +
        "name=" + prefectureName.getName() +
        ", kana=" + prefectureName.getKana() +
        ", roma=" + prefectureName.getRoma() +
        '}';

    assertEquals(expectedToString, prefectureName.toString());
  }
}