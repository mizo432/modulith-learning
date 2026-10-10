package undecided.generic.accountMgmt.internal.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.generic.accountMgmt.spi.User;
import undecided.generic.accountMgmt.spi.UserQuery;

/**
 * ユーザー情報の取得（Query）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、読み取り操作のみを担当します。
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserQueryApi {

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

  /** ユーザーステータスレスポンスです。 */
  public record UserStatusResponse(String username, boolean needsPasswordChange) {}
}
