package undecided.erp.scrum.application.query.project;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.project.Project;
import undecided.erp.scrum.domain.model.project.ProjectMember;
import undecided.erp.scrum.domain.model.project.ProjectMemberRepository;
import undecided.erp.scrum.domain.model.project.ProjectMemberStatus;
import undecided.erp.scrum.domain.model.project.ProjectRepository;
import undecided.erp.scrum.domain.model.project.ProjectRole;
import undecided.erp.scrum.domain.model.project.ProjectRoleRepository;
import undecided.erp.scrum.domain.model.project.ProjectStatus;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * プロジェクトに関連するクエリサービスを提供するクラス。
 *
 * <p>このサービスは、プロジェクトの検索、詳細表示、ダッシュボード表示などの 読み取り専用の操作を提供します。
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectQuery {

  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectRoleRepository projectRoleRepository;

  /**
   * 指定されたIDのプロジェクトを取得します。
   *
   * @param projectId プロジェクトID
   * @return プロジェクト（存在しない場合はEmpty）
   */
  public Optional<Project> findProjectById(SnowflakeId projectId) {
    return projectRepository.findById(projectId);
  }

  /**
   * すべてのプロジェクトを取得します。
   *
   * @return プロジェクトのリスト
   */
  public List<Project> findAllProjects() {
    return projectRepository.findAll();
  }

  /**
   * アクティブなプロジェクトを取得します。
   *
   * @return アクティブなプロジェクトのリスト
   */
  public List<Project> findActiveProjects() {
    return projectRepository.findByStatus(ProjectStatus.ACTIVE);
  }

  /**
   * アーカイブされたプロジェクトを取得します。
   *
   * @return アーカイブされたプロジェクトのリスト
   */
  public List<Project> findArchivedProjects() {
    return projectRepository.findByStatus(ProjectStatus.ARCHIVED);
  }

  /**
   * 指定されたプロジェクトマネージャーが管理するプロジェクトを取得します。
   *
   * @param projectManager プロジェクトマネージャー
   * @return プロジェクトのリスト
   */
  public List<Project> findProjectsByManager(Member projectManager) {
    return projectRepository.findByProjectManager(projectManager);
  }

  /**
   * 指定された名前を含むプロジェクトを検索します。
   *
   * @param name プロジェクト名
   * @return プロジェクトのリスト
   */
  public List<Project> searchProjectsByName(String name) {
    return projectRepository.findByNameContaining(name);
  }

  /**
   * 指定されたプロジェクトのメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return プロジェクトメンバーのリスト
   */
  public List<ProjectMember> findProjectMembers(SnowflakeId projectId) {
    Optional<Project> projectOpt = projectRepository.findById(projectId);
    return projectOpt.map(projectMemberRepository::findByProject).orElse(List.of());
  }

  /**
   * 指定されたプロジェクトのアクティブなメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return アクティブなプロジェクトメンバーのリスト
   */
  public List<ProjectMember> findActiveProjectMembers(SnowflakeId projectId) {
    Optional<Project> projectOpt = projectRepository.findById(projectId);
    return projectOpt
        .map(
            project ->
                projectMemberRepository.findByProjectAndStatus(project, ProjectMemberStatus.ACTIVE))
        .orElse(List.of());
  }

  /**
   * 指定されたプロジェクトの招待中のメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return 招待中のプロジェクトメンバーのリスト
   */
  public List<ProjectMember> findInvitedProjectMembers(SnowflakeId projectId) {
    Optional<Project> projectOpt = projectRepository.findById(projectId);
    return projectOpt
        .map(
            project ->
                projectMemberRepository.findByProjectAndStatus(
                    project, ProjectMemberStatus.INVITED))
        .orElse(List.of());
  }

  /**
   * 指定されたメンバーが所属するプロジェクトを取得します。
   *
   * @param member メンバー
   * @return プロジェクトメンバーのリスト
   */
  public List<ProjectMember> findMemberProjects(Member member) {
    return projectMemberRepository.findByMember(member);
  }

  /**
   * 指定されたメンバーがアクティブに参加しているプロジェクトを取得します。
   *
   * @param member メンバー
   * @return アクティブなプロジェクトメンバーのリスト
   */
  public List<ProjectMember> findActiveMemberProjects(Member member) {
    return projectMemberRepository.findByMemberAndStatus(member, ProjectMemberStatus.ACTIVE);
  }

  /**
   * 指定されたメンバーが招待されているプロジェクトを取得します。
   *
   * @param member メンバー
   * @return 招待中のプロジェクトメンバーのリスト
   */
  public List<ProjectMember> findInvitedMemberProjects(Member member) {
    return projectMemberRepository.findByMemberAndStatus(member, ProjectMemberStatus.INVITED);
  }

  /**
   * すべてのプロジェクトロールを取得します。
   *
   * @return プロジェクトロールのリスト
   */
  public List<ProjectRole> findAllRoles() {
    return projectRoleRepository.findAll();
  }

  /**
   * 指定された名前のプロジェクトロールを取得します。
   *
   * @param name ロール名
   * @return プロジェクトロール（存在しない場合はEmpty）
   */
  public Optional<ProjectRole> findRoleByName(String name) {
    return projectRoleRepository.findByName(name);
  }

  /**
   * 編集権限を持つプロジェクトロールを取得します。
   *
   * @return 編集権限を持つプロジェクトロールのリスト
   */
  public List<ProjectRole> findRolesWithEditPermission() {
    return projectRoleRepository.findByCanEditTrue();
  }

  /**
   * 招待権限を持つプロジェクトロールを取得します。
   *
   * @return 招待権限を持つプロジェクトロールのリスト
   */
  public List<ProjectRole> findRolesWithInvitePermission() {
    return projectRoleRepository.findByCanInviteTrue();
  }

  /**
   * ロール管理権限を持つプロジェクトロールを取得します。
   *
   * @return ロール管理権限を持つプロジェクトロールのリスト
   */
  public List<ProjectRole> findRolesWithRoleManagementPermission() {
    return projectRoleRepository.findByCanManageRolesTrue();
  }
}
