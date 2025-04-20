package undecided.authorization.infra.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.authorization.domain.model.Permission;

/**
 * 権限リポジトリ
 * <p>
 * 権限エンティティのデータアクセスを提供するリポジトリインターフェースです。
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  /**
   * 権限名で権限を検索します。
   *
   * @param name 権限名
   * @return 権限のOptional
   */
  Optional<Permission> findByName(String name);

  /**
   * 権限名が存在するかチェックします。
   *
   * @param name 権限名
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByName(String name);
}
