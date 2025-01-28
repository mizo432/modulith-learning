package undecided.erp.common.primitive;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Arrays2Test {

  @Test
  void whenAllElementsAreNotNull_thenAllElementsNotNullReturnsTrue() {
    //given
    Integer[] arrayWithAllElements = new Integer[]{1, 2, 3};

    //when
    boolean result = Arrays2.allElementsNotNull(arrayWithAllElements);

    //then
    assertTrue(result);
  }

  @Test
  void whenAnyElementIsNull_thenAllElementsNotNullReturnsFalse() {
    //given
    Integer[] arrayWithNullElement = new Integer[]{1, null, 3};

    //when
    boolean result = Arrays2.allElementsNotNull(arrayWithNullElement);

    //then
    assertFalse(result);
  }

  @Test
  void whenArrayIsEmpty_thenAllElementsNotNullReturnsTrue() {
    //given
    Integer[] emptyArray = new Integer[]{};

    //when
    boolean result = Arrays2.allElementsNotNull(emptyArray);

    //then
    assertTrue(result);
  }
}