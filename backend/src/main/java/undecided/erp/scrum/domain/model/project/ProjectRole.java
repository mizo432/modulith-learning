package undecided.erp.scrum.domain.model.project;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * ProjectRoleクラスは、エンティティとしてプロジェクト内のロールを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`roleId`: ロール固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`name`: ロールの名前（プロジェクト管理者、メンバー、閲覧者など）。
 *   <li>`description`: ロールの詳細説明。
 *   <li>`canEdit`: プロジェクトの編集権限があるかどうか。
 *   <li>`canInvite`: メンバーの招待権限があるかどうか。
 *   <li>`canManageRoles`: ロールの管理権限があるかどうか。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "project_roles")
@NoArgsConstructor
public class ProjectRole extends PptEntity<ProjectRole> implements Serializable {

  /**
   * ユニークなロール識別子を表す変数。
   *
   * <p>アプリケーションにおける各プロジェクトロールを一意に識別するために使用されます。 データベース上の "role_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "role_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId roleId;

  /**
   * ロールの名前を表す変数。
   *
   * <p>ロールの識別に使用される名前です（プロジェクト管理者、メンバー、閲覧者など）。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  /**
   * ロールの詳細説明を表す変数。
   *
   * <p>ロールの詳細な説明や責任を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * プロジェクトの編集権限があるかどうかを表す変数。
   *
   * <p>このロールを持つメンバーがプロジェクトを編集できるかどうかを示します。 データベース上の "can_edit" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "can_edit", nullable = false)
  private boolean canEdit;

  /**
   * メンバーの招待権限があるかどうかを表す変数。
   *
   * <p>このロールを持つメンバーが新しいメンバーをプロジェクトに招待できるかどうかを示します。 データベース上の "can_invite" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "can_invite", nullable = false)
  private boolean canInvite;

  /**
   * ロールの管理権限があるかどうかを表す変数。
   *
   * <p>このロールを持つメンバーがプロジェクト内のロールを管理できるかどうかを示します。 データベース上の "can_manage_roles" カラムに対応し、null
   * 値は許可されていません。
   */
  @Getter
  @Column(name = "can_manage_roles", nullable = false)
  private boolean canManageRoles;

  /**
   * 新しいプロジェクトロールを作成します。
   *
   * @param name ロール名
   * @param description ロールの説明
   * @param canEdit 編集権限があるかどうか
   * @param canInvite 招待権限があるかどうか
   * @param canManageRoles ロール管理権限があるかどうか
   * @return 新しいプロジェクトロールインスタンス
   */
  public static ProjectRole create(
      String name, String description, boolean canEdit, boolean canInvite, boolean canManageRoles) {
    ProjectRole role = new ProjectRole();
    role.roleId = SnowflakeId.newInstance();
    role.name = name;
    role.description = description;
    role.canEdit = canEdit;
    role.canInvite = canInvite;
    role.canManageRoles = canManageRoles;
    return role;
  }

  /**
   * プロジェクト管理者ロールを作成します。
   *
   * @return プロジェクト管理者ロールインスタンス
   */
  public static ProjectRole createAdminRole() {
    return create("プロジェクト管理者", "プロジェクトの全ての機能にアクセスできる管理者ロール", true, true, true);
  }

  /**
   * プロジェクトメンバーロールを作成します。
   *
   * @return プロジェクトメンバーロールインスタンス
   */
  public static ProjectRole createMemberRole() {
    return create("メンバー", "プロジェクトに参加し、編集できるメンバーロール", true, false, false);
  }

  /**
   * プロジェクト閲覧者ロールを作成します。
   *
   * @return プロジェクト閲覧者ロールインスタンス
   */
  public static ProjectRole createViewerRole() {
    return create("閲覧者", "プロジェクトを閲覧のみできるロール", false, false, false);
  }

  /**
   * このメソッドはProjectRoleクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return ProjectRoleオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "ProjectRole{"
        + "roleId="
        + roleId
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", canEdit="
        + canEdit
        + ", canInvite="
        + canInvite
        + ", canManageRoles="
        + canManageRoles
        + '}';
  }
}
