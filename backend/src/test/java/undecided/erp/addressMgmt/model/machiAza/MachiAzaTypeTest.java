package undecided.erp.addressMgmt.model.machiAza;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class MachiAzaTypeTest {

  @Test
  void testValueOfCodeValidCode() {
    assertEquals(MachiAzaType.大字町, MachiAzaType.valueOfCode("1"));
    assertEquals(MachiAzaType.丁目, MachiAzaType.valueOfCode("2"));
    assertEquals(MachiAzaType.小字, MachiAzaType.valueOfCode("3"));
    assertEquals(MachiAzaType.大字町または丁目または小字なし, MachiAzaType.valueOfCode("4"));
    assertEquals(MachiAzaType.道路方式の住居表示における道路名, MachiAzaType.valueOfCode("5"));
  }

  @Test
  void testValueOfCodeInvalidCode() {
    assertThrows(NoSuchElementException.class, () -> MachiAzaType.valueOfCode("Dummy"));
    assertThrows(NoSuchElementException.class, () -> MachiAzaType.valueOfCode(""));
  }

}