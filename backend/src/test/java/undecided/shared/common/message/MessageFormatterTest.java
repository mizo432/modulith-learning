package undecided.shared.common.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.MissingResourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MessageFormatterTest クラスのテスト")
class MessageFormatterTest {

  @Nested
  @DisplayName("メソッド format")
  class FormatMethodTest {

    @Test
    @DisplayName("メッセージコードが存在し、引数がない場合の正しいフォーマットの確認")
    void shouldFormatMessageWithoutArguments() {
      // Arrange
      var messageCode = "sample.message"; // "sample.message" はリソースで定義済みと仮定
      // リソースには以下のように定義されていると仮定:
      // sample.message=Hello, World!

      // Act
      var result = MessageFormatter.format(messageCode);

      // Assert
      assertThat(result).isEqualTo("Hello, World!");
    }

    @Test
    @DisplayName("メッセージコードが存在し、引数が渡される場合の正しいフォーマットの確認")
    void shouldFormatMessageWithArguments() {
      // Arrange
      var messageCode = "greeting.message"; // "greeting.message" はリソースで定義済みと仮定
      var args = new Object[] {"ユーザー", "日本"};
      // リソースには以下のように定義されていると仮定:
      // greeting.message=こんにちは、{0} さん！{1}からの挨拶です。

      // Act
      var result = MessageFormatter.format(messageCode, args);

      // Assert
      assertThat(result).isEqualTo("こんにちは、ユーザー さん！日本からの挨拶です。");
    }

    @Test
    @DisplayName("メッセージコードが存在しない場合 MissingResourceException が発生することの確認")
    void shouldThrowMissingResourceExceptionWhenCodeIsNotFound() {
      // Arrange
      var nonexistentCode = "nonexistent.code";

      // Act & Assert
      assertThatThrownBy(() -> MessageFormatter.format(nonexistentCode))
          .isInstanceOf(MissingResourceException.class)
          .hasMessageContaining(nonexistentCode);
    }

    @Test
    @DisplayName("引数が渡されず、引数を必要とするメッセージコードの場合正しい例外が発生することの確認")
    void shouldThrowIllegalArgumentExceptionWhenArgumentsAreMissing() {
      // Arrange
      var messageCode = "greeting.message"; // "greeting.message" は placeholder を持つパターンと仮定
      // リソースには以下のように定義されていると仮定:
      // greeting.message=こんにちは、{0} さん！{1}からの挨拶です。

      // Act & Assert
      assertThatThrownBy(() -> MessageFormatter.format(messageCode))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("null のメッセージコードが渡された場合 NullPointerException が発生することの確認")
    void shouldThrowNullPointerExceptionWhenCodeIsNull() {
      // Arrange
      String nullCode = null;

      // Act & Assert
      assertThatThrownBy(() -> MessageFormatter.format(nullCode))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("null の引数が渡された場合正しく処理されることの確認")
    void shouldHandleNullArgumentsCorrectly() {
      // Arrange
      var messageCode = "message.with.null"; // "message.with.null" はリソースで定義済みと仮定
      var args = new Object[] {null};
      // リソースには以下のように定義されていると仮定:
      // message.with.null=値は {0} です。

      // Act
      var result = MessageFormatter.format(messageCode, args);

      // Assert
      assertThat(result).isEqualTo("値は null です。");
    }
  }
}
