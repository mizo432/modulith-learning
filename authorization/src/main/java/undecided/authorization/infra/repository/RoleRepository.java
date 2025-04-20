package undecided.authorization.infra.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.authorization.domain.model.Role;

/**
 * ロールリポジトリ
 * <p>
 * ロールエンティティのデータアクセスを提供するリポジトリインターフェースです。
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * ロール名でロールを検索します。
   *
   * @param name ロール名
   * @return ロールのOptional
   */
  Optional<Role> findByName(String name);

  /**
   * ロール名が存在するかチェックします。
   *
   * @param name ロール名
   * @return 存在する場合はtrue、そうでない場合はfalse
   */
  boolean existsByName(String name);
}
