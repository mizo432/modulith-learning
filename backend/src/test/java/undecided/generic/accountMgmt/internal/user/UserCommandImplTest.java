package undecided.generic.accountMgmt.internal.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import undecided.generic.accountMgmt.ChangePasswordRequest;
import undecided.generic.accountMgmt.internal.user.UserCommandImpl.PasswordChangeDto;
import undecided.generic.accountMgmt.internal.user.UserCommandImpl.UserStatusDto;
import com.github.mizo432.shared.exception.BusinessException;

@Tag("small")
@DisplayName("UserCommandImplのテスト")
@ExtendWith(MockitoExtension.class)
class UserCommandImplTest {

  @Mock private RestTemplate restTemplate;

  private UserCommandImpl userCommandImpl;

  @BeforeEach
  void setUp() {
    userCommandImpl = new UserCommandImpl(restTemplate);
  }

  @Nested
  @DisplayName("changePasswordメソッドのテスト")
  class ChangePasswordTest {

    @Test
    @DisplayName("初回ログイン時にauthorizationサービスに委譲してパスワードを変更できる")
    void shouldChangePasswordOnFirstLogin() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "newPassword123");

      // 初回ログイン状態をtrueにモック
      when(restTemplate.getForEntity(anyString(), eq(UserStatusDto.class)))
          .thenReturn(new ResponseEntity<>(new UserStatusDto("testuser", true), HttpStatus.OK));

      // 初回ログインパスワード変更のレスポンスをモック
      when(restTemplate.postForEntity(anyString(), any(PasswordChangeDto.class), eq(Void.class)))
          .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

      userCommandImpl.changePassword("testuser", request);

      // 初回ログインパスワード変更APIが呼び出されることを検証
      verify(restTemplate).postForEntity(contains("/first-login-password"), any(), eq(Void.class));
    }

    @Test
    @DisplayName("初回ログイン以外でパスワードを変更できる")
    void shouldChangePasswordWhenNotFirstLogin() {
      ChangePasswordRequest request =
          new ChangePasswordRequest("currentPass", "newPassword123", "newPassword123");

      // 初回ログイン状態をfalseにモック
      when(restTemplate.getForEntity(anyString(), eq(UserStatusDto.class)))
          .thenReturn(new ResponseEntity<>(new UserStatusDto("testuser", false), HttpStatus.OK));

      // パスワード変更のレスポンスをモック
      when(restTemplate.postForEntity(anyString(), any(PasswordChangeDto.class), eq(Void.class)))
          .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

      userCommandImpl.changePassword("testuser", request);

      // 通常パスワード変更APIが呼び出されることを検証
      verify(restTemplate).postForEntity(contains("/password"), any(), eq(Void.class));
    }

    @Test
    @DisplayName("新しいパスワードが短い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooShort() {
      ChangePasswordRequest request = new ChangePasswordRequest(null, "short", "short");

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

      // 初回ログイン状態をtrueにモック
      when(restTemplate.getForEntity(anyString(), eq(UserStatusDto.class)))
          .thenReturn(new ResponseEntity<>(new UserStatusDto("testuser", true), HttpStatus.OK));

      // パスワード変更でエラーを返す
      when(restTemplate.postForEntity(anyString(), any(PasswordChangeDto.class), eq(Void.class)))
          .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("初回ログイン時のパスワード変更に失敗しました");
    }
  }
}
