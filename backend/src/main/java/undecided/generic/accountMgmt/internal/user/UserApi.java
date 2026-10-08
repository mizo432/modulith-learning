package undecided.generic.accountMgmt.internal.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.generic.accountMgmt.spi.ChangePasswordRequest;
import undecided.generic.accountMgmt.spi.User;
import undecided.generic.accountMgmt.spi.UserCommand;
import undecided.generic.accountMgmt.spi.UserQuery;

/** ユーザー管理用のREST APIです。 初回ログイン時のパスワード変更機能を提供します。 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {

  private final UserCommand userCommand;
  private final UserQuery userQuery;

  /**
   * 現在のユーザーの情報を取得します。
   *
   * @param username ユーザー名
   * @return ユーザー情報（パスワードは含まない）
   */
  @GetMapping("/{username}/status")
  ResponseEntity<UserStatusResponse> getUserStatus(@PathVariable String username) {
    User user =
        userQuery
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません: " + username));

    UserStatusResponse response =
        new UserStatusResponse(user.getUsername(), user.needsPasswordChange());
    return ResponseEntity.ok(response);
  }

  /**
   * パスワードを変更します。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   * @return 変更結果
   */
  @PutMapping("/{username}/password")
  ResponseEntity<Void> changePassword(
      @PathVariable String username, @RequestBody ChangePasswordRequest request) {
    userCommand.changePassword(username, request);
    return ResponseEntity.noContent().build();
  }

  /** ユーザーステータスレスポンスです。 */
  public record UserStatusResponse(String username, boolean needsPasswordChange) {}
}
