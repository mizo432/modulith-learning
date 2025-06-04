package undecided.erp.scrum.application.command.project;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.project.Project;
import undecided.erp.scrum.domain.model.project.ProjectMember;
import undecided.erp.scrum.domain.model.project.ProjectMemberRepository;
import undecided.erp.scrum.domain.model.project.ProjectRepository;
import undecided.erp.scrum.domain.model.project.ProjectRole;
import undecided.erp.scrum.domain.model.project.ProjectRoleRepository;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * プロジェクトメンバーに関連するビジネスロジックを提供するサービスクラス。
 *
 * <p>このサービスは、メンバーの招待、権限設定などのプロジェクトメンバー管理機能を提供します。
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberCommand {

  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectRoleRepository projectRoleRepository;

  /**
   * メンバーをプロジェクトに招待します。
   *
   * @param projectId プロジェクトID
   * @param member 招待するメンバー
   * @param roleId ロールID
   * @return 招待されたプロジェクトメンバー（プロジェクトまたはロールが存在しない場合はEmpty）
   */
  @Transactional
  public Optional<ProjectMember> inviteMember(
      SnowflakeId projectId, Member member, SnowflakeId roleId) {
    Optional<Project> projectOpt = projectRepository.findById(projectId);
    Optional<ProjectRole> roleOpt = projectRoleRepository.findById(roleId);

    if (projectOpt.isPresent() && roleOpt.isPresent()) {
      // 既に招待されているか確認
      Optional<ProjectMember> existingMember =
          projectMemberRepository.findByProjectAndMember(projectOpt.get(), member);
      if (existingMember.isPresent()) {
        return existingMember;
      }

      // 新しい招待を作成
      ProjectMember projectMember = ProjectMember.invite(projectOpt.get(), member, roleOpt.get());
      return Optional.of(projectMemberRepository.save(projectMember));
    }
    return Optional.empty();
  }

  /**
   * 招待を承諾します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @return 更新されたプロジェクトメンバー（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<ProjectMember> acceptInvitation(SnowflakeId projectMemberId) {
    return projectMemberRepository
        .findById(projectMemberId)
        .map(
            projectMember -> {
              projectMember.acceptInvitation();
              return projectMemberRepository.save(projectMember);
            });
  }

  /**
   * 招待を拒否します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @return 更新されたプロジェクトメンバー（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<ProjectMember> declineInvitation(SnowflakeId projectMemberId) {
    return projectMemberRepository
        .findById(projectMemberId)
        .map(
            projectMember -> {
              projectMember.declineInvitation();
              return projectMemberRepository.save(projectMember);
            });
  }

  /**
   * メンバーのロールを変更します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @param roleId 新しいロールID
   * @return 更新されたプロジェクトメンバー（プロジェクトメンバーまたはロールが存在しない場合はEmpty）
   */
  @Transactional
  public Optional<ProjectMember> changeRole(SnowflakeId projectMemberId, SnowflakeId roleId) {
    Optional<ProjectMember> memberOpt = projectMemberRepository.findById(projectMemberId);
    Optional<ProjectRole> roleOpt = projectRoleRepository.findById(roleId);

    if (memberOpt.isPresent() && roleOpt.isPresent()) {
      ProjectMember projectMember = memberOpt.get();
      projectMember.changeRole(roleOpt.get());
      return Optional.of(projectMemberRepository.save(projectMember));
    }
    return Optional.empty();
  }

  /**
   * メンバーをプロジェクトから削除します。
   *
   * @param projectMemberId プロジェクトメンバーID
   */
  @Transactional
  public void removeMember(SnowflakeId projectMemberId) {
    projectMemberRepository.deleteById(projectMemberId);
  }

  /**
   * 新しいプロジェクトロールを作成します。
   *
   * @param name ロール名
   * @param description ロールの説明
   * @param canEdit 編集権限があるかどうか
   * @param canInvite 招待権限があるかどうか
   * @param canManageRoles ロール管理権限があるかどうか
   * @return 作成されたプロジェクトロール
   */
  @Transactional
  public ProjectRole createRole(
      String name, String description, boolean canEdit, boolean canInvite, boolean canManageRoles) {
    ProjectRole role = ProjectRole.create(name, description, canEdit, canInvite, canManageRoles);
    return projectRoleRepository.save(role);
  }

  /** デフォルトのプロジェクトロールを作成します。 管理者、メンバー、閲覧者の3つのロールを作成します。 */
  @Transactional
  public void createDefaultRoles() {
    if (projectRoleRepository.count() == 0) {
      projectRoleRepository.save(ProjectRole.createAdminRole());
      projectRoleRepository.save(ProjectRole.createMemberRole());
      projectRoleRepository.save(ProjectRole.createViewerRole());
    }
  }
}
