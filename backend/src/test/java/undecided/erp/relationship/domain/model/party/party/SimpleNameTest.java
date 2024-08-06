package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SimpleNameTest {

  /**
   * Method under test: {@link SimpleName#of(String)}
   */
  @Test
  void of1() {
    // Arrange, Act and Assert
    assertEquals("42", SimpleName.of("42").getValue());
  }

  @Test
  void of2() {
    // Arrange, Act and Assert
    assertThrows(NullPointerException.class, () -> SimpleName.of(null));
  }

  @Test
  void of3() {
    // Arrange, Act and Assert
    assertThrows(IllegalArgumentException.class, () -> SimpleName.of(""));
  }

  /**
   * Method under test: {@link SimpleName#reconstruct(String)}
   */
  @Test
  void reconstruct() {
    // Arrange, Act and Assert
    assertEquals("42", SimpleName.reconstruct("42").getValue());
    assertThat(SimpleName.reconstruct("")).isEqualTo(SimpleName.EMPTY);
  }

  /**
   * Method under test: {@link SimpleName#toString()}
   */
  @Test
  void toStringA() {
    // Arrange, Act and Assert
    assertEquals("42", SimpleName.of("42").toString());
  }
}
