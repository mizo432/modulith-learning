package undecided.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Collections2Test - Collections2クラスのテスト")
class Collections2Test {

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("コレクションが空の場合、trueを返す")
    void shouldReturnTrueWhenCollectionIsEmpty() {
      // Arrange
      Collection<String> emptyCollection = Collections.emptyList();

      // Act
      boolean result = Collections2.isEmpty().test(emptyCollection);

      // Assert
      assertThat(result).as("空のコレクションの場合、trueを返すことを期待します").isTrue();
    }

    @Test
    @DisplayName("コレクションが空でない場合、falseを返す")
    void shouldReturnFalseWhenCollectionIsNotEmpty() {
      // Arrange
      Collection<String> nonEmptyCollection = new ArrayList<>();
      nonEmptyCollection.add("item");

      // Act
      boolean result = Collections2.isEmpty().test(nonEmptyCollection);

      // Assert
      assertThat(result).as("非空のコレクションの場合、falseを返すことを期待します").isFalse();
    }

    @Test
    @DisplayName("コレクションがnullの場合、例外をスローする")
    void shouldThrowExceptionWhenCollectionIsNull() {
      // Arrange
      Collection<String> nullCollection = null;

      // Act & Assert
      assertThatThrownBy(() -> Collections2.isEmpty().test(nullCollection))
          .as("nullのコレクションの場合、例外をスローすることを期待します")
          .isInstanceOf(NullPointerException.class);
    }
  }
}
