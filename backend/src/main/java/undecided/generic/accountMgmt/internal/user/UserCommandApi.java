package undecided.generic.accountMgmt.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.generic.accountMgmt.spi.ChangePasswordRequest;
import undecided.generic.accountMgmt.spi.UserCommand;

/**
 * ユーザー情報の変更（Command）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、書き込み操作のみを担当します。
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCommandApi {

  private final UserCommand userCommand;

  /**
   * パスワードを変更します。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   * @return 204 No Contentレスポンス
   */
  @PutMapping("/{username}/password")
  ResponseEntity<Void> changePassword(
      @PathVariable String username, @RequestBody ChangePasswordRequest request) {
    userCommand.changePassword(username, request);
    return ResponseEntity.noContent().build();
  }
}
