package undecided.erp.scrum.application.command.project;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.project.Project;
import undecided.erp.scrum.domain.model.project.ProjectRepository;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * プロジェクトに関連するビジネスロジックを提供するサービスクラス。
 *
 * <p>このサービスは、プロジェクトの作成、編集、アーカイブなどの操作を提供します。
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCommand {

  private final ProjectRepository projectRepository;

  /**
   * 新しいプロジェクトを作成します。
   *
   * @param name プロジェクト名
   * @param description プロジェクトの説明
   * @param projectManager プロジェクトマネージャー
   * @param product 関連付けるプロダクト（オプション）
   * @return 作成されたプロジェクト
   */
  @Transactional
  public Project createProject(
      String name, String description, Member projectManager, Product product) {
    Project project = Project.create(name, description, projectManager, product);
    return projectRepository.save(project);
  }

  /**
   * 新しいプロジェクトを作成します（プロダクトなし）。
   *
   * @param name プロジェクト名
   * @param description プロジェクトの説明
   * @param projectManager プロジェクトマネージャー
   * @return 作成されたプロジェクト
   */
  @Transactional
  public Project createProject(String name, String description, Member projectManager) {
    return createProject(name, description, projectManager, null);
  }

  /**
   * プロジェクトを編集します。
   *
   * @param projectId 編集するプロジェクトのID
   * @param name 新しいプロジェクト名
   * @param description 新しいプロジェクトの説明
   * @param projectManager 新しいプロジェクトマネージャー
   * @param product 関連付けるプロダクト
   * @return 更新されたプロジェクト（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Project> editProject(
      SnowflakeId projectId,
      String name,
      String description,
      Member projectManager,
      Product product) {
    return projectRepository
        .findById(projectId)
        .map(
            project -> {
              project.edit(name, description, projectManager, product);
              return projectRepository.save(project);
            });
  }

  /**
   * プロジェクトを編集します（プロダクトを変更しない）。
   *
   * @param projectId 編集するプロジェクトのID
   * @param name 新しいプロジェクト名
   * @param description 新しいプロジェクトの説明
   * @param projectManager 新しいプロジェクトマネージャー
   * @return 更新されたプロジェクト（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Project> editProject(
      SnowflakeId projectId, String name, String description, Member projectManager) {
    return projectRepository
        .findById(projectId)
        .map(
            project -> {
              project.edit(name, description, projectManager);
              return projectRepository.save(project);
            });
  }

  /**
   * プロジェクトをアーカイブします。
   *
   * @param projectId アーカイブするプロジェクトのID
   * @return アーカイブされたプロジェクト（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Project> archiveProject(SnowflakeId projectId) {
    return projectRepository
        .findById(projectId)
        .map(
            project -> {
              project.archive();
              return projectRepository.save(project);
            });
  }

  /**
   * プロジェクトを再アクティブ化します。
   *
   * @param projectId 再アクティブ化するプロジェクトのID
   * @return 再アクティブ化されたプロジェクト（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Project> activateProject(SnowflakeId projectId) {
    return projectRepository
        .findById(projectId)
        .map(
            project -> {
              project.activate();
              return projectRepository.save(project);
            });
  }

  /**
   * プロジェクトを削除します。
   *
   * @param projectId 削除するプロジェクトのID
   */
  @Transactional
  public void deleteProject(SnowflakeId projectId) {
    projectRepository.deleteById(projectId);
  }
}
