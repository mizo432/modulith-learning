package undecided.authorization.domain.model.tenant;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * テナントリポジトリ
 *
 * <p>テナントエンティティのデータアクセスを提供するリポジトリインターフェースです。
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

  /**
   * テナントコードでテナントを検索します。
   *
   * @param tenantCode テナントコード
   * @return テナントのOptional
   */
  Optional<Tenant> findByTenantCode(String tenantCode);

  /**
   * テナントコードが存在するかチェックします。
   *
   * @param tenantCode テナントコード
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByTenantCode(String tenantCode);
}
