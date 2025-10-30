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
import undecided.erp.scrum.application.command.project.ProjectCommand;
import undecided.erp.scrum.application.query.project.ProjectQuery;
import undecided.erp.scrum.domain.model.project.Project;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * プロジェクト管理に関連するAPIを提供するコントローラークラス。
 *
 * <p>このクラスは、プロジェクトの作成、編集、アーカイブなどの操作を提供します。
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectApi {

  private final ProjectCommand projectCommand;
  private final ProjectQuery projectQuery;

  /**
   * すべてのプロジェクトを取得します。
   *
   * @return プロジェクトのリスト
   */
  @GetMapping
  public ResponseEntity<List<Project>> getAllProjects() {
    List<Project> projects = projectQuery.findAllProjects();
    return ResponseEntity.ok(projects);
  }

  /**
   * 指定されたIDのプロジェクトを取得します。
   *
   * @param projectId プロジェクトID
   * @return プロジェクト
   */
  @GetMapping("/{projectId}")
  public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
    return projectQuery
        .findProjectById(SnowflakeId.of(Long.parseLong(projectId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 新しいプロジェクトを作成します。
   *
   * @param request プロジェクト作成リクエスト
   * @return 作成されたプロジェクト
   */
  @PostMapping
  public ResponseEntity<Project> createProject(@RequestBody CreateProjectRequest request) {
    Project project =
        projectCommand.createProject(
            request.getName(), request.getDescription(), request.getProjectManager());
    return ResponseEntity.status(HttpStatus.CREATED).body(project);
  }

  /**
   * プロジェクトを編集します。
   *
   * @param projectId プロジェクトID
   * @param request プロジェクト編集リクエスト
   * @return 更新されたプロジェクト
   */
  @PutMapping("/{projectId}")
  public ResponseEntity<Project> updateProject(
      @PathVariable String projectId, @RequestBody UpdateProjectRequest request) {
    return projectCommand
        .editProject(
            SnowflakeId.of(Long.parseLong(projectId)),
            request.getName(),
            request.getDescription(),
            request.getProjectManager())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * プロジェクトをアーカイブします。
   *
   * @param projectId プロジェクトID
   * @return アーカイブされたプロジェクト
   */
  @PutMapping("/{projectId}/archive")
  public ResponseEntity<Project> archiveProject(@PathVariable String projectId) {
    return projectCommand
        .archiveProject(SnowflakeId.of(Long.parseLong(projectId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * プロジェクトを再アクティブ化します。
   *
   * @param projectId プロジェクトID
   * @return 再アクティブ化されたプロジェクト
   */
  @PutMapping("/{projectId}/activate")
  public ResponseEntity<Project> activateProject(@PathVariable String projectId) {
    return projectCommand
        .activateProject(SnowflakeId.of(Long.parseLong(projectId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * プロジェクトを削除します。
   *
   * @param projectId プロジェクトID
   * @return 空のレスポンス
   */
  @DeleteMapping("/{projectId}")
  public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
    projectCommand.deleteProject(SnowflakeId.of(Long.parseLong(projectId)));
    return ResponseEntity.noContent().build();
  }

  /**
   * アクティブなプロジェクトを取得します。
   *
   * @return アクティブなプロジェクトのリスト
   */
  @GetMapping("/active")
  public ResponseEntity<List<Project>> getActiveProjects() {
    List<Project> projects = projectQuery.findActiveProjects();
    return ResponseEntity.ok(projects);
  }

  /**
   * アーカイブされたプロジェクトを取得します。
   *
   * @return アーカイブされたプロジェクトのリスト
   */
  @GetMapping("/archived")
  public ResponseEntity<List<Project>> getArchivedProjects() {
    List<Project> projects = projectQuery.findArchivedProjects();
    return ResponseEntity.ok(projects);
  }

  /**
   * 指定された名前を含むプロジェクトを検索します。
   *
   * @param name プロジェクト名
   * @return プロジェクトのリスト
   */
  @GetMapping("/search/{name}")
  public ResponseEntity<List<Project>> searchProjectsByName(@PathVariable String name) {
    List<Project> projects = projectQuery.searchProjectsByName(name);
    return ResponseEntity.ok(projects);
  }

  /** プロジェクト作成リクエスト。 */
  @Setter
  @Getter
  public static class CreateProjectRequest {
    private String name;
    private String description;
    private Member projectManager;
  }

  /** プロジェクト更新リクエスト。 */
  @Setter
  @Getter
  public static class UpdateProjectRequest {
    private String name;
    private String description;
    private Member projectManager;
  }
}
