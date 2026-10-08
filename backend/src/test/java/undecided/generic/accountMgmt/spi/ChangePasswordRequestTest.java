package undecided.generic.accountMgmt.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ChangePasswordRequestのテスト")
class ChangePasswordRequestTest {

  @Nested
  @DisplayName("isNewPasswordMatchedメソッドのテスト")
  class IsNewPasswordMatchedTest {

    @Test
    @DisplayName("新しいパスワードと確認用パスワードが一致する場合はtrueを返す")
    void shouldReturnTrueWhenPasswordsMatch() {
      ChangePasswordRequest request =
          new ChangePasswordRequest("current", "newPassword123", "newPassword123");

      assertThat(request.isNewPasswordMatched()).isTrue();
    }

    @Test
    @DisplayName("新しいパスワードと確認用パスワードが一致しない場合はfalseを返す")
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
      ChangePasswordRequest request =
          new ChangePasswordRequest("current", "newPassword123", "differentPassword");

      assertThat(request.isNewPasswordMatched()).isFalse();
    }

    @Test
    @DisplayName("新しいパスワードがnullの場合はfalseを返す")
    void shouldReturnFalseWhenNewPasswordIsNull() {
      ChangePasswordRequest request = new ChangePasswordRequest("current", null, "newPassword123");

      assertThat(request.isNewPasswordMatched()).isFalse();
    }
  }

  @Nested
  @DisplayName("isValidPasswordLengthメソッドのテスト")
  class IsValidPasswordLengthTest {

    @Test
    @DisplayName("パスワードが8文字の場合はtrueを返す")
    void shouldReturnTrueWhenPasswordIs8Characters() {
      ChangePasswordRequest request = new ChangePasswordRequest("current", "12345678", "12345678");

      assertThat(request.isValidPasswordLength()).isTrue();
    }

    @Test
    @DisplayName("パスワードが64文字の場合はtrueを返す")
    void shouldReturnTrueWhenPasswordIs64Characters() {
      String password = "1".repeat(64);
      ChangePasswordRequest request = new ChangePasswordRequest("current", password, password);

      assertThat(request.isValidPasswordLength()).isTrue();
    }

    @Test
    @DisplayName("パスワードが7文字の場合はfalseを返す")
    void shouldReturnFalseWhenPasswordIs7Characters() {
      ChangePasswordRequest request = new ChangePasswordRequest("current", "1234567", "1234567");

      assertThat(request.isValidPasswordLength()).isFalse();
    }

    @Test
    @DisplayName("パスワードが65文字の場合はfalseを返す")
    void shouldReturnFalseWhenPasswordIs65Characters() {
      String password = "1".repeat(65);
      ChangePasswordRequest request = new ChangePasswordRequest("current", password, password);

      assertThat(request.isValidPasswordLength()).isFalse();
    }

    @Test
    @DisplayName("パスワードがnullの場合はfalseを返す")
    void shouldReturnFalseWhenPasswordIsNull() {
      ChangePasswordRequest request = new ChangePasswordRequest("current", null, null);

      assertThat(request.isValidPasswordLength()).isFalse();
    }
  }
}
