package undecided.generic.accountMgmt.internal.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import undecided.generic.accountMgmt.ChangePasswordRequest;
import com.github.mizo432.shared.exception.BusinessException;

@Tag("small")
@DisplayName("UserCommandImplのテスト")
class UserCommandImplTest {

  private MockRestServiceServer server;
  private UserCommandImpl userCommandImpl;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder();
    server = MockRestServiceServer.bindTo(builder).build();
    userCommandImpl = new UserCommandImpl(builder.build());
  }

  @Nested
  @DisplayName("changePasswordメソッドのテスト")
  class ChangePasswordTest {

    @Test
    @DisplayName("初回ログイン時にauthorizationサービスに委譲してパスワードを変更できる")
    void shouldChangePasswordOnFirstLogin() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "newPassword123");

      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
          .andExpect(method(HttpMethod.GET))
          .andRespond(
              withSuccess(
                  "{\"username\":\"testuser\",\"isFirstLogin\":true}",
                  MediaType.APPLICATION_JSON));
      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login-password"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withNoContent());

      userCommandImpl.changePassword("testuser", request);

      server.verify();
    }

    @Test
    @DisplayName("初回ログイン以外でパスワードを変更できる")
    void shouldChangePasswordWhenNotFirstLogin() {
      ChangePasswordRequest request =
          new ChangePasswordRequest("currentPass", "newPassword123", "newPassword123");

      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
          .andExpect(method(HttpMethod.GET))
          .andRespond(
              withSuccess(
                  "{\"username\":\"testuser\",\"isFirstLogin\":false}",
                  MediaType.APPLICATION_JSON));
      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/password"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withNoContent());

      userCommandImpl.changePassword("testuser", request);

      server.verify();
    }

    @Test
    @DisplayName("新しいパスワードが短い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooShort() {
      ChangePasswordRequest request = new ChangePasswordRequest(null, "abcdefg", "abcdefg");

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("8文字以上");
    }

    @Test
    @DisplayName("新しいパスワードが長い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooLong() {
      String longPassword = "a".repeat(65);
      ChangePasswordRequest request = new ChangePasswordRequest(null, longPassword, longPassword);

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("64文字以下");
    }

    @Test
    @DisplayName("新しいパスワードと確認用パスワードが一致しない場合は例外がスローされる")
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "differentPass");

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("一致しません");
    }

    @Test
    @DisplayName("authorizationサービスがエラーを返した場合は例外がスローされる")
    void shouldThrowExceptionWhenAuthorizationServiceFails() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "newPassword123");

      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login"))
          .andExpect(method(HttpMethod.GET))
          .andRespond(
              withSuccess(
                  "{\"username\":\"testuser\",\"isFirstLogin\":true}",
                  MediaType.APPLICATION_JSON));
      server
          .expect(requestTo("http://localhost:8082/api/auth/users/testuser/first-login-password"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withStatus(HttpStatus.BAD_REQUEST));

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("初回ログイン時のパスワード変更に失敗しました");
    }
  }
}
