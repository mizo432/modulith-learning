package undecided.authorization.business.service;

import java.util.List;
import java.util.Optional;
import undecided.authorization.domain.model.User;

/**
 * ユーザーサービス
 * <p>
 * ユーザー管理に関するビジネスロジックを提供するサービスインターフェースです。
 */
public interface UserService {

  /**
   * すべてのユーザーを取得します。
   *
   * @return ユーザーのリスト
   */
  List<User> findAllUsers();

  /**
   * IDでユーザーを検索します。
   *
   * @param id ユーザーID
   * @return ユーザーのOptional
   */
  Optional<User> findUserById(Long id);

  /**
   * ユーザー名でユーザーを検索します。
   *
   * @param username ユーザー名
   * @return ユーザーのOptional
   */
  Optional<User> findUserByUsername(String username);

  /**
   * メールアドレスでユーザーを検索します。
   *
   * @param email メールアドレス
   * @return ユーザーのOptional
   */
  Optional<User> findUserByEmail(String email);

  /**
   * ユーザーを作成します。
   *
   * @param user 作成するユーザー
   * @return 作成されたユーザー
   */
  User createUser(User user);

  /**
   * ユーザーを更新します。
   *
   * @param user 更新するユーザー
   * @return 更新されたユーザー
   */
  User updateUser(User user);

  /**
   * ユーザーを削除します。
   *
   * @param id 削除するユーザーのID
   */
  void deleteUser(Long id);

  /**
   * ユーザーにロールを追加します。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  User addRoleToUser(Long userId, String roleName);

  /**
   * ユーザーからロールを削除します。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  User removeRoleFromUser(Long userId, String roleName);
}
