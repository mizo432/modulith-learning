package undecided.authorization.infra.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.authorization.domain.model.User;

/**
 * ユーザーリポジトリ
 * <p>
 * ユーザーエンティティのデータアクセスを提供するリポジトリインターフェースです。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * ユーザー名でユーザーを検索します。
   *
   * @param username ユーザー名
   * @return ユーザーのOptional
   */
  Optional<User> findByUsername(String username);

  /**
   * メールアドレスでユーザーを検索します。
   *
   * @param email メールアドレス
   * @return ユーザーのOptional
   */
  Optional<User> findByEmail(String email);

  /**
   * ユーザー名が存在するかチェックします。
   *
   * @param username ユーザー名
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByUsername(String username);

  /**
   * メールアドレスが存在するかチェックします。
   *
   * @param email メールアドレス
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByEmail(String email);
}
