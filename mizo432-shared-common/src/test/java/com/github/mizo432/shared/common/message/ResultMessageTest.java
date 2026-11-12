package com.github.mizo432.shared.common.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ResultMessageの単体テスト")
class ResultMessageTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("コード、引数、テキストで正常に作成されること")
    void shouldCreateWithCodeArgsAndText() {
      // Arrange
      String code = "ERROR.001";
      Object[] args = new Object[] {"arg1", "arg2"};
      String text = "Error message";

      // Act
      ResultMessage message = new ResultMessage(code, args, text);

      // Assert
      assertThat(message.code()).isEqualTo(code);
      assertThat(message.args()).containsExactly("arg1", "arg2");
      assertThat(message.text()).isEqualTo(text);
    }

    @Test
    @DisplayName("引数がnullの場合、空の配列に置換されること")
    void shouldReplaceNullArgsWithEmptyArray() {
      // Act
      ResultMessage message = new ResultMessage("ERROR.001", null, "Error message");

      // Assert
      assertThat(message.args()).isEmpty();
    }

    @Test
    @DisplayName("テキストがnullの場合、nullとして保持されること")
    void shouldKeepNullText() {
      // Act
      ResultMessage message = new ResultMessage("ERROR.001", new Object[0], null);

      // Assert
      assertThat(message.text()).isNull();
    }
  }

  @Nested
  @DisplayName("fromCode メソッドのテスト")
  class FromCodeMethodTest {

    @Test
    @DisplayName("コードのみでResultMessageが作成されること")
    void shouldCreateMessageFromCode() {
      // Act
      ResultMessage message = ResultMessage.fromCode("ERROR.001");

      // Assert
      assertThat(message.code()).isEqualTo("ERROR.001");
      assertThat(message.args()).isEmpty();
      assertThat(message.text()).isNull();
    }

    @Test
    @DisplayName("コードと引数でResultMessageが作成されること")
    void shouldCreateMessageFromCodeWithArgs() {
      // Act
      ResultMessage message = ResultMessage.fromCode("ERROR.001", "arg1", "arg2");

      // Assert
      assertThat(message.code()).isEqualTo("ERROR.001");
      assertThat(message.args()).containsExactly("arg1", "arg2");
      assertThat(message.text()).isNull();
    }
  }

  @Nested
  @DisplayName("fromText メソッドのテスト")
  class FromTextMethodTest {

    @Test
    @DisplayName("テキストのみでResultMessageが作成されること")
    void shouldCreateMessageFromText() {
      // Act
      ResultMessage message = ResultMessage.fromText("Error message");

      // Assert
      assertThat(message.code()).isNull();
      assertThat(message.args()).isEmpty();
      assertThat(message.text()).isEqualTo("Error message");
    }
  }

  @Nested
  @DisplayName("toString メソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("テキストがある場合、テキストが返されること")
    void shouldReturnTextWhenTextIsPresent() {
      // Arrange
      ResultMessage message = new ResultMessage("ERROR.001", new Object[0], "Error message");

      // Act & Assert
      assertThat(message.toString()).isEqualTo("Error message");
    }

    @Test
    @DisplayName("テキストがない場合、詳細な形式が返されること")
    void shouldReturnDetailedFormatWhenTextIsMissing() {
      // Arrange
      ResultMessage message = ResultMessage.fromCode("ERROR.001", "arg1");

      // Act & Assert
      assertThat(message.toString())
          .isEqualTo("ResultMessage{code='ERROR.001', args=[arg1]}");
    }

    @Test
    @DisplayName("テキストが空文字列の場合、詳細な形式が返されること")
    void shouldReturnDetailedFormatWhenTextIsEmpty() {
      // Arrange
      ResultMessage message = new ResultMessage("ERROR.001", new Object[0], "");

      // Act & Assert
      assertThat(message.toString())
          .isEqualTo("ResultMessage{code='ERROR.001', args=[]}");
    }
  }
}
