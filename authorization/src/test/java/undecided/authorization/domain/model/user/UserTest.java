package undecided.authorization.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** {@code UserTest} は、Userエンティティの単体テストクラスです。 */
@Tag("small")
@DisplayName("Userエンティティの単体テスト")
class UserTest {

  @Nested
  @DisplayName("firstLogin関連メソッドのテスト")
  class FirstLoginTest {

    @Test
    @DisplayName("デフォルトではfirstLoginがfalseであること")
    void shouldHaveFirstLoginFalseByDefault() {
      // Arrange
      User user =
          User.builder()
              .username("testuser")
              .password("password")
              .email("test@example.com")
              .enabled(true)
              .build();

      // Assert
      assertThat(user.isFirstLogin()).as("デフォルトではfirstLoginがfalseであること").isFalse();
    }

    @Test
    @DisplayName("firstLoginをtrueに設定した場合、isFirstLoginがtrueを返すこと")
    void shouldReturnTrueWhenFirstLoginSetToTrue() {
      // Arrange
      User user =
          User.builder()
              .username("admin")
              .password("admin")
              .email("admin@example.com")
              .enabled(true)
              .firstLogin(true)
              .build();

      // Assert
      assertThat(user.isFirstLogin()).as("firstLoginがtrueに設定されていること").isTrue();
    }

    @Test
    @DisplayName("setFirstLoginCompleteを呼び出した後、isFirstLoginがfalseを返すこと")
    void shouldReturnFalseAfterSetFirstLoginComplete() {
      // Arrange
      User user =
          User.builder()
              .username("admin")
              .password("admin")
              .email("admin@example.com")
              .enabled(true)
              .firstLogin(true)
              .build();

      // Act
      user.setFirstLoginComplete();

      // Assert
      assertThat(user.isFirstLogin())
          .as("setFirstLoginComplete呼び出し後にfirstLoginがfalseになること")
          .isFalse();
    }

    @Test
    @DisplayName("firstLoginがfalseの状態でsetFirstLoginCompleteを呼び出してもfalseのままであること")
    void shouldRemainFalseWhenSetFirstLoginCompleteCalledOnFalseUser() {
      // Arrange
      User user =
          User.builder()
              .username("testuser")
              .password("password")
              .email("test@example.com")
              .enabled(true)
              .firstLogin(false)
              .build();

      // Act
      user.setFirstLoginComplete();

      // Assert
      assertThat(user.isFirstLogin())
          .as("firstLoginがfalseの状態でsetFirstLoginCompleteを呼び出してもfalseのままであること")
          .isFalse();
    }

    @Test
    @DisplayName("firstLoginフラグのセッターで値を変更できること")
    void shouldUpdateFirstLoginViaSetter() {
      // Arrange
      User user =
          User.builder()
              .username("testuser")
              .password("password")
              .email("test@example.com")
              .enabled(true)
              .firstLogin(false)
              .build();

      // Act
      user.setFirstLogin(true);

      // Assert
      assertThat(user.isFirstLogin()).as("セッターでfirstLoginをtrueに設定できること").isTrue();
    }
  }
}
