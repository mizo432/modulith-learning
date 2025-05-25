package undecided.erp.scrum.domain.model.project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * ProjectRepositoryは、Projectエンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 プロジェクトエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, SnowflakeId> {

  /**
   * 指定されたプロジェクトマネージャーが管理するプロジェクトを検索します。
   *
   * @param projectManager プロジェクトマネージャー
   * @return プロジェクトマネージャーが管理するプロジェクトのリスト
   */
  List<Project> findByProjectManager(Member projectManager);

  /**
   * 指定されたステータスのプロジェクトを検索します。
   *
   * @param status プロジェクトステータス
   * @return 指定されたステータスのプロジェクトのリスト
   */
  List<Project> findByStatus(ProjectStatus status);

  /**
   * プロジェクト名に指定された文字列を含むプロジェクトを検索します。
   *
   * @param name 検索するプロジェクト名の一部
   * @return 条件に一致するプロジェクトのリスト
   */
  List<Project> findByNameContaining(String name);
}
