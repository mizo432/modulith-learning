package undecided.generic.accountMgmt.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Usernameのテスト")
class UsernameTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("有効なユーザー名でインスタンスが作成できる")
    void shouldCreateUsernameWithValidName() {
      Username username = new Username("testuser");

      assertThat(username.value()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("nullを指定すると空のUsernameが作成される")
    void shouldCreateEmptyUsernameWhenNull() {
      Username username = new Username(null);
      assertThat(username.value()).isEmpty();
    }

    @Test
    @DisplayName("空文字列を指定すると空のUsernameが作成される")
    void shouldCreateEmptyUsernameWhenEmpty() {
      Username username = new Username("");
      assertThat(username.value()).isEmpty();
    }

    @Test
    @DisplayName("空白文字列を指定すると空のUsernameが作成される")
    void shouldCreateEmptyUsernameWhenBlank() {
      Username username = new Username("   ");
      assertThat(username.value()).isEmpty();
    }

    @Test
    @DisplayName("51文字以上のユーザー名を指定すると例外がスローされる")
    void shouldThrowExceptionWhenTooLong() {
      String longName = "a".repeat(51);

      assertThatThrownBy(() -> new Username(longName))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("50 characters or less");
    }

    @Test
    @DisplayName("50文字のユーザー名は有効である")
    void shouldAccept50CharacterUsername() {
      String name50 = "a".repeat(50);
      Username username = new Username(name50);

      assertThat(username.value()).hasSize(50);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("ユーザー名の値を返す")
    void shouldReturnValueInToString() {
      Username username = new Username("testuser");

      assertThat(username.toString()).isEqualTo("testuser");
    }
  }
}
