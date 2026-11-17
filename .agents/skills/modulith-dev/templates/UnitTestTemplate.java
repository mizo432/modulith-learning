package undecided.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * {@code SampleServiceTest} は、SampleService の単体テストクラスです。
 * <p>
 * 規約要件:
 * 1. @Tag("small") を付与
 * 2. クラス・メソッドともに日本語 @DisplayName を付与
 * 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない
 * 5. package-private 可視性
 * 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("SampleServiceの単体テスト")
class SampleServiceTest {

  private final SampleService service = new SampleService();

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("正常な引数が渡された場合、期待通りの結果を返すこと")
    void shouldReturnExpectedResultWhenInputIsValid() {
      // Arrange
      String input = "test-value";

      // Act
      String actual = service.execute(input);

      // Assert
      assertThat(actual)
          .as("正常値に対する処理結果が一致すること")
          .isEqualTo("PROCESSED: test-value");
    }

    @Test
    @DisplayName("引数がnullの場合、例外がスローされること")
    void shouldThrowExceptionWhenInputIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.execute(null))
          .as("null入力時にIllegalArgumentExceptionまたはBusinessExceptionが発生すること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("空文字列が渡された場合、空の結果または所定の処理結果を返すこと")
    void shouldHandleEmptyStringCorrectly() {
      // Arrange
      String input = "";

      // Act
      String actual = service.execute(input);

      // Assert
      assertThat(actual).isEmpty();
    }
  }
}
