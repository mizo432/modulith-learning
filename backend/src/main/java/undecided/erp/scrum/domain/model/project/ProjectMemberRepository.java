package undecided.erp.scrum.domain.model.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * ProjectMemberRepositoryは、ProjectMemberエンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、
 * プロジェクトメンバーエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, SnowflakeId> {

  /**
   * 指定されたプロジェクトのメンバーを検索します。
   *
   * @param project プロジェクト
   * @return プロジェクトのメンバーリスト
   */
  List<ProjectMember> findByProject(Project project);

  /**
   * 指定されたメンバーが所属するプロジェクトメンバーシップを検索します。
   *
   * @param member メンバー
   * @return メンバーが所属するプロジェクトメンバーシップのリスト
   */
  List<ProjectMember> findByMember(Member member);

  /**
   * 指定されたプロジェクトとメンバーのプロジェクトメンバーシップを検索します。
   *
   * @param project プロジェクト
   * @param member メンバー
   * @return プロジェクトメンバーシップ（存在する場合）
   */
  Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

  /**
   * 指定されたプロジェクトと状態のプロジェクトメンバーシップを検索します。
   *
   * @param project プロジェクト
   * @param status メンバーシップの状態
   * @return 条件に一致するプロジェクトメンバーシップのリスト
   */
  List<ProjectMember> findByProjectAndStatus(Project project, ProjectMemberStatus status);

  /**
   * 指定されたプロジェクトとロールのプロジェクトメンバーシップを検索します。
   *
   * @param project プロジェクト
   * @param role ロール
   * @return 条件に一致するプロジェクトメンバーシップのリスト
   */
  List<ProjectMember> findByProjectAndRole(Project project, ProjectRole role);

  /**
   * 指定されたメンバーと状態のプロジェクトメンバーシップを検索します。
   *
   * @param member メンバー
   * @param status メンバーシップの状態
   * @return 条件に一致するプロジェクトメンバーシップのリスト
   */
  List<ProjectMember> findByMemberAndStatus(Member member, ProjectMemberStatus status);
}
