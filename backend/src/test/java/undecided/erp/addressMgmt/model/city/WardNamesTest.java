package undecided.erp.addressMgmt.model.city;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the WardNames class.
 */
public class WardNamesTest {

  /**
   * Test case for reconstruct method.
   */
  @Test
  public void testReconstruct() {
    // Arrange
    String expectedName = "TestName";
    String expectedKana = "TestKana";
    String expectedRoma = "TestRoma";

    // Act
    WardNames wardNames = WardNames.reconstruct(expectedName, expectedKana, expectedRoma);

    // Assert
    assertEquals(expectedName, wardNames.getName().getValue());
    assertEquals(expectedKana, wardNames.getKana().getValue());
    assertEquals(expectedRoma, wardNames.getRoma().getValue());
  }
}