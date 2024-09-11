package undecided.erp.shared.crud;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CrudTypeTest {

  /**
   * Method under test: {@link CrudType#available()}
   */
  @Test
  void available() {
    // Arrange, Act and Assert
    assertThat(CrudType.NO_CHANGED.available()).isTrue();
    assertThat(CrudType.DELETED.available()).isFalse();
  }

  /**
   * Method under test: {@link CrudType#isChanged()}
   */
  @Test
  void isChanged() {
    // Arrange, Act and Assert
    assertThat(CrudType.NO_CHANGED.isChanged()).isFalse();
    assertThat(CrudType.CREATED.isChanged()).isTrue();
  }

  /**
   * Method under test: {@link CrudType#isCreated()}
   */
  @Test
  void isCreated() {
    // Arrange, Act and Assert
    assertThat(CrudType.NO_CHANGED.isCreated()).isFalse();
    assertThat(CrudType.CREATED.isCreated()).isTrue();
  }

  /**
   * Method under test: {@link CrudType#isDeleted()}
   */
  @Test
  void isDeleted() {
    // Arrange, Act and Assert
    assertThat(CrudType.NO_CHANGED.isDeleted()).isFalse();
    assertThat(CrudType.DELETED.isDeleted()).isTrue();
  }

  /**
   * Method under test: {@link CrudType#isUpdated()}
   */
  @Test
  void isUpdated() {
    // Arrange, Act and Assert
    assertThat(CrudType.NO_CHANGED.isUpdated()).isFalse();
    assertThat(CrudType.UPDATED.isUpdated()).isTrue();
  }
}
