package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class NameKanaTest {

  @Test
  void shouldReconstructFromValidString() {
    String inputValue = "ValidNameKana";
    NameKana nameKana = NameKana.reconstruct(inputValue);
    assertEquals(inputValue, nameKana.getValue(),
        "NameKana should be reconstructed correctly from valid string");
  }

  @Test
  void shouldHandleNullStringInputGracefully() {
    String inputValue = null;
    NameKana nameKana = NameKana.reconstruct(inputValue);
    assertNull(nameKana.getValue(), "NameKana should handle null string input gracefully");
  }

  @Test
  void shouldHandleEmptyStringInputGracefully() {
    String inputValue = "";
    NameKana nameKana = NameKana.reconstruct(inputValue);
    assertEquals("", nameKana.getValue(),
        "NameKana value should be empty string when input value is empty string");
  }

  @Test
  void shouldHandleWhiteSpacesStringInputGracefully() {
    String inputValue = "   ";
    NameKana nameKana = NameKana.reconstruct(inputValue);
    assertEquals("   ", nameKana.getValue(),
        "NameKana value should preserve white spaces when the input value has white spaces");
  }
}