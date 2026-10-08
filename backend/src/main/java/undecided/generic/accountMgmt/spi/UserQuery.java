package undecided.generic.accountMgmt.spi;

import java.util.Optional;

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
