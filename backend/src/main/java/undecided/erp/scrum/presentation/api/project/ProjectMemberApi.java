package undecided.erp.scrum.presentation.api.project;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.application.command.project.ProjectMemberCommand;
import undecided.erp.scrum.application.query.project.ProjectQuery;
import undecided.erp.scrum.domain.model.project.ProjectMember;
import undecided.erp.scrum.domain.model.project.ProjectRole;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * プロジェクトメンバー管理に関連するAPIを提供するコントローラークラス。
 *
 * <p>このクラスは、メンバーの招待、権限設定などのプロジェクトメンバー管理機能を提供します。
 */
@RestController
@RequestMapping("/api/project-members")
@RequiredArgsConstructor
public class ProjectMemberApi {

  private final ProjectMemberCommand projectMemberCommand;
  private final ProjectQuery projectQuery;

  /**
   * 指定されたプロジェクトのメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return プロジェクトメンバーのリスト
   */
  @GetMapping("/project/{projectId}")
  public ResponseEntity<List<ProjectMember>> getProjectMembers(@PathVariable String projectId) {
    List<ProjectMember> members =
        projectQuery.findProjectMembers(SnowflakeId.of(Long.parseLong(projectId)));
    return ResponseEntity.ok(members);
  }

  /**
   * 指定されたプロジェクトのアクティブなメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return アクティブなプロジェクトメンバーのリスト
   */
  @GetMapping("/project/{projectId}/active")
  public ResponseEntity<List<ProjectMember>> getActiveProjectMembers(
      @PathVariable String projectId) {
    List<ProjectMember> members =
        projectQuery.findActiveProjectMembers(SnowflakeId.of(Long.parseLong(projectId)));
    return ResponseEntity.ok(members);
  }

  /**
   * 指定されたプロジェクトの招待中のメンバーを取得します。
   *
   * @param projectId プロジェクトID
   * @return 招待中のプロジェクトメンバーのリスト
   */
  @GetMapping("/project/{projectId}/invited")
  public ResponseEntity<List<ProjectMember>> getInvitedProjectMembers(
      @PathVariable String projectId) {
    List<ProjectMember> members =
        projectQuery.findInvitedProjectMembers(SnowflakeId.of(Long.parseLong(projectId)));
    return ResponseEntity.ok(members);
  }

  /**
   * メンバーをプロジェクトに招待します。
   *
   * @param request 招待リクエスト
   * @return 招待されたプロジェクトメンバー
   */
  @PostMapping("/invite")
  public ResponseEntity<ProjectMember> inviteMember(@RequestBody InviteMemberRequest request) {
    return projectMemberCommand
        .inviteMember(
            SnowflakeId.of(Long.parseLong(request.getProjectId())),
            request.getMember(),
            SnowflakeId.of(Long.parseLong(request.getRoleId())))
        .map(member -> ResponseEntity.status(HttpStatus.CREATED).body(member))
        .orElse(ResponseEntity.badRequest().build());
  }

  /**
   * 招待を承諾します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @return 更新されたプロジェクトメンバー
   */
  @PutMapping("/{projectMemberId}/accept")
  public ResponseEntity<ProjectMember> acceptInvitation(@PathVariable String projectMemberId) {
    return projectMemberCommand
        .acceptInvitation(SnowflakeId.of(Long.parseLong(projectMemberId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 招待を拒否します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @return 更新されたプロジェクトメンバー
   */
  @PutMapping("/{projectMemberId}/decline")
  public ResponseEntity<ProjectMember> declineInvitation(@PathVariable String projectMemberId) {
    return projectMemberCommand
        .declineInvitation(SnowflakeId.of(Long.parseLong(projectMemberId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * メンバーのロールを変更します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @param request ロール変更リクエスト
   * @return 更新されたプロジェクトメンバー
   */
  @PutMapping("/{projectMemberId}/role")
  public ResponseEntity<ProjectMember> changeRole(
      @PathVariable String projectMemberId, @RequestBody ChangeRoleRequest request) {
    return projectMemberCommand
        .changeRole(
            SnowflakeId.of(Long.parseLong(projectMemberId)),
            SnowflakeId.of(Long.parseLong(request.getRoleId())))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * メンバーをプロジェクトから削除します。
   *
   * @param projectMemberId プロジェクトメンバーID
   * @return 空のレスポンス
   */
  @DeleteMapping("/{projectMemberId}")
  public ResponseEntity<Void> removeMember(@PathVariable String projectMemberId) {
    projectMemberCommand.removeMember(SnowflakeId.of(Long.parseLong(projectMemberId)));
    return ResponseEntity.noContent().build();
  }

  /**
   * すべてのプロジェクトロールを取得します。
   *
   * @return プロジェクトロールのリスト
   */
  @GetMapping("/roles")
  public ResponseEntity<List<ProjectRole>> getAllRoles() {
    List<ProjectRole> roles = projectQuery.findAllRoles();
    return ResponseEntity.ok(roles);
  }

  /**
   * 新しいプロジェクトロールを作成します。
   *
   * @param request ロール作成リクエスト
   * @return 作成されたプロジェクトロール
   */
  @PostMapping("/roles")
  public ResponseEntity<ProjectRole> createRole(@RequestBody CreateRoleRequest request) {
    ProjectRole role =
        projectMemberCommand.createRole(
            request.getName(),
            request.getDescription(),
            request.isCanEdit(),
            request.isCanInvite(),
            request.isCanManageRoles());
    return ResponseEntity.status(HttpStatus.CREATED).body(role);
  }

  /**
   * デフォルトのプロジェクトロールを作成します。
   *
   * @return 空のレスポンス
   */
  @PostMapping("/roles/default")
  public ResponseEntity<Void> createDefaultRoles() {
    projectMemberCommand.createDefaultRoles();
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** メンバー招待リクエスト。 */
  @Setter
  @Getter
  public static class InviteMemberRequest {
    private String projectId;
    private Member member;
    private String roleId;
  }

  /** ロール変更リクエスト。 */
  @Setter
  @Getter
  public static class ChangeRoleRequest {
    private String roleId;
  }

  /** ロール作成リクエスト。 */
  @Setter
  @Getter
  public static class CreateRoleRequest {
    private String name;
    private String description;
    private boolean canEdit;
    private boolean canInvite;
    private boolean canManageRoles;
  }
}
