package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.prefecture.PrefectureKana;

class PrefectureKanaTest {

  @Test
  void shouldReconstructFromValidString() {
    String inputValue = "ValidNameKana";
    PrefectureKana prefectureKana = PrefectureKana.reconstruct(inputValue);
    assertEquals(inputValue, prefectureKana.getValue(),
        "NameKana should be reconstructed correctly from valid string");
  }

  @Test
  void shouldHandleNullStringInputGracefully() {
    String inputValue = null;
    PrefectureKana prefectureKana = PrefectureKana.reconstruct(inputValue);
    assertNull(prefectureKana.getValue(), "NameKana should handle null string input gracefully");
  }

  @Test
  void shouldHandleEmptyStringInputGracefully() {
    String inputValue = "";
    PrefectureKana prefectureKana = PrefectureKana.reconstruct(inputValue);
    assertEquals("", prefectureKana.getValue(),
        "NameKana value should be empty string when input value is empty string");
  }

  @Test
  void shouldHandleWhiteSpacesStringInputGracefully() {
    String inputValue = "   ";
    PrefectureKana prefectureKana = PrefectureKana.reconstruct(inputValue);
    assertEquals("   ", prefectureKana.getValue(),
        "NameKana value should preserve white spaces when the input value has white spaces");
  }
}