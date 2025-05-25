package undecided.erp.scrum.domain.model.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * ProjectRoleRepositoryは、ProjectRoleエンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、
 * プロジェクトロールエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, SnowflakeId> {

  /**
   * 指定された名前のプロジェクトロールを検索します。
   *
   * @param name ロール名
   * @return 指定された名前のプロジェクトロール（存在する場合）
   */
  Optional<ProjectRole> findByName(String name);

  /**
   * 編集権限を持つプロジェクトロールを検索します。
   *
   * @return 編集権限を持つプロジェクトロールのリスト
   */
  List<ProjectRole> findByCanEditTrue();

  /**
   * 招待権限を持つプロジェクトロールを検索します。
   *
   * @return 招待権限を持つプロジェクトロールのリスト
   */
  List<ProjectRole> findByCanInviteTrue();

  /**
   * ロール管理権限を持つプロジェクトロールを検索します。
   *
   * @return ロール管理権限を持つプロジェクトロールのリスト
   */
  List<ProjectRole> findByCanManageRolesTrue();
}
