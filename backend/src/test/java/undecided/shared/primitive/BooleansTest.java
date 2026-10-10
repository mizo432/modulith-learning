package undecided.shared.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * {@code BooleansTest} は、Booleans ユーティリティクラスの単体テストクラスです。
 *
 * <p>規約要件: 1. @Tag("small") を付与 2. クラス・メソッドともに日本語 @DisplayName を付与 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない 5. package-private 可視性 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("Booleansの単体テスト")
class BooleansTest {

  @Nested
  @DisplayName("isTrue メソッドのテスト")
  class IsTrueTest {

    @Test
    @DisplayName("値がtrueの場合、trueを返すこと")
    void shouldReturnTrueWhenValueIsTrue() {
      // Arrange
      Boolean value = true;

      // Act
      boolean actual = Booleans.isTrue(value);

      // Assert
      assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("値がfalseの場合、falseを返すこと")
    void shouldReturnFalseWhenValueIsFalse() {
      // Arrange
      Boolean value = false;

      // Act
      boolean actual = Booleans.isTrue(value);

      // Assert
      assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("値がnullの場合、falseを返すこと")
    void shouldReturnFalseWhenValueIsNull() {
      // Arrange
      Boolean value = null;

      // Act
      boolean actual = Booleans.isTrue(value);

      // Assert
      assertThat(actual).isFalse();
    }
  }

  @Nested
  @DisplayName("isFalse メソッドのテスト")
  class IsFalseTest {

    @Test
    @DisplayName("値がfalseの場合、trueを返すこと")
    void shouldReturnTrueWhenValueIsFalse() {
      // Arrange
      Boolean value = false;

      // Act
      boolean actual = Booleans.isFalse(value);

      // Assert
      assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("値がtrueの場合、falseを返すこと")
    void shouldReturnFalseWhenValueIsTrue() {
      // Arrange
      Boolean value = true;

      // Act
      boolean actual = Booleans.isFalse(value);

      // Assert
      assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("値がnullの場合、trueを返すこと")
    void shouldReturnTrueWhenValueIsNull() {
      // Arrange
      Boolean value = null;

      // Act
      boolean actual = Booleans.isFalse(value);

      // Assert
      assertThat(actual).isTrue();
    }
  }
}
