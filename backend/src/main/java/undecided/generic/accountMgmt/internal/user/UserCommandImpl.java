package undecided.generic.accountMgmt.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import undecided.generic.accountMgmt.ChangePasswordRequest;
import undecided.generic.accountMgmt.UserCommand;
import undecided.shared.exception.BusinessException;

/**
 * ユーザー操作の実装クラスです。
 *
 * <p>パスワード変更などの機微情報処理はauthorizationサービスに委譲します。
 */
@RequiredArgsConstructor
@Service
public class UserCommandImpl implements UserCommand {

  private static final String AUTHORIZATION_SERVICE_URL = "http://localhost:8082";
  private final RestTemplate restTemplate;

  @Override
  public void changePassword(String username, ChangePasswordRequest request) {
    // パスワード検証
    if (!request.isValidPasswordLength()) {
      throw new BusinessException("新しいパスワードは8文字以上64文字以下で指定してください");
    }

    if (!request.isNewPasswordMatched()) {
      throw new BusinessException("新しいパスワードと確認用パスワードが一致しません");
    }

    // authorizationサービスにパスワード変更を委譲
    try {
      // 初回ログインかどうかを確認
      boolean isFirstLogin = checkIfFirstLogin(username);

      if (isFirstLogin) {
        // 初回ログイン時のパスワード変更
        changePasswordOnFirstLogin(username, request.newPassword());
      } else {
        // 通常時のパスワード変更
        changePasswordWithCurrentPassword(
            username, request.currentPassword(), request.newPassword());
      }
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      throw new BusinessException("パスワード変更に失敗しました: " + e.getMessage());
    }
  }

  /**
   * 初回ログインかどうかを確認します。
   *
   * @param username ユーザー名
   * @return 初回ログインの場合はtrue
   */
  private boolean checkIfFirstLogin(String username) {
    String url = AUTHORIZATION_SERVICE_URL + "/api/auth/users/" + username + "/first-login";
    ResponseEntity<UserStatusDto> response = restTemplate.getForEntity(url, UserStatusDto.class);

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      return response.getBody().isFirstLogin();
    }
    return false;
  }

  /**
   * 初回ログイン時のパスワード変更を行います。
   *
   * @param username ユーザー名
   * @param newPassword 新しいパスワード
   */
  private void changePasswordOnFirstLogin(String username, String newPassword) {
    String url =
        AUTHORIZATION_SERVICE_URL + "/api/auth/users/" + username + "/first-login-password";
    PasswordChangeDto requestDto = new PasswordChangeDto(null, newPassword);

    ResponseEntity<Void> response = restTemplate.postForEntity(url, requestDto, Void.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new BusinessException("初回ログイン時のパスワード変更に失敗しました");
    }
  }

  /**
   * 通常時のパスワード変更を行います。
   *
   * @param username ユーザー名
   * @param currentPassword 現在のパスワード
   * @param newPassword 新しいパスワード
   */
  private void changePasswordWithCurrentPassword(
      String username, String currentPassword, String newPassword) {
    String url = AUTHORIZATION_SERVICE_URL + "/api/auth/users/" + username + "/password";
    PasswordChangeDto requestDto = new PasswordChangeDto(currentPassword, newPassword);

    ResponseEntity<Void> response = restTemplate.postForEntity(url, requestDto, Void.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new BusinessException("パスワード変更に失敗しました");
    }
  }

  /** パスワード変更リクエストDTOです。 */
  public record PasswordChangeDto(String currentPassword, String newPassword) {}

  /** ユーザーステータスDTOです。 */
  public record UserStatusDto(String username, boolean isFirstLogin) {}
}
