package undecided.generic.accountMgmt.internal.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import undecided.generic.accountMgmt.UserQuery;
import undecided.generic.accountMgmt.spi.User;

/**
 * ユーザー検索の実装クラスです。
 *
 * <p>ユーザー基本情報はローカルDBから取得し、機微情報（初回ログイン状態）はauthorizationサービスから取得します。
 */
@RequiredArgsConstructor
@Service
public class UserQueryImpl implements UserQuery {

  private static final String AUTHORIZATION_SERVICE_URL = "http://localhost:8082";
  private final UserRepository userRepository;
  private final RestTemplate restTemplate;

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * 初回ログイン状態を確認します。
   *
   * @param username ユーザー名
   * @return 初回ログイン状態の場合はtrue
   */
  public boolean needsPasswordChange(String username) {
    try {
      String url = AUTHORIZATION_SERVICE_URL + "/api/auth/users/" + username + "/first-login";
      ResponseEntity<UserStatusDto> response = restTemplate.getForEntity(url, UserStatusDto.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        return response.getBody().isFirstLogin();
      }
    } catch (Exception e) {
      // authorizationサービスに接続できない場合はfalseを返す
    }
    return false;
  }

  /** ユーザーステータスDTOです。 */
  public record UserStatusDto(String username, boolean isFirstLogin) {}
}
