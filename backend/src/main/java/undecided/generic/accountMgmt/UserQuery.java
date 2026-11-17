package undecided.generic.accountMgmt;

import java.util.Optional;
import undecided.generic.accountMgmt.spi.User;

/** ユーザー検索用のクエリインターフェースです。 */
public interface UserQuery {

  /**
   * ユーザー名でユーザーを検索します。
   *
   * @param username ユーザー名
   * @return 見つかった場合はUserを包んだOptional、そうでない場合は空のOptional
   */
  Optional<User> findByUsername(String username);
}
