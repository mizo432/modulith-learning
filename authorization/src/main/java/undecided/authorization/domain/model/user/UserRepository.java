package undecided.authorization.domain.model.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.authorization.domain.model.tenant.Tenant;

/**
 * ユーザーリポジトリ
 *
 * <p>ユーザーエンティティのデータアクセスを提供するリポジトリインターフェースです。
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

  /**
   * テナントとユーザー名でユーザーを検索します。
   *
   * @param tenant テナント
   * @param username ユーザー名
   * @return ユーザーのOptional
   */
  Optional<User> findByTenantAndUsername(Tenant tenant, String username);

  /**
   * テナントとメールアドレスでユーザーを検索します。
   *
   * @param tenant テナント
   * @param email メールアドレス
   * @return ユーザーのOptional
   */
  Optional<User> findByTenantAndEmail(Tenant tenant, String email);

  /**
   * テナントとユーザー名でユーザーが存在するかチェックします。
   *
   * @param tenant テナント
   * @param username ユーザー名
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByTenantAndUsername(Tenant tenant, String username);

  /**
   * テナントとメールアドレスでユーザーが存在するかチェックします。
   *
   * @param tenant テナント
   * @param email メールアドレス
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByTenantAndEmail(Tenant tenant, String email);

  /**
   * テナントIDでユーザーを検索します。
   *
   * @param tenantId テナントID
   * @return ユーザーのOptional
   */
  @Query("SELECT u FROM User u WHERE u.tenant.id = :tenantId AND u.username = :username")
  Optional<User> findByTenantIdAndUsername(
      @Param("tenantId") Long tenantId, @Param("username") String username);
}
