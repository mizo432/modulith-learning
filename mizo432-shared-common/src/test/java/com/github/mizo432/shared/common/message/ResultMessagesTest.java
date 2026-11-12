package com.github.mizo432.shared.common.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ResultMessagesの単体テスト")
class ResultMessagesTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("タイプのみで正常に作成されること")
    void shouldCreateWithTypeOnly() {
      // Act
      ResultMessages messages = new ResultMessages(StandardResultMessageType.SUCCESS);

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
      assertThat(messages.isNotEmpty()).isFalse();
    }

    @Test
    @DisplayName("タイプとメッセージで正常に作成されること")
    void shouldCreateWithTypeAndMessages() {
      // Arrange
      ResultMessage message = ResultMessage.fromText("Test message");

      // Act
      ResultMessages messages = new ResultMessages(StandardResultMessageType.SUCCESS, message);

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
      assertThat(messages.isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("タイプがnullの場合例外がスローされること")
    void shouldThrowExceptionWhenTypeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> new ResultMessages(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("type must not be null!");
    }

    @Test
    @DisplayName("メッセージ配列がnullの場合、空のリストとして作成されること")
    void shouldCreateWithEmptyListWhenMessagesIsNull() {
      // Act
      ResultMessages messages =
          new ResultMessages(StandardResultMessageType.SUCCESS, (ResultMessage[]) null);

      // Assert
      assertThat(messages.isNotEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("ファクトリメソッドのテスト")
  class FactoryMethodsTest {

    @Test
    @DisplayName("successメソッドで成功メッセージが作成されること")
    void shouldCreateSuccessMessage() {
      // Act
      ResultMessages messages = ResultMessages.success();

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
    }

    @Test
    @DisplayName("infoメソッドで情報メッセージが作成されること")
    void shouldCreateInfoMessage() {
      // Act
      ResultMessages messages = ResultMessages.info();

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.INFO);
    }

    @Test
    @DisplayName("warningメソッドで警告メッセージが作成されること")
    void shouldCreateWarningMessage() {
      // Act
      ResultMessages messages = ResultMessages.warning();

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.WARNING);
    }

    @Test
    @DisplayName("errorメソッドでエラーメッセージが作成されること")
    void shouldCreateErrorMessage() {
      // Act
      ResultMessages messages = ResultMessages.error();

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.ERROR);
    }

    @Test
    @DisplayName("dangerメソッドで危険メッセージが作成されること")
    void shouldCreateDangerMessage() {
      // Act
      ResultMessages messages = ResultMessages.danger();

      // Assert
      assertThat(messages.getType()).isEqualTo(StandardResultMessageType.DANGER);
    }
  }

  @Nested
  @DisplayName("add メソッドのテスト")
  class AddMethodTest {

    @Test
    @DisplayName("ResultMessageが正常に追加されること")
    void shouldAddResultMessage() {
      // Arrange
      ResultMessages messages = ResultMessages.success();
      ResultMessage message = ResultMessage.fromText("Test message");

      // Act
      ResultMessages result = messages.add(message);

      // Assert
      assertThat(messages.isNotEmpty()).isTrue();
      assertThat(result).isSameAs(messages);
    }

    @Test
    @DisplayName("コードでメッセージが追加されること")
    void shouldAddMessageByCode() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act
      messages.add("ERROR.001");

      // Assert
      assertThat(messages.isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("コードと引数でメッセージが追加されること")
    void shouldAddMessageByCodeWithArgs() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act
      messages.add("ERROR.001", "arg1", "arg2");

      // Assert
      assertThat(messages.isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("nullメッセージが渡された場合例外がスローされること")
    void shouldThrowExceptionWhenMessageIsNull() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThatThrownBy(() -> messages.add((ResultMessage) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("message must not be null!");
    }

    @Test
    @DisplayName("nullコードが渡された場合例外がスローされること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThatThrownBy(() -> messages.add((String) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("code must not be null!");
    }

    @Test
    @DisplayName("チェーン呼び出しが可能であること")
    void shouldSupportChaining() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act
      ResultMessages result =
          messages
              .add(ResultMessage.fromText("Message 1"))
              .add(ResultMessage.fromText("Message 2"));

      // Assert
      assertThat(result).isSameAs(messages);
    }
  }

  @Nested
  @DisplayName("addAll メソッドのテスト")
  class AddAllMethodTest {

    @Test
    @DisplayName("複数のメッセージが正常に追加されること")
    void shouldAddMultipleMessages() {
      // Arrange
      ResultMessages messages = ResultMessages.success();
      ResultMessage message1 = ResultMessage.fromText("Message 1");
      ResultMessage message2 = ResultMessage.fromText("Message 2");

      // Act
      messages.addAll(message1, message2);

      // Assert
      assertThat(messages.isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("コレクションのメッセージが正常に追加されること")
    void shouldAddMessagesFromCollection() {
      // Arrange
      ResultMessages messages = ResultMessages.success();
      List<ResultMessage> messageList =
          List.of(ResultMessage.fromText("Message 1"), ResultMessage.fromText("Message 2"));

      // Act
      messages.addAll(messageList);

      // Assert
      assertThat(messages.isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("null配列が渡された場合例外がスローされること")
    void shouldThrowExceptionWhenArrayIsNull() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThatThrownBy(() -> messages.addAll((ResultMessage[]) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("messages must not be null!");
    }

    @Test
    @DisplayName("nullコレクションが渡された場合例外がスローされること")
    void shouldThrowExceptionWhenCollectionIsNull() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThatThrownBy(() -> messages.addAll((List<ResultMessage>) null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("messages must not be null!");
    }
  }

  @Nested
  @DisplayName("isNotEmpty メソッドのテスト")
  class IsNotEmptyMethodTest {

    @Test
    @DisplayName("メッセージがない場合falseを返すこと")
    void shouldReturnFalseWhenEmpty() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThat(messages.isNotEmpty()).isFalse();
    }

    @Test
    @DisplayName("メッセージがある場合trueを返すこと")
    void shouldReturnTrueWhenNotEmpty() {
      // Arrange
      ResultMessages messages = ResultMessages.success().add(ResultMessage.fromText("Test"));

      // Act & Assert
      assertThat(messages.isNotEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("iterator メソッドのテスト")
  class IteratorMethodTest {

    @Test
    @DisplayName("メッセージをイテレートできること")
    void shouldIterateOverMessages() {
      // Arrange
      ResultMessages messages =
          ResultMessages.success()
              .add(ResultMessage.fromText("Message 1"))
              .add(ResultMessage.fromText("Message 2"));

      // Act & Assert
      int count = 0;
      for (ResultMessage message : messages) {
        assertThat(message).isNotNull();
        count++;
      }
      assertThat(count).isEqualTo(2);
    }
  }

  @Nested
  @DisplayName("toString メソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("文字列表現が正しく返されること")
    void shouldReturnCorrectStringRepresentation() {
      // Arrange
      ResultMessages messages = ResultMessages.success();

      // Act & Assert
      assertThat(messages.toString()).contains("type=").contains("list=");
    }
  }

  @Nested
  @DisplayName("DEFAULT_MESSAGES_ATTRIBUTE_NAME のテスト")
  class DefaultAttributeNameTest {

    @Test
    @DisplayName("デフォルト属性名が正しく設定されていること")
    void shouldHaveCorrectDefaultAttributeName() {
      // Act & Assert
      assertThat(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME).isEqualTo("resultMessages");
    }
  }
}
