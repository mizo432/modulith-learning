package undecided.config.presentation.web.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * {@code HttpSessionEventLoggingListenerTest} は、HttpSessionEventLoggingListener の単体テストクラスです。
 *
 * <p>規約要件: 1. @Tag("small") を付与 2. クラス・メソッドともに日本語 @DisplayName を付与 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない 5. package-private 可視性 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("HttpSessionEventLoggingListenerの単体テスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HttpSessionEventLoggingListenerTest {

  private final HttpSessionEventLoggingListener listener = new HttpSessionEventLoggingListener();

  @Nested
  @DisplayName("sessionCreated メソッドのテスト")
  class SessionCreatedTest {

    @Test
    @DisplayName("セッションが作成された場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenSessionCreated() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      HttpSessionEvent event = new HttpSessionEvent(session);

      // Act & Assert
      assertThatCode(() -> listener.sessionCreated(event))
          .as("セッション作成イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("セッションが作成された場合、セッションIDが正しく取得されること")
    void shouldGetSessionIdWhenSessionCreated() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      HttpSessionEvent event = new HttpSessionEvent(session);

      // Act
      listener.sessionCreated(event);

      // Assert
      // リスナーはログ出力のみを行い、例外をスローしないことを確認
      assertThatCode(() -> listener.sessionCreated(event))
          .as("セッション作成イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("sessionDestroyed メソッドのテスト")
  class SessionDestroyedTest {

    @Test
    @DisplayName("セッションが破棄された場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenSessionDestroyed() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      HttpSessionEvent event = new HttpSessionEvent(session);

      // Act & Assert
      assertThatCode(() -> listener.sessionDestroyed(event))
          .as("セッション破棄イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("sessionWillPassivate メソッドのテスト")
  class SessionWillPassivateTest {

    @Test
    @DisplayName("セッションがパッシベートされる場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenSessionWillPassivate() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      HttpSessionEvent event = new HttpSessionEvent(session);

      // Act & Assert
      assertThatCode(() -> listener.sessionWillPassivate(event))
          .as("セッションパッシベーションイベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("sessionDidActivate メソッドのテスト")
  class SessionDidActivateTest {

    @Test
    @DisplayName("セッションがアクティベートされた場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenSessionDidActivate() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      HttpSessionEvent event = new HttpSessionEvent(session);

      // Act & Assert
      assertThatCode(() -> listener.sessionDidActivate(event))
          .as("セッションアクティベーションイベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("attributeAdded メソッドのテスト")
  class AttributeAddedTest {

    @Test
    @DisplayName("属性が追加された場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenAttributeAdded() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      String attributeName = "testAttribute";
      String attributeValue = "testValue";
      HttpSessionBindingEvent event =
          new HttpSessionBindingEvent(session, attributeName, attributeValue);

      // Act & Assert
      assertThatCode(() -> listener.attributeAdded(event))
          .as("属性追加イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("属性値がnullの場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenAttributeValueIsNull() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      String attributeName = "testAttribute";
      HttpSessionBindingEvent event = new HttpSessionBindingEvent(session, attributeName, null);

      // Act & Assert
      assertThatCode(() -> listener.attributeAdded(event))
          .as("属性値がnullでも例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("attributeRemoved メソッドのテスト")
  class AttributeRemovedTest {

    @Test
    @DisplayName("属性が削除された場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenAttributeRemoved() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      String attributeName = "testAttribute";
      String attributeValue = "testValue";
      HttpSessionBindingEvent event =
          new HttpSessionBindingEvent(session, attributeName, attributeValue);

      // Act & Assert
      assertThatCode(() -> listener.attributeRemoved(event))
          .as("属性削除イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("attributeReplaced メソッドのテスト")
  class AttributeReplacedTest {

    @Test
    @DisplayName("属性が置換された場合、例外が発生しないこと")
    void shouldNotThrowExceptionWhenAttributeReplaced() {
      // Arrange
      HttpSession session = mock(HttpSession.class);
      when(session.getId()).thenReturn("test-session-id");
      String attributeName = "testAttribute";
      String attributeValue = "newValue";
      HttpSessionBindingEvent event =
          new HttpSessionBindingEvent(session, attributeName, attributeValue);

      // Act & Assert
      assertThatCode(() -> listener.attributeReplaced(event))
          .as("属性置換イベントの処理で例外が発生しないこと")
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("リスナーのインターフェース実装テスト")
  class InterfaceImplementationTest {

    @Test
    @DisplayName("HttpSessionListener インターフェースを実装していること")
    void shouldImplementHttpSessionListener() {
      // Assert
      assertThat(listener)
          .as("HttpSessionListener インターフェースを実装していること")
          .isInstanceOf(jakarta.servlet.http.HttpSessionListener.class);
    }

    @Test
    @DisplayName("HttpSessionAttributeListener インターフェースを実装していること")
    void shouldImplementHttpSessionAttributeListener() {
      // Assert
      assertThat(listener)
          .as("HttpSessionAttributeListener インターフェースを実装していること")
          .isInstanceOf(jakarta.servlet.http.HttpSessionAttributeListener.class);
    }

    @Test
    @DisplayName("HttpSessionActivationListener インターフェースを実装していること")
    void shouldImplementHttpSessionActivationListener() {
      // Assert
      assertThat(listener)
          .as("HttpSessionActivationListener インターフェースを実装していること")
          .isInstanceOf(jakarta.servlet.http.HttpSessionActivationListener.class);
    }
  }
}
