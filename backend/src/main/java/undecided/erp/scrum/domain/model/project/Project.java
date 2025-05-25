package undecided.erp.scrum.domain.model.project;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * Projectクラスは、エンティティとしてスクラム開発におけるプロジェクトを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`projectId`: プロジェクト固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`name`: プロジェクトの名前。
 *   <li>`description`: プロジェクトの詳細説明。
 *   <li>`status`: プロジェクトの状態（アクティブ、アーカイブなど）。
 *   <li>`projectManager`: プロジェクトマネージャー。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "projects")
@NoArgsConstructor
public class Project extends PptEntity<Project> implements Serializable {

  /**
   * ユニークなプロジェクト識別子を表す変数。
   *
   * <p>アプリケーションにおける各プロジェクトを一意に識別するために使用されます。 データベース上の "project_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "project_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId projectId;

  /**
   * プロジェクトの名前を表す変数。
   *
   * <p>プロジェクトの識別に使用される名前です。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * プロジェクトの詳細説明を表す変数。
   *
   * <p>プロジェクトの詳細な説明や背景情報を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * プロジェクトの状態を表す変数。
   *
   * <p>プロジェクトの現在の状態（アクティブ、アーカイブなど）を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProjectStatus status;

  /**
   * プロジェクトマネージャーを表す変数。
   *
   * <p>このプロジェクトのマネージャーとなるチームメンバーです。 データベース上の "project_manager_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "project_manager_id", nullable = false)
  private Member projectManager;

  /**
   * 新しいプロジェクトを作成します。
   *
   * @param name プロジェクト名
   * @param description プロジェクトの説明
   * @param projectManager プロジェクトマネージャー
   * @return 新しいプロジェクトインスタンス
   */
  public static Project create(String name, String description, Member projectManager) {
    Project project = new Project();
    project.projectId = SnowflakeId.newInstance();
    project.name = name;
    project.description = description;
    project.status = ProjectStatus.ACTIVE;
    project.projectManager = projectManager;
    return project;
  }

  /**
   * プロジェクトを編集します。
   *
   * @param name 新しいプロジェクト名
   * @param description 新しいプロジェクトの説明
   * @param projectManager 新しいプロジェクトマネージャー
   * @return 更新されたプロジェクトインスタンス
   */
  public Project edit(String name, String description, Member projectManager) {
    this.name = name;
    this.description = description;
    this.projectManager = projectManager;
    return this;
  }

  /**
   * プロジェクトをアーカイブします。
   *
   * @return アーカイブされたプロジェクトインスタンス
   */
  public Project archive() {
    this.status = ProjectStatus.ARCHIVED;
    return this;
  }

  /**
   * プロジェクトを再アクティブ化します。
   *
   * @return アクティブ化されたプロジェクトインスタンス
   */
  public Project activate() {
    this.status = ProjectStatus.ACTIVE;
    return this;
  }

  /**
   * このメソッドはProjectクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Projectオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Project{"
        + "projectId="
        + projectId
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", status="
        + status
        + ", projectManager="
        + projectManager
        + '}';
  }
}
